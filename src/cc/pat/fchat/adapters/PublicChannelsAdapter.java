package cc.pat.fchat.adapters;

import java.util.ArrayList;

import cc.pat.fchat.R;
import cc.pat.fchat.objects.Channel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PublicChannelsAdapter extends BaseAdapter {

	private ArrayList<Channel> channels;
	private Context mContext;

	public PublicChannelsAdapter(ArrayList<Channel> channels, Context context) {
		mContext = context;
		this.channels = channels;
	}

	@Override
	public int getCount() {
		return channels.size();
	}

	@Override
	public Object getItem(int position) {
		return channels.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.public_channel_list_item, parent, false);
			viewHolder = new ViewHolder();

			viewHolder.channelName = (TextView) convertView.findViewById(R.id.channelName);
			viewHolder.characterCount = (TextView) convertView.findViewById(R.id.characterCount);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.channelName.setText(channels.get(pos).channelName);
		viewHolder.characterCount.setText(" (" + String.valueOf(channels.get(pos).charactersNumber)+ ")");

		return convertView;
	}

	static class ViewHolder {
		TextView channelName;
		TextView characterCount;
	}
}
