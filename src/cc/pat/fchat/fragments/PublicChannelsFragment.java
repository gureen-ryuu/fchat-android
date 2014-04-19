package cc.pat.fchat.fragments;

import java.util.ArrayList;

import cc.pat.fchat.FApp;
import cc.pat.fchat.R;
import cc.pat.fchat.adapters.ChannelsAdapter;
import cc.pat.fchat.objects.Channel;
import cc.pat.fchat.objects.Commands;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class PublicChannelsFragment extends Fragment{

	private ListView channelsList;
	private TextView title;
	private ChannelsAdapter publicChannelsAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_channels_list, container, false);
		channelsList = (ListView) fragmentView.findViewById(R.id.channelsList);
		title = (TextView) fragmentView.findViewById(R.id.titleView);
		title.setText("Public Channels");
		FApp.getInstance().mBoundService.sendCommand(Commands.CHA);
        return fragmentView;
    }
	
	public void refreshList(ArrayList<Channel> channels){
		publicChannelsAdapter = new ChannelsAdapter(channels, getActivity().getApplicationContext());
		channelsList.setAdapter(publicChannelsAdapter);		
	}
}
