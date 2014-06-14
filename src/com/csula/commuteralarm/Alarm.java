package com.csula.commuteralarm;

import java.util.Calendar;

import com.google.android.gms.maps.model.LatLng;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Alarm implements Parcelable{
	int id;
	boolean isOn = false;
	boolean isDismissed = false;
	String name;
	double radius;			// This shouldn't be int (or should mean feet)
	boolean[] days;	    // This needs to be a bool array
	long time;			// This needs to be a start and end time
	//Date starttime;
	int startTimeHour, startTimeMin, endTimeHour, endTimeMin;
	LatLng dest;
	
	public Alarm(){
		days = new boolean[7];
		for(int i = 0; i < 7; i++){
			days[i] = true;
		}
	}
	
	public Alarm(Parcel p) {
		id = p.readInt();
		isOn = p.readInt() == 1;
		isDismissed = p.readInt() == 1;
		name = p.readString();
		radius = p.readInt();
		p.readBooleanArray(days);
		time = p.readLong();
		startTimeHour = p.readInt();
		startTimeMin = p.readInt();
		endTimeHour = p.readInt();
		endTimeMin = p.readInt();
		dest = p.readParcelable(null);
	}
	
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
		sM = String.valueOf(Mins);
		if(Mins < 10){
			sM = "0" + sM;
		}
		
		return sH+":"+sM+" "+ret;
	}
	
	public void SaveAlarm(){
		
		
	}
	
	// checks if the commute is currently in progress
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
	
	public boolean isInRadius(Location currLoc){
		boolean ret = false;
		float[] distance = new float[2];
		Location.distanceBetween(currLoc.getLatitude(), currLoc.getLongitude(), dest.latitude, dest.longitude, distance);
		if(distance[0] <= radius){
			ret = true;
		}
		return ret;
	}
	
	public void LoadAlarm(int ID){
		//JSONArray jArr = new JSONArray(data);
		//JSONObject jObj = jArr.getJSONObject(ID);
		//this.name = jObj.getString("name");
		/*for(int i = 0; i < jArr.length(); i++){
			JSONObject obj = jArr.getJSONObject(i);
			
		}*/
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel par, int flags) {
		par.writeInt(id);
		par.writeInt(isOn ? 1 : 0);
		par.writeInt(isDismissed ? 1 : 0);
		par.writeString(name);
		par.writeDouble(radius);
		par.writeBooleanArray(days);
		par.writeLong(time);
		par.writeInt(startTimeHour);
		par.writeInt(startTimeMin);
		par.writeInt(endTimeHour);
		par.writeInt(endTimeMin);
		par.writeParcelable(dest, 0);
	}

}
/*
JSON Data format
[
{
    "id": 0,
    "isActive" : 0,
    "Name": "MyAlarm1",
    "rad": 2.1,
    "days": [
        0,
        0,
        1,
        1,
        1,
        0,
        0
    ],
    "tstart": 5,
    "tend": 6
},
{
    "id": 0,
    "isActive" : 1,
    "Name": "MyAlarm2",
    "rad": 3,
    "days": [
        0,
        1,
        1,
        0,
        1,
        0,
        0
    ],
    "tstart": 3,
    "tend": 7
}
]
 */