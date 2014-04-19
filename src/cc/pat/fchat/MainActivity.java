package cc.pat.fchat;

import java.util.ArrayList;

import cc.pat.fchat.fragments.PrivateChannelsFragment;
import cc.pat.fchat.fragments.PublicChannelsFragment;
import cc.pat.fchat.objects.Actions;
import cc.pat.fchat.objects.Channel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {

	private String[] mFragments = { "Public Channels", "Private channels" };
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private Fragment publicRoomsFragment;
	private Fragment privateRoomsFragment;
	public boolean isReceiverRegistered = false;
	public FChatReceiver fChatReceiver;
	
	public class FChatReceiver extends BroadcastReceiver
	{
		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v("Pat","Broadcast event received: " + intent.getAction());
			if(intent.getAction().equalsIgnoreCase(Actions.PUBLIC_CHANNELS_RETRIEVED) && publicRoomsFragment != null){
					ArrayList<Channel> channels = (ArrayList<Channel>) intent.getSerializableExtra("channels");
					((PublicChannelsFragment)publicRoomsFragment).refreshList(channels);
			}
			else if(intent.getAction().equalsIgnoreCase(Actions.PRIVATE_CHANNELS_RETRIEVED) && privateRoomsFragment != null){
					ArrayList<Channel> channels = (ArrayList<Channel>) intent.getSerializableExtra("channels");
					((PrivateChannelsFragment)privateRoomsFragment).refreshList(channels);
			}
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mFragments));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		fChatReceiver = new FChatReceiver();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.v("Pat", "Resuming!!!!!!");
		if (!isReceiverRegistered) {
			IntentFilter filter = new IntentFilter();
			filter.addAction(Actions.PUBLIC_CHANNELS_RETRIEVED);
			filter.addAction(Actions.PRIVATE_CHANNELS_RETRIEVED);
			
			registerReceiver(fChatReceiver, filter);
			isReceiverRegistered = true;
			Log.v("Pat", "Registered receiver!");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.v("Pat", "Pausing!!!!");
		if (isReceiverRegistered) {
			unregisterReceiver(fChatReceiver);
			isReceiverRegistered = false;
		}
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// Create a new fragment and specify the planet to show based on position
		Fragment nextFragment = null;

		switch(position){
			case 0:
				if(publicRoomsFragment == null){
					publicRoomsFragment = new PublicChannelsFragment();
				}
				nextFragment = publicRoomsFragment;				
				break;
			case 1:
				if(privateRoomsFragment == null){
					privateRoomsFragment = new PrivateChannelsFragment();
				}
				nextFragment = privateRoomsFragment;
		}
		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, nextFragment).commit();

		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList);
	}
}
