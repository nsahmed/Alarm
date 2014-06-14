package com.csula.commuteralarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class AndroidAlarmServiceStarter extends BroadcastReceiver implements LocationListener{

	Location currentLoc = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        String provider = locationManager.getBestProvider(c, false);
        currentLoc = locationManager.getLastKnownLocation(provider);
		Alarm a = intent.getParcelableExtra("com.example.mydemo.alarm");
		if(a.isInProgress() && a.isInRadius(currentLoc)) {
			Intent i = new Intent();
			i.setClass(null, AndroidAlarmService.class);
			new AndroidAlarmService().startActivity(null);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		currentLoc = location;		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

}
