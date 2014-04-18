package cc.pat.fchat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FChatSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_OPEN_CHANNELS = "open_channels";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "channel_name";
	public static final String COLUMN_TITLE = "channel_title";
	public static final String COLUMN_TYPE = "channel_type";
	public static final String COLUMN_CHARACTER_NUMBER = "channel_character_number";

	private static final String DATABASE_NAME = "cc.pat.fchat.db";
	private static final int DATABASE_VERSION = 1;

	public FChatSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	// Database creation sql statement
//	private static final String DATABASE_CREATE = "create table " + TABLE_COMMENTS + "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_COMMENT + " text not null);";

}
