package cc.pat.fchat.fragments;

import java.util.ArrayList;

import cc.pat.fchat.FApp;
import cc.pat.fchat.R;
import cc.pat.fchat.adapters.ChannelsAdapter;
import cc.pat.fchat.adapters.MessageCursorAdapter;
import cc.pat.fchat.db.FChatSQLiteHelper;
import cc.pat.fchat.objects.Channel;
import cc.pat.fchat.objects.ChatMessage;
import cc.pat.fchat.objects.Commands;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ChannelFragment extends Fragment {

	private ListView messageList;
	private TextView title;
	private MessageCursorAdapter messageCursorAdapter;
	private Cursor messageCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_channel, container, false);
		messageCursor = FApp.getInstance().fChatSQLiteHelper.getChannelMessages("");
		//		title = (TextView) fragmentView.findViewById(R.id.titleView);
		//		title.setText("Channel test");
		//		Log.v("Pat", "opening message fragment: " + messageCursor.getCount() + " : : " + messageCursor.getColumnCount() + " ::: COLUMN NAME: " + messageCursor.getColumnName(0));

		messageList = (ListView) fragmentView.findViewById(R.id.messageList);

		messageCursorAdapter = new MessageCursorAdapter(getActivity().getApplicationContext(), R.layout.channel_message, messageCursor,
				new String[] { FChatSQLiteHelper.COLUMN_MESSAGE_FROM, FChatSQLiteHelper.COLUMN_MESSAGE_BODY }, new int[] { R.id.fromTextView, R.id.messageBodyTextView },
				SimpleCursorAdapter.FLAG_AUTO_REQUERY);
		messageList.setAdapter(messageCursorAdapter);
		return fragmentView;
	}

	public void refreshList() {
		Log.v("Pat", "Refreshing list!!!");
		messageCursor = FApp.getInstance().fChatSQLiteHelper.getChannelMessages("");
		messageCursorAdapter.changeCursor(messageCursor);
		messageList.smoothScrollToPosition(messageList.getCount()-1);
//		messageCursor.requery();
//		messageCursorAdapter.notifyDataSetChanged();
		
	}
}
