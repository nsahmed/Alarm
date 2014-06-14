package com.csula.commuteralarm;


import java.util.Calendar;

import com.example.mapdemo.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AndroidAlarmService extends Activity {

	private PendingIntent pendingIntent;
	MediaPlayer mMediaPlayer = new MediaPlayer();
	Context ctx = this;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		Button buttonCancel = (Button)findViewById(R.id.cancelalarm);
		Button buttonSnooze = (Button)findViewById(R.id.snoozealarm);

		Intent myIntent = new Intent(AndroidAlarmService.this, MyAlarmService.class);
		pendingIntent = PendingIntent.getService(AndroidAlarmService.this, 0, myIntent, 0);

		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 10);
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
		Toast.makeText(AndroidAlarmService.this, "Start Alarm", Toast.LENGTH_LONG).show();

		try {
			Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			mMediaPlayer.setDataSource(ctx, alert);
			final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.setLooping(true);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
				Vibrator vibrator = (Vibrator) AndroidAlarmService.this
						.getSystemService(AndroidAlarmService.VIBRATOR_SERVICE);
				vibrator.vibrate(2000);
			}
		} catch(Exception e) {
		} 

		buttonCancel.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
				alarmManager.cancel(pendingIntent);*/
				mMediaPlayer.stop();

				// Tell the user about what we did.
				Toast.makeText(AndroidAlarmService.this, "Cancel!", Toast.LENGTH_LONG).show();


			}});

		buttonSnooze.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mMediaPlayer.pause();
				new java.util.Timer().schedule( 
						new java.util.TimerTask() {
							@Override
							public void run() {
								mMediaPlayer.start();
							}
						}, 
						5000 
						);
			}
		});

	}
}