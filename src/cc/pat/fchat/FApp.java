package cc.pat.fchat;

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
	public static FApp instance;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("Pat", "Creating app");
		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		mImageCache = new ImageLruCache(ImageLruCache.getDefaultLruCacheSize());
		mImageLoader = new ImageLoader(mRequestQueue, mImageCache);
		instance = this;
	}
	
	public RequestQueue getRequestQueue() {
		return mRequestQueue;
	}
	
	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		VolleyLog.d("Adding request to queue: %s", req.getUrl());

		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		// set the default tag if tag is empty
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
	
	public ImageLoader getImageLoader(){
		return mImageLoader;
	}

}
