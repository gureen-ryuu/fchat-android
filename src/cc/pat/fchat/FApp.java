package cc.pat.fchat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.util.LangUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cc.pat.fchat.objects.Actions;
import cc.pat.fchat.objects.ChatCharacter;
import cc.pat.fchat.objects.Commands;
import cc.pat.fchat.objects.Friend;
import cc.pat.fchat.utils.CommandsBuilder;
import cc.pat.fchat.utils.ImageCaching.ImageLruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.content.Intent;
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
	public ArrayList<Friend> friends;
	public HashMap<String, ChatCharacter> onlineCharacters;
	public int onlineCharactersCount = 0;
	public CommandsReceiver commandsReceiver;

	public void saveSessionData(JSONObject sessionJSON, String account, String password) throws JSONException {
		Log.v("Pat", "Thread ID inside session: " + Thread.currentThread().getId());
		characters = new ArrayList<String>();
		friends = new ArrayList<Friend>();
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
			friends.add(new Friend(tmpJSONArray.getJSONObject(index)));
		}
		
		tmpJSONArray = sessionJSON.getJSONArray("bookmarks");
		for (int index = 0; index < tmpJSONArray.length(); index++) {
			bookmarks.add(tmpJSONArray.getString(index));
		}
		
		Log.v("Pat", "session data saved: " + defaultCharacter + " : : " + characters.size() + " ::::::" + CommandsBuilder.IDN(ticket, account, characters.get(0)));
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("Pat", "Creating app");
		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		mImageCache = new ImageLruCache(ImageLruCache.getDefaultLruCacheSize());
		mImageLoader = new ImageLoader(mRequestQueue, mImageCache);

		commandsReceiver = new CommandsReceiver();
		
		onlineCharacters = new HashMap<String, ChatCharacter>();
		instance = this;
	}

	public static FApp getInstance() {
		return instance;
	}

	public void LISDone(){
		
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
					if (payload.startsWith(Commands.CON))
						CON(payload.substring(4));
					else if (payload.startsWith(Commands.LIS))
						LIS(payload.substring(4));
					else if (payload.startsWith(Commands.NLN))
						NLN(payload.substring(4));
					else if (payload.startsWith(Commands.FLN))
						FLN(payload.substring(4));
					else if(payload.startsWith(Commands.MSG))
						MSG(payload.substring(4));
					else if(payload.startsWith(Commands.PRI)){
						PRI(payload.substring(4));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		public void MSG(String payload){
			
		}
		
		public void PRI(String payload){
			
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
	}
	
	private static class ServerVariables {
//		public static 
	}
	
	private void launchMainActivity() {
		Log.v("Pat", "Sendint broadcast");
		Intent loginIntent = new Intent();
		loginIntent.setAction(Actions.LOGGED_IN);
		sendBroadcast(loginIntent);
	}

}
