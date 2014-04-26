package cc.pat.fchat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cc.pat.fchat.objects.Actions;
import cc.pat.fchat.objects.Channel;
import cc.pat.fchat.objects.Channel.ChannelMode;
import cc.pat.fchat.objects.Channel.ChannelType;
import cc.pat.fchat.objects.ChatCharacter;
import cc.pat.fchat.objects.ChatRoomMessage;
import cc.pat.fchat.objects.Commands;
import cc.pat.fchat.objects.Friend;
import cc.pat.fchat.objects.PrivateMessage;
import cc.pat.fchat.utils.CommandsBuilder;
import cc.pat.fchat.utils.ImageCaching.ImageLruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class FApp extends Application {

	public static final String TAG = "Pat";
	private RequestQueue mRequestQueue;
	private ImageLruCache mImageCache;
	private ImageLoader mImageLoader;
	private static FApp instance;

	public String account;
	public String password;
	public String accountID;

	public String ticket;
	public String defaultCharacter;
	public ArrayList<String> characters;
	public ArrayList<String> bookmarks;
	public ArrayList<Friend> accountFriends; //Obtained from API
	//	public HashMap<String, ChatCharacter> friendsList;
	public HashSet<String> friendsList;
	public HashMap<String, Channel> openChannelsList;
	public HashMap<String, Channel> publicChannelsList;

	public HashMap<String, ChatCharacter> onlineCharacters;
	public int onlineCharactersCount = 0;
	public CommandsReceiver commandsReceiver;
	private boolean mBound = false;
	public ChatService mBoundService;
	private Intent chatServiceIntent;
	private HashMap<String, String> serverVariables;

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBoundService = null;
			mBound = false;
			Log.v("pat", "Disconnected from service from FApp!");

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBoundService = ((ChatService.ChatBinder) service).getService();
			mBound = true;
			Log.v("Pat", "Connected to service from FApp!");
		}
	};

	public void saveSessionData(JSONObject sessionJSON, String account, String password) throws JSONException {
		Log.v("Pat", "Thread ID inside session: " + Thread.currentThread().getId());
		characters = new ArrayList<String>();
		accountFriends = new ArrayList<Friend>();
		bookmarks = new ArrayList<String>();

		JSONArray tmpJSONArray;

		ticket = sessionJSON.getString("ticket");
		accountID = sessionJSON.getString("account_id");
		defaultCharacter = sessionJSON.getString("default_character");
		this.account = account;
		this.password = password;

		tmpJSONArray = sessionJSON.getJSONArray("characters");
		for (int index = 0; index < tmpJSONArray.length(); index++) {
			characters.add(tmpJSONArray.getString(index));
		}

		tmpJSONArray = sessionJSON.getJSONArray("friends");
		for (int index = 0; index < tmpJSONArray.length(); index++) {
			accountFriends.add(new Friend(tmpJSONArray.getJSONObject(index)));
		}

		tmpJSONArray = sessionJSON.getJSONArray("bookmarks");
		for (int index = 0; index < tmpJSONArray.length(); index++) {
			bookmarks.add(tmpJSONArray.getString(index));
		}

		Log.v("Pat", "session data saved: " + defaultCharacter + " : : " + characters.size() + " ::::::" + CommandsBuilder.IDN(ticket, account, characters.get(0)));
	}

	private void startChatService() {
		chatServiceIntent = new Intent(getApplicationContext(), ChatService.class);
		if (ChatService.isInstanceCreated()) {
			Log.v("Pat", "Calling stop service");
			try {
				stopService(chatServiceIntent);
			} catch (RuntimeException ex) {
			}
		}

		startService(chatServiceIntent);

		bindService(chatServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		startChatService();

		Log.v("Pat", "Creating app");
		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		mImageCache = new ImageLruCache(ImageLruCache.getDefaultLruCacheSize());
		mImageLoader = new ImageLoader(mRequestQueue, mImageCache);

		commandsReceiver = new CommandsReceiver();
		onlineCharacters = new HashMap<String, ChatCharacter>();
		openChannelsList = new HashMap<String, Channel>();
		publicChannelsList = new HashMap<String, Channel>();
		//		friendsList = new HashMap<String, ChatCharacter>();
		friendsList = new HashSet<String>();
		serverVariables = new HashMap<String, String>();
		instance = this;
	}

	public static FApp getInstance() {
		return instance;
	}

	public void LISDone() {

	}

	public RequestQueue getRequestQueue() {
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		VolleyLog.d("Adding request to queue: %s", req.getUrl());

		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		Log.v("Pat", "Adding to request queue");
		req.setTag(TAG);

		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public ImageLruCache getCache() {
		return mImageCache;
	}

	public RequestQueue getQueue() {
		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	public class CommandsReceiver {

		private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		private final CommandHandlerThread commandHandlerThread = new CommandHandlerThread();

		public CommandsReceiver() {
			commandHandlerThread.start();
		}

		public void receiveCommand(String payload) {
			try {
				queue.put(payload);
			} catch (InterruptedException e1) {
			}
		}

		private class CommandHandlerThread extends Thread {

			@Override
			public void run() {
				while (true) {
					try {
						handleNextCommand();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} // Blocks until queue isn't empty.
				}
			}

			private void handleNextCommand() throws InterruptedException {
				String payload = queue.take();
				try {
					if (payload.startsWith(Commands.CHA))
						CHA(payload.substring(4));
					else if (payload.startsWith(Commands.CON))
						CON(payload.substring(4));
					else if (payload.startsWith(Commands.FLN))
						FLN(payload.substring(4));
					else if (payload.startsWith(Commands.FRL))
						FRL(payload.substring(4));
					else if (payload.startsWith(Commands.LIS))
						LIS(payload.substring(4));
					else if (payload.startsWith(Commands.MSG))
						MSG(payload.substring(4));
					else if (payload.startsWith(Commands.NLN))
						NLN(payload.substring(4));
					else if (payload.startsWith(Commands.ORS))
						ORS(payload.substring(4));
					else if (payload.startsWith(Commands.PRI))
						PRI(payload.substring(4));
					else if (payload.startsWith(Commands.VAR))
						VAR(payload.substring(4));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		public void CHA(String payload) throws JSONException {
			JSONObject payloadJSON = new JSONObject(payload);
			JSONArray channelsJSON = payloadJSON.getJSONArray("channels");
			
			ArrayList<Channel> publicChannels = new ArrayList<Channel>();
			for(int i=0; i < channelsJSON.length(); i++){
				JSONObject channelJSON = channelsJSON.getJSONObject(i);
				Channel channel = new Channel();
				channel.charactersNumber = channelJSON.getInt("characters");
				channel.channelName = channelJSON.getString("name");
				channel.channelTitle = channel.channelName;
				channel.channelType = ChannelType.PUBLIC;
				channel.channelMode = ChannelMode.valueOf(channelJSON.getString("mode").toUpperCase());
//				publicChannelsList.put(channel.channelName, channel);
				publicChannels.add(channel);
			}
			Collections.sort(publicChannels, new PublicChannelComparator());
			Intent channelsRetrievedIntent = new Intent();
			channelsRetrievedIntent.setAction(Actions.PUBLIC_CHANNELS_RETRIEVED);
			channelsRetrievedIntent.putExtra("channels", publicChannels);
			sendBroadcast(channelsRetrievedIntent);
		}
		
		public class PublicChannelComparator implements Comparator<Channel> {
		    @Override
		    public int compare(Channel o1, Channel o2) {
		        return o1.channelName.compareToIgnoreCase(o2.channelName);
		    }
		}
		
		public class PrivateChannelComparator implements Comparator<Channel> {
		    @Override
		    public int compare(Channel o1, Channel o2) {
		        return o2.charactersNumber - o1.charactersNumber;
		    }
		}
		/**Syntax
		>> ORS { "channels": [object] }
		Raw sample
		ORS { channels: [{"name":"ADH-300f8f419e0c4814c6a8","characters":0,"title":"Ariel's Fun Club"},
						{"name":"ADH-d2afa269718e5ff3fae7","characters":6,"title":"Monster Girl Dungeon RPG"},
						{"name":"ADH-75027f927bba58dee47b","characters":2,"title":"Naruto Descendants OOC"} ...] }
		*/
		
		
		public void ORS(String payload) throws JSONException {
			JSONObject payloadJSON = new JSONObject(payload);
			JSONArray channelsJSON = payloadJSON.getJSONArray("channels");
			
			ArrayList<Channel> privateChannels = new ArrayList<Channel>();	
			for(int i=0; i < channelsJSON.length(); i++){
				JSONObject channelJSON = channelsJSON.getJSONObject(i);
				Channel channel = new Channel();
				channel.charactersNumber = channelJSON.getInt("characters");
				channel.channelName = channelJSON.getString("name");
				channel.channelTitle = channelJSON.getString("title");
				channel.channelType = ChannelType.PRIVATE;
				channel.channelMode = ChannelMode.BOTH;
				privateChannels.add(channel);
			}
			Collections.sort(privateChannels, new PrivateChannelComparator());
			Intent channelsRetrievedIntent = new Intent();
			channelsRetrievedIntent.setAction(Actions.PRIVATE_CHANNELS_RETRIEVED);
			channelsRetrievedIntent.putExtra("channels", privateChannels);
			sendBroadcast(channelsRetrievedIntent);
		}

		/** Syntax
	 	MSG { "character": string, "message": string, "channel": string }
		*/		
		public void MSG(String payload) throws JSONException {
			JSONObject payloadJSON = new JSONObject(payload);
			String senderIdentity = payloadJSON.getString("character");
			String channelID = payloadJSON.getString("channel");
			ChatRoomMessage chatMessage = new ChatRoomMessage();
			chatMessage.from = (ChatCharacter) onlineCharacters.get(senderIdentity);
			chatMessage.message = payloadJSON.getString("message");
			chatMessage.sentTime = Calendar.getInstance().getTime();
			chatMessage.isSender = false;
		}

		
		/** PRI { "character": string, "message": string }
		 */
		public void PRI(String payload) throws JSONException {
			JSONObject payloadJSON = new JSONObject(payload);
			String senderIdentity = payloadJSON.getString("character");
			PrivateMessage chatMessage = new PrivateMessage();
			chatMessage.from = (ChatCharacter) onlineCharacters.get(senderIdentity);
			chatMessage.message = payloadJSON.getString("message");
			chatMessage.sentTime = Calendar.getInstance().getTime();
			chatMessage.isSender = false;
		}

		public void CON(String payload) throws JSONException {
			JSONObject payloadJSON = new JSONObject(payload);
			onlineCharactersCount = payloadJSON.getInt("count");
		}

		public void LIS(String payload) throws JSONException {
			JSONObject payloadJSON = new JSONObject(payload);
			JSONArray characters = payloadJSON.getJSONArray("characters");

			for (int i = 0; i < characters.length(); i++) {
				ChatCharacter character = new ChatCharacter();
				character.initLIS(characters.getJSONArray(i));
				onlineCharacters.put(character.identity, character);
			}

			if (onlineCharacters.size() == onlineCharactersCount) {
				Intent lisIntent = new Intent();
				lisIntent.setAction(Actions.LIS_DONE);
				sendBroadcast(lisIntent);
				launchMainActivity();
			}
		}

		public void NLN(String payload) throws JSONException {
			JSONObject payloadJSON = new JSONObject(payload);
			ChatCharacter onlineCharacter = new ChatCharacter();
			onlineCharacter.initNLN(payloadJSON);
			onlineCharacters.put(onlineCharacter.identity, onlineCharacter);
		}

		public void FLN(String payload) throws JSONException {
			JSONObject payloadJSON = new JSONObject(payload);
			String identity = payloadJSON.getString("character");
			onlineCharacters.get(identity).setFLN();
		}

		public void STA(String payload) throws JSONException {
			JSONObject payloadJSON = new JSONObject(payload);
			String identity = payloadJSON.getString("character");
			onlineCharacters.get(identity).changeStatus(payloadJSON);
		}

		public void VAR(String payload) throws JSONException {
			JSONObject payloadJSON = new JSONObject(payload);
			String value = payloadJSON.getString("value");
			String variable = payloadJSON.getString("variable");
			serverVariables.put(variable, value);
		}

		public void FRL(String payload) throws JSONException {
			JSONObject payloadJSON = new JSONObject(payload);
			JSONArray friendsListJSON = payloadJSON.getJSONArray("characters");
			for (int i = 0; i < friendsListJSON.length(); i++) {
				friendsList.add(friendsListJSON.getString(i));
			}
		}
	}

	private void launchMainActivity() {
		Log.v("Pat", "Sendint broadcast");
		Intent loginIntent = new Intent();
		loginIntent.setAction(Actions.LOGGED_IN);
		sendBroadcast(loginIntent);
	}

}
