//package cc.pat.fchat.datasources;
//
//import cc.pat.fchat.db.FChatSQLiteHelper;
//import cc.pat.fchat.objects.Channel;
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//
//public class OpenChannelsDataSource {
//	private SQLiteDatabase database;
//	private FChatSQLiteHelper dbHelper;
//	private String[] allColumns = { FChatSQLiteHelper.COLUMN_CHANNEL_ID, FChatSQLiteHelper.COLUMN_CHANNEL_MODE, FChatSQLiteHelper.COLUMN_CHANNEL_NAME,
//			FChatSQLiteHelper.COLUMN_CHANNEL_TITLE, FChatSQLiteHelper.COLUMN_CHANNEL_TYPE, FChatSQLiteHelper.COLUMN_CHANNEL_CHARACTER_NUMBER };
//
//	public OpenChannelsDataSource(Context context) {
//		dbHelper = new FChatSQLiteHelper(context);
//	}
//
//	public void open() throws SQLException {
//		database = dbHelper.getWritableDatabase();
//	}
//
//	public void close() {
//		dbHelper.close();
//	}
//
//	public Channel openChannel(Channel channel) {
//		ContentValues values = new ContentValues();
//		values.put(FChatSQLiteHelper.COLUMN_CHANNEL_MODE, channel.channelMode.name());
//		values.put(FChatSQLiteHelper.COLUMN_CHANNEL_NAME, channel.channelName);
//		values.put(FChatSQLiteHelper.COLUMN_CHANNEL_TITLE, channel.channelTitle);
//		values.put(FChatSQLiteHelper.COLUMN_CHANNEL_TYPE, channel.channelType.name());
//		values.put(FChatSQLiteHelper.COLUMN_CHANNEL_CHARACTER_NUMBER, channel.charactersNumber);
//
//		database.insert(FChatSQLiteHelper.TABLE_OPEN_CHANNELS, null, values);
//		return channel;
//	}
//
//	public void deleteComment(Channel channel) {
//		database.delete(FChatSQLiteHelper.TABLE_OPEN_CHANNELS, FChatSQLiteHelper.COLUMN_CHANNEL_NAME
//				+ " = " + channel.channelTitle, null);
//	}
//
//	public List<Comment> getAllComments() {
//		List<Comment> comments = new ArrayList<Comment>();
//
//		Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
//				allColumns, null, null, null, null, null);
//
//		cursor.moveToFirst();
//		while (!cursor.isAfterLast()) {
//			Comment comment = cursorToComment(cursor);
//			comments.add(comment);
//			cursor.moveToNext();
//		}
//		// make sure to close the cursor
//		cursor.close();
//		return comments;
//	}
//
//	private Comment cursorToComment(Cursor cursor) {
//		Comment comment = new Comment();
//		comment.setId(cursor.getLong(0));
//		comment.setComment(cursor.getString(1));
//		return comment;
//	}
//}
