package cc.pat.fchat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

public class ChatService extends Service {

	private String username;
	private String password;
	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private String hostURL = "chat.f-list.net:8722";

	private final static String TAG = "pat";

	private final static int CONNECT = 0;
	private final static int INIT_CHAT_LISTENER = 1;
	private final static int SEND_MESSAGE = 2;
	private final static int INIT_CONTACTS_LISTENER = 3;
	private final static int ADD_CHAT = 4;
	private final static int LOGIN = 5;

	private static ChatService instance = null;

	public static boolean isInstanceCreated() {
		return instance != null;
	}

	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// final int startId = msg.arg1;
			final int operationType = msg.arg2;

			switch (operationType) {
			case CONNECT:
				connect();
				break;
			case LOGIN:
				break;
			case INIT_CHAT_LISTENER:
				initChats();
				initChatListener();
				initContacts();
				// initMessageListener();
				break;
			case SEND_MESSAGE:
				sendMessage(msg.getData());
				break;
			case INIT_CONTACTS_LISTENER:
				initContacts();
				initContactsListener();
				break;
			case ADD_CHAT:
				addChat(msg.getData());
				break;
			}
		}

		private void addChat(Bundle data) {
		}

		private void initContacts() {
		}

		private void initContactsListener() {
		}

		private void sendMessage(Bundle data) {

			Intent messageIntent = new Intent();
			// messageIntent.setAction(Action.MESSAGE_RECEIVED);
			// messageIntent.putExtra("participant", chat.getParticipant());
			sendBroadcast(messageIntent);
		}

		private void initMessageListener() {
		}

		private void initChats() {
		}

		private void initChatListener() {
		}

		private void handleChatCollision() {
		}

		private class FooMessageListener {
		}

		private boolean login() {
			return true;
		}

		private boolean connect() {
			final WebSocketConnection mConnection = new WebSocketConnection();
			final String wsuri = "ws://" + hostURL;

			try {
				mConnection.connect(wsuri, new WebSocketHandler() {

					@Override
					public void onOpen() {
						Log.d(TAG, "Status: Connected to " + wsuri);
						//						mConnection.sendTextMessage("Hello, world!");
					}

					@Override
					public void onTextMessage(String payload) {
						Log.d(TAG, "Got echo: " + payload);
					}

					@Override
					public void onClose(int code, String reason) {
						Log.d(TAG, "Connection lost.");
					}
				});
			} catch (WebSocketException e) {

				Log.d(TAG, e.toString());
				return false;
			}
			return true;
		}

		private void launchMainActivity() {
			Log.v("Pat", "Sendint broadcast");
			// Intent loginIntent = new Intent();
			// loginIntent.setAction(Action.LOGGED_IN);
			// sendBroadcast(loginIntent);
		}

		

	}

	@Override
	public void onCreate() {
		Log.v("Pat", "Starting service");
		instance = this;
		HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
	}

	private int startId;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			username = extras.getString("username");
			password = extras.getString("password");
		}
		this.startId = startId;
		Log.v("Pat", "Service started " + startId + ":" + flags + " username: " + username + " password: " + password);
		return Service.START_REDELIVER_INTENT;
	}

	public void connect(String username, String password) {
		this.username = username;
		this.password = password;

		Log.v("Pat", "Starting connection");
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		msg.arg2 = CONNECT;
		mServiceHandler.sendMessage(msg);
	}

	public void initChatListener() {
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		msg.arg2 = INIT_CHAT_LISTENER;
		mServiceHandler.sendMessage(msg);
	}

	public void addChat(String participant) {
		Message msg = mServiceHandler.obtainMessage();
		Bundle args = new Bundle();
		args.putString("participant", participant);
		msg.arg1 = startId;
		msg.arg2 = ADD_CHAT;
		msg.setData(args);
		mServiceHandler.sendMessage(msg);
	}

	public void sendMessage(String participant, String message) {
		Message msg = mServiceHandler.obtainMessage();
		Bundle args = new Bundle();
		args.putString("participant", participant);
		args.putString("message", message);
		msg.arg1 = startId;
		msg.arg2 = SEND_MESSAGE;
		msg.setData(args);
		mServiceHandler.sendMessage(msg);
	}

	public class ChatBinder extends Binder {
		ChatService getService() {
			return ChatService.this;
		}
	}

	private IBinder mBinder = new ChatBinder();
	private boolean mAllowRebind = true;

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return mAllowRebind;
	}

	@Override
	public void onDestroy() {
		instance = null;
		Log.v("Pat", "Service destroyed!!!" + startId);
		super.onDestroy();
	}

	public boolean isConnected() {
		// if(connection == null)
		// return false;
		//
		// return connection.isConnected() && connection.isAuthenticated();
		return true;
	}

}
