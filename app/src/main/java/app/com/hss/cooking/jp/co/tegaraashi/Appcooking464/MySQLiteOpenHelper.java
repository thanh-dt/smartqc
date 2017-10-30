package app.com.hss.cooking.jp.co.tegaraashi.Appcooking464;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	static final String DB = "sqlite_magatama.db";
	static final int DB_VERSION = 1;
	static final String CREATE_TABLE = "create table use_coupon ( _id integer primary key autoincrement, coupon_id integer not null );";
	static final String DROP_TABLE = "drop table use_coupon;";

	public MySQLiteOpenHelper(Context c) {
		super(c, DB, null, DB_VERSION);
	}
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE);
		onCreate(db);
	}
}
 
