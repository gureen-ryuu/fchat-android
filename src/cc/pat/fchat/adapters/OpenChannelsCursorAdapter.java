package cc.pat.fchat.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

public class OpenChannelsCursorAdapter extends SimpleCursorAdapter {

	public OpenChannelsCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}

}
