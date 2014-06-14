package com.csula.commuteralarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager extends SQLiteOpenHelper{
	final static String DB_DATABASE_NAME = "CommuterAlarm";
	final static String DB_TABLE_NAME = "Alarms";
	final static String DB_COL_ID = "Id";
	final static String DB_COL_NAME = "Name";
	final static String DB_COL_ISACTIVE = "IsActive";
	final static String DB_COL_RADIUS = "Radius";
	final static String DB_COL_DAYS = "Days";
	final static String DB_COL_STARTTIME = "StartTime";
	final static String DB_COL_ENDTIME = "EndTime";
	
	final static String DB_CREATE =
		"CREATE TABLE " + DB_TABLE_NAME + "(" +
			DB_COL_ID + " PRIMARY KEY, AUTO_INCREMENT, " +
			DB_COL_NAME + " varchar(64), " +
			DB_COL_ISACTIVE + " boolean, " +
			DB_COL_RADIUS + " int, " +
			DB_COL_DAYS + " int, " + 
			DB_COL_STARTTIME + " time, " +
			DB_COL_ENDTIME + " time "
		+ ");"
	;
	
	final Context mContext;
	
	public DbManager(Context context){
		super(context, DB_DATABASE_NAME, null, 1);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	void deleteDatabase() {
		mContext.deleteDatabase(DB_DATABASE_NAME);
	}
}
