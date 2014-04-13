package cc.pat.fchat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import cc.pat.fchat.objects.Actions;
import cc.pat.fchat.objects.Commands;
import cc.pat.fchat.utils.CommandsBuilder;
import cc.pat.fchat.utils.CommandsReceiver;

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

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private String hostURL = "chat.f-list.net:9722";

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
				connect(msg.getData().getString("character"));
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

		private class FooMessageListener {
		}

		private boolean login() {
			return true;
		}

		private boolean connect(final String character) {
			final WebSocketConnection mConnection = new WebSocketConnection();
			final String wsuri = "ws://" + hostURL;

			try {
				mConnection.connect(wsuri, new WebSocketHandler() {

					@Override
					public void onOpen() {
						Log.d(TAG, "Status: Connected to " + wsuri);
						Log.v("Pat", "Sending IDN: " + CommandsBuilder.IDN(character));
						mConnection.sendTextMessage(CommandsBuilder.IDN(character));
						launchMainActivity();
					}

					@Override
					public void onTextMessage(String payload) {
						Log.d(TAG, "Got echo: " + payload.substring(0, 3));
						if(payload.equals(Commands.PIN)){
							Log.d("Pat", "Got PIN, returning PIN");
							mConnection.sendTextMessage(CommandsBuilder.PIN());
							return;
						}
						CommandsReceiver.receiveCommand(payload);						 
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
			Intent loginIntent = new Intent();
			loginIntent.setAction(Actions.LOGGED_IN);
			sendBroadcast(loginIntent);
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
		this.startId = startId;
		return Service.START_STICKY;
	}

	public void connect(String character) {
		Log.v("Pat", "Starting connection");
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		msg.arg2 = CONNECT;
		Bundle args = new Bundle();
		args.putString("character", character);
		msg.setData(args);
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
