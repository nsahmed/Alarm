package com.csula.commuteralarm;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import com.google.android.gms.maps.model.LatLng;

import android.location.Location;

// Data structure for storing alarm entries
public class Alarm {
	int id;							// Id (assigned)
	
	boolean isOn = false;			// If it is currently active (set by the user)
	
	boolean isDismissed = false;	// In case it was in effect, alarmed, and user dismissed it
									// this is used to make sure it won't get triggered again
	
	String name;					// The name of the alarm (set by the user)
	
	double radius;					// The radius in meters
	
	String alarm_tone;				// (Currently unused)
	
	boolean[] days;					// Bool array of the 7 days
									// The alarm only listens if the current weekday's
									// index is true
	
	LatLng dest;					// The destination
	
	// The start and end times. The alarm only listens in between the times
	// specified here
	int startTimeHour, startTimeMin, endTimeHour, endTimeMin;
	
	
	public Alarm(){
		radius = 1000;
		dest = new LatLng(34.066438, -118.167232);
		days = new boolean[7];
		for(int i = 0; i < 7; i++){
			days[i] = true;
		}
	}
	
	// Used for displaying the days in the UI to convert which index corresponds to which day
	public String getDaysText(){
		String ret = "";
		if(days[0]) { ret += "M "; }
		if(days[1]) { ret += "Tu "; }
		if(days[2]) { ret += "W "; }
		if(days[3]) { ret += "Th "; }
		if(days[4]) { ret += "F "; }
		if(days[5]) { ret += "Sa "; }
		if(days[6]) { ret += "Su "; }
		return ret;
	}
	
	// Used for displaying the times. Internally time is stored in 24 hours
	// This converts time to AM/PM format, which is used both at display
	// and at entry
	public String getTimeText(boolean isStart){
		int Hours, Mins;
		boolean isPM = false;
		String sH, sM, ret;
		if(isStart){ Hours = startTimeHour; Mins = startTimeMin; }
		else{ Hours = endTimeHour; Mins = endTimeMin; }
		// AM/PM 
		if(Hours > 12){
			isPM = true;
			Hours -= 12;
		}
		if(isPM) { ret = "PM"; } else{ ret = "AM"; }
		sH = String.valueOf(Hours);
		//Leading Zeroes
		if(Hours < 10){
			sH = "0" + sH;
		}
		sM  =String.valueOf(Mins);
		if(Mins < 10){
			sM = "0" + sM;
		}
		
		return sH+":"+sM+" "+ret;
	}
	
	// Checks if the commute is currently in progress
	// Used by the alarm service
	public boolean isInProgress(){
		boolean ret = false;
		if(isOn && !isDismissed){
			Calendar c = Calendar.getInstance();
			int currHour = c.get(Calendar.HOUR_OF_DAY);
			int currMin = c.get(Calendar.MINUTE);
			int currDay = (c.get(Calendar.DAY_OF_WEEK)-1); 
			if(currDay < 0) {currDay += 7;}
			if( days[currDay] &&
				currHour >= startTimeHour &&
				currMin >= startTimeMin &&
				currHour <= endTimeHour &&
				currMin <= endTimeMin){
				ret = true;
			}
		}
		return ret;
	}
	
	// Checks if user is in the trigger radius
	// Used by the alarm service
	public boolean isInRadius(Location currLoc){
		boolean ret = false;
		float[] distance = new float[2];
		Location.distanceBetween(currLoc.getLatitude(), currLoc.getLongitude(), dest.latitude, dest.longitude, distance);
		if(distance[0] <= radius){
			ret = true;
		}
		return ret;
	}
}