package com.csula.commuteralarm;

import java.util.ArrayList;
import java.util.List;

import com.csula.commuteralarm.R;
import com.google.android.gms.maps.model.LatLng;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity extends ListActivity  {
	
	public static List<Alarm> alarmList = new ArrayList<Alarm>();
	public static Alarm currentAlarm;		// Used by the map and configure-alarm
	public static boolean isNew = false;	// Indicates if the alarm was newly created
											// used by the map
	
	private Button newBtn;					// Add new alarm button
	
	private DbManager Db;
	private SQLiteDatabase wDb = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ListView listView1 = (ListView)findViewById(android.R.id.list);
		newBtn = (Button)findViewById(R.id.newBtn);

		Db = new DbManager(this);
		wDb = Db.getWritableDatabase();
		
		addDefaultAlarms();
		
		// List alarms
				listView1.setAdapter(new AlarmArrayAdapter(
						HomeActivity.this, alarmList));
				
		// --- Add new alarm button ---
		newBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0){
				// HACK this should be in the constructor of alarm
				Alarm newA = new Alarm();
				alarmList.add(newA);
				newA.name = "Alarm " + String.valueOf(alarmList.size());
				
				// HACK this should be its own method
				/*
				ContentValues values = new ContentValues();
				values.put(Db.DB_COL_ID, 0);
				wDb.insert(Db.DB_TABLE_NAME, null, values);
				*/
				
				// After new blank alarm is created change activity to Map
				Intent i = new Intent();
				i.setClass(HomeActivity.this, Map.class);
				currentAlarm = newA;
				isNew = true;
				startActivity(i);
			}
		});
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		// Refresh alarm list
		ListView listView1 = (ListView)findViewById(android.R.id.list);
		listView1.setAdapter(new AlarmArrayAdapter(
				HomeActivity.this, alarmList));
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	// --- Click on alarm entry ---
	// Go to selected alarm's configuration activity
	@Override
	protected void onListItemClick (ListView l, View v, int position, long id) {
		Intent i = new Intent();
		i.setClass(HomeActivity.this, ConfigureAlarm.class);
		currentAlarm = alarmList.get(position);
		startActivity(i);
	}
	
	// For the demo
	private void addDefaultAlarms(){
		if(alarmList.isEmpty()){
			// HACK - hardcoded stuff. 
			Alarm a = new Alarm();
			a.name = "School";
			a.radius = 500;
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
			b.dest = new LatLng(34.040160, -118.247000);
			alarmList.add(a);
			alarmList.add(b);
		}
	}
}