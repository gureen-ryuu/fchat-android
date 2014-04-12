package cc.pat.fchat;

import cc.pat.fchat.utils.ImageCaching.ImageLruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.text.TextUtils;

public class FApp extends Application {

	public static final String TAG = "Pat";
	private RequestQueue mRequestQueue;
	private ImageLruCache mImageCache;
	private ImageLoader mImageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		mImageCache = new ImageLruCache(ImageLruCache.getDefaultLruCacheSize());
		mImageLoader = new ImageLoader(mRequestQueue, mImageCache);
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
