package com.csula.commuteralarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.mapdemo.R;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity extends ListActivity {

	List<Alarm> result = new ArrayList<Alarm>();
	private Button newBtn, startAlarm;
	public static Alarm currentAlarm;
	private DbManager Db;
	private SQLiteDatabase wDb = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ListView listView1 = (ListView)findViewById(android.R.id.list);
		newBtn = (Button)findViewById(R.id.newBtn);
		startAlarm = (Button)findViewById(R.id.startAlarm);

		Db = new DbManager(this);
		wDb = Db.getWritableDatabase();

		// HACK - hardcoded stuff. This will be loaded from data file
		Alarm a = new Alarm();
		a.name = "School";
		a.radius = 500;
		a.time = 1000;
		a.days[2] = false;
		a.startTimeHour = 13;
		a.startTimeMin = 30;
		a.endTimeHour = 15;
		a.endTimeMin = 0;
		Alarm b = new Alarm();
		b.isOn = true;
		b.name = "Work";
		b.radius = 500;
		b.startTimeHour = 7;
		b.startTimeMin = 5;
		b.endTimeHour = 9;
		b.endTimeMin = 45;
		result.add(a);
		result.add(b);


		newBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){
				// HACK this should be in the constructor of alarm
				Alarm newA = new Alarm();
				newA.name = "New Alarm";
				//newA.day_of_week = Alarm.day.Monday;
				newA.radius = 500;
				newA.time = 500;
				result.add(newA);

				// HACK this should be its own method
				//ContentValues values = new ContentValues();

				//values.put(Db.DB_COL_ID, 0);
				//wDb.insert(Db.DB_TABLE_NAME, null, values);


				Intent i = new Intent();
				//i.setClass(HomeActivity.this, ConfigureAlarm.class);
				i.setClass(HomeActivity.this, Map.class);
				currentAlarm = newA;

				startActivity(i);
			}
		});

		startAlarm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){
				Intent i = new Intent();
				i.setClass(HomeActivity.this, AndroidAlarmService.class);
				startActivity(i);
			}
		});

		listView1.setAdapter(new AlarmArrayAdapter(
				HomeActivity.this, result));
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Refresh alarm list
		ListView listView1 = (ListView)findViewById(android.R.id.list);
		for(Alarm a: result) {
			Intent alarmIntent = new Intent(getBaseContext(), AndroidAlarmServiceStarter.class);
			alarmIntent.putExtra("com.example.mydemo.alarm", a);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			alarmManager.setRepeating(AlarmManager.RTC,  Calendar.getInstance().getTimeInMillis(), 
					60*1000,pendingIntent);
		}
		listView1.setAdapter(new AlarmArrayAdapter(
				HomeActivity.this, result));
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onListItemClick (ListView l, View v, int position, long id) {
		Intent i = new Intent();
		i.setClass(HomeActivity.this, ConfigureAlarm.class);
		//i.setClass(HomeActivity.this, Map.class);
		currentAlarm = result.get(position);
		startActivity(i);
	}
}