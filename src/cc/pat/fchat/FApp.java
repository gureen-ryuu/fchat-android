package cc.pat.fchat;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cc.pat.fchat.objects.ChatCharacter;
import cc.pat.fchat.objects.Friend;
import cc.pat.fchat.utils.CommandsBuilder;
import cc.pat.fchat.utils.ImageCaching.ImageLruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import android.app.Application;
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

}
