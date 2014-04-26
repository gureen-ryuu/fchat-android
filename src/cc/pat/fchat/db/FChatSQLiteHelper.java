package cc.pat.fchat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FChatSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_OPEN_CHANNELS = "open_channels";
	public static final String COLUMN_CHANNEL_ID = "_id";
	public static final String COLUMN_CHANNEL_NAME = "channel_name";
	public static final String COLUMN_CHANNEL_TITLE = "channel_title";
	public static final String COLUMN_CHANNEL_TYPE = "channel_type";
	public static final String COLUMN_CHANNEL_MODE = "channel_mode";
	public static final String COLUMN_CHANNEL_CHARACTER_NUMBER = "channel_character_number";
	
	public static final String TABLE_OPEN_MESSAGES_CHATS = "open_messages_chats";
	public static final String COLUMN_MESSAGES_CHATS_ID = "_id";
	public static final String COLUMN_MESSAGES_CHATS_PARTICIPANT = "message_chat_participant";

	public static final String TABLE_PRIVATE_MESSAGES = "open_messages";
	public static final String COLUMN_MESSAGE_ID = "_id";
	public static final String COLUMN_MESSAGE_FROM = "message_from";
	public static final String COLUMN_MESSAGE_BODY = "message_body";
	public static final String COLUMN_MESSAGE_ISSENDER = "message_is_sender";
	public static final String COLUMN_MESSAGE_TIMESTAMP = "message_timestamp";
	public static final String COLUMN_MESSAGE_CHAT_ID = "message_chat_id";

	private static final String DATABASE_NAME = "cc.pat.fchat.db";
	private static final int DATABASE_VERSION = 1;

	public FChatSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL(DATABASE_CREATE_CHANNELS);
		 db.execSQL(DATABASE_CREATE_MESSAGES);
//		 db.execSQL(DATABASE_CREATE_CHANNELS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("Pat",
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRIVATE_MESSAGES);
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPEN_CHANNELS);
		    onCreate(db);
	}

	//	 Database creation sql statement
	private static final String DATABASE_CREATE_CHANNELS = "create table " + TABLE_OPEN_CHANNELS
			+ "("
			+ COLUMN_CHANNEL_ID + " integer primary key autoincrement, "
			+ COLUMN_CHANNEL_NAME + " text not null, "
			+ COLUMN_CHANNEL_TITLE + " text not null, "
			+ COLUMN_CHANNEL_CHARACTER_NUMBER + " unsigned int, "
			+ COLUMN_CHANNEL_TYPE + " text not null, "
			+ COLUMN_CHANNEL_MODE + " text not null"
			+ ");";

//	private static final String DATABASE_CREATE_MESSAGES_CHAT = "create table " + TABLE_OPEN_MESSAGES_CHATS
//			+ "("
//			+ COLUMN_MESSAGES_CHATS_ID + " integer primary key autoincrement, "
//			+ COLUMN_MESSAGES_CHATS_PARTICIPANT + " text not null "
//			+ ");";
	
	private static final String DATABASE_CREATE_MESSAGES = "create table " + TABLE_PRIVATE_MESSAGES
			+ "("
			+ COLUMN_MESSAGE_ID + " integer primary key autoincrement, "
			+ COLUMN_MESSAGE_FROM + " text not null, "
			+ COLUMN_MESSAGE_BODY + " text not null, "
			+ COLUMN_CHANNEL_CHARACTER_NUMBER + " unsigned int, "
			+ COLUMN_MESSAGE_ISSENDER + " text not null, "
			+ COLUMN_MESSAGE_TIMESTAMP + " text not null, "
			+ COLUMN_MESSAGE_CHAT_ID + " text not null"
			+ ");";
	
}
