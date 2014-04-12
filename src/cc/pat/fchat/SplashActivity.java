package cc.pat.fchat;

import android.support.v4.app.FragmentActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SplashActivity extends FragmentActivity {

	private boolean mBound = false;
	private boolean isReceiverRegistered = false;
	private ChatService mBoundService;
	private ChatReceiver chatReceiver;
	private Intent chatServiceIntent;

	private Button loginButton;
	private EditText username;
	private EditText password;

	public class ChatReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v("Pat", "Broadcast event received!!!");
			if (intent.getAction().equalsIgnoreCase(Action.LOGGED_IN)) {
				Intent i = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBoundService = null;
			mBound = false;
			Log.v("pat", "Disconnected from service");
			loginButton.setOnClickListener(null);
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBoundService = ((ChatService.ChatBinder) service).getService();
			mBound = true;
			Log.v("Pat", "Connected to service!");
			loginButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.v("Pat", "Login in: " + username.getText().toString());
					mBoundService.connect(username.getText().toString(), password.getText().toString());
				}
			});
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		if (savedInstanceState == null) {
		}

		chatServiceIntent = new Intent(getApplicationContext(), ChatService.class);
		if (ChatService.isInstanceCreated()) {
			Log.v("Pat", "Calling stop service");
			try {
				stopService(chatServiceIntent);
			} catch (RuntimeException ex) {
			}
		}

		chatReceiver = new ChatReceiver();
		startService(chatServiceIntent);
		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		
		username.setText("patochan");
		password.setText("Ir0nmaiden");
		loginButton = (Button) findViewById(R.id.loginButton);
	}

	@Override
	public void onStart() {
		super.onStart();
		bindService(chatServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
		Log.v("Pat", "onStart activity");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.v("Pat", "Resuming!!!!!!");
		if (!isReceiverRegistered) {
			IntentFilter filter = new IntentFilter();
			filter.addAction(Action.LOGGED_IN);

			registerReceiver(chatReceiver, filter);
			isReceiverRegistered = true;
			Log.v("Pat", "Registered receiver!");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.v("Pat", "Pausing!!!!");
		if (isReceiverRegistered) {
			unregisterReceiver(chatReceiver);
			isReceiverRegistered = false;
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		// Unbind from the service
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
