package com.csula.commuteralarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

public class ConfigureAlarm extends Activity  {
	
	Alarm alarm;	// The currently selected alarm under configuration
	
	// -- Textviews, checkboxes and linearlayouts for the configuration entries --
	TextView TV_AlarmName;
	LinearLayout LL_AlarmName;
	TextView TV_AlarmLoc;
	LinearLayout LL_AlarmLoc;
	TextView TV_Radius;
	LinearLayout LL_Radius;
	TextView TV_Days;
	LinearLayout LL_Days;
	TextView TV_TStart;
	LinearLayout LL_TStart;
	TextView TV_TEnd;
	LinearLayout LL_TEnd;
	LinearLayout LL_IsActive;
	CheckBox CB_IsActive;
	private String m_Text = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// HACK -- this needs to be done clean
		alarm = HomeActivity.currentAlarm;
		
		setContentView(R.layout.config_alarm);
		
		// -- Entry to show/edit the name of the alarm --
		TV_AlarmName = (TextView)findViewById(R.id.ca_val_alarmname);
		TV_AlarmName.setText(alarm.name);
		LL_AlarmName = (LinearLayout)findViewById(R.id.ca_ll_alarmname);
		
		// -- Entry showing the coordinates of the destination or editing (go to map) --
		TV_AlarmLoc = (TextView)findViewById(R.id.ca_val_alarmloc);
		TV_AlarmLoc.setText("Long: |  Lat:  ");
		TV_AlarmLoc.setText("Long: " + String.format("%1$,.4f", alarm.dest.longitude)+
				" |  Lat: " + String.format("%1$,.4f", alarm.dest.latitude));
		LL_AlarmLoc = (LinearLayout)findViewById(R.id.ca_ll_alarmloc);
		
		// -- The configured radius. Editable with string entry without going to map --
		TV_Radius = (TextView)findViewById(R.id.ca_val_radius);
		TV_Radius.setText(String.format("%1$,.0f", alarm.radius)+" m");
		LL_Radius = (LinearLayout)findViewById(R.id.ca_ll_radius);
		
		// -- Shows the days of the week on which the alarm is active, editable --
		TV_Days = (TextView)findViewById(R.id.ca_val_days);
		TV_Days.setText(alarm.getDaysText());
		LL_Days = (LinearLayout)findViewById(R.id.ca_ll_daylist);
		
		// -- Shows the start time, editable --
		TV_TStart = (TextView)findViewById(R.id.ca_val_starttime);
		TV_TStart.setText(alarm.getTimeText(true));
		LL_TStart = (LinearLayout)findViewById(R.id.ca_ll_starttime);
		
		// -- Shows the end time, editable --
		TV_TEnd = (TextView)findViewById(R.id.ca_val_endtime);
		TV_TEnd.setText(alarm.getTimeText(false));
		LL_TEnd = (LinearLayout)findViewById(R.id.ca_ll_endtime);
		
		// -- Whether the alarm is active --
		CB_IsActive = (CheckBox)findViewById(R.id.ca_chk_isactive);
		CB_IsActive.setChecked(alarm.isOn);
		CB_IsActive.setClickable(false);
		LL_IsActive = (LinearLayout)findViewById(R.id.ca_ll_isactive);
		
		// Set listener to the above views
		LL_AlarmName.setOnClickListener(ON_LL_AlarmName);
		LL_Radius.setOnClickListener(ON_LL_Radius);
		LL_Days.setOnClickListener(ON_LL_Days);
		LL_TStart.setOnClickListener(ON_LL_TStart);
		LL_TEnd.setOnClickListener(ON_LL_TEnd);
		LL_AlarmLoc.setOnClickListener(ON_LL_AlarmLoc);
		LL_IsActive.setOnClickListener(ON_LL_IsActive);
		
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	////////////////////////////
	// --- Assign listeners ---
	///////////////////////////
	
	// ---------------------------------
	// --- String input for Alarm Name ---
	// ---------------------------------
	public View.OnClickListener ON_LL_AlarmName = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(ConfigureAlarm.this);
			builder.setTitle("Enter Alarm Name");

			// Set up the input
			final EditText input = new EditText(ConfigureAlarm.this);
			input.setText(alarm.name);
			// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
			input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
			builder.setView(input);

			// Set up the buttons
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        m_Text = input.getText().toString();
			        alarm.name = m_Text;
			        TV_AlarmName.setText(m_Text);
			    }
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        dialog.cancel();
			    }
			});
			builder.show();
		}
	};
	
	// ---------------------------------
	// --- Listener on radius, string entry ---
	// ---------------------------------
	public View.OnClickListener ON_LL_Radius = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(ConfigureAlarm.this);
			builder.setTitle("Enter Alarm Radius");

			// Set up the input
			final EditText input = new EditText(ConfigureAlarm.this);
			
			input.setText(String.valueOf(alarm.radius));
			// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
			input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
			builder.setView(input);

			// Set up the buttons
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        m_Text = input.getText().toString();
			        alarm.radius = Double.parseDouble(m_Text);
			        //alarm.name = m_Text;
			        TV_Radius.setText(m_Text + " m");
			    }
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        dialog.cancel();
			    }
			});
			builder.show();
		}
	};
	// ---------------------------------
	// --- Listener on days, provides checkbox group ---
	// ---------------------------------
	public View.OnClickListener ON_LL_Days = new View.OnClickListener() {
		final CharSequence[] items = {"Monday ","Tuesday ","Wednesday ","Thursday ", "Friday ", "Saturday ", "Sunday "};
		@Override
		public void onClick(View v) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(ConfigureAlarm.this);
			builder.setTitle("Select Commute Days");
			
			builder.setMultiChoiceItems(items, alarm.days,
                    new DialogInterface.OnMultiChoiceClickListener() {
             // indexSelected contains the index of item (of which checkbox checked)
             @Override
             public void onClick(DialogInterface dialog, int indexSelected,
                     boolean isChecked) {
                 if (isChecked) {
                     // If the user checked the item, add it to the selected items
                     // write your code when user checked the checkbox 
                     
                	 //selectedItems.add(indexSelected);
                	 alarm.days[indexSelected] = true;
                	 Log.w("", "true");
                 } else {
                     // Else, if the item is already in the array, remove it 
                     // write your code when user Uchecked the checkbox 
                     
                	 alarm.days[indexSelected] = false;
                	 Log.w("", "false");
                	 //selectedItems.remove(Integer.valueOf(indexSelected));
                 }
             }
			});
			
			// Set up the buttons
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
						    @Override
						    public void onClick(DialogInterface dialog, int which) {
						        //m_Text = input.getText().toString();
						        //alarm.radius = Integer.parseInt(m_Text);
						        //alarm.name = m_Text;
						        //TV_Radius.setText(m_Text + " feet");
						    	TV_Days.setText(alarm.getDaysText());
						    }
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						    @Override
						    public void onClick(DialogInterface dialog, int which) {
						        dialog.cancel();
						    }
						});
			builder.show();
		}
	};
	// ---------------------------------
	// --- Listener on start time entry ---
	// ---------------------------------
	public View.OnClickListener ON_LL_TStart = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
            
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(ConfigureAlarm.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                	//if(selectedHour > 12){ ampm = "PM"; selectedHour -= 12; }
                	alarm.startTimeHour = selectedHour;
                	alarm.startTimeMin = selectedMinute;
                	TV_TStart.setText( alarm.getTimeText(true));
                	
                }
            }, alarm.startTimeHour, alarm.startTimeMin, false);
            mTimePicker.setTitle("Select Commute Start Time");
            mTimePicker.show();
		}
	};
	
	// ---------------------------------
	// -- Listener on end time entry ---
	// ---------------------------------
	public View.OnClickListener ON_LL_TEnd = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(ConfigureAlarm.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                	//if(selectedHour > 12){ ampm = "PM"; selectedHour -= 12; }
                	alarm.endTimeHour = selectedHour;
                	alarm.endTimeMin = selectedMinute;
                	TV_TEnd.setText( alarm.getTimeText(false));
                	
                }
            }, alarm.endTimeHour, alarm.endTimeMin, false);
            mTimePicker.setTitle("Select Commute End Time");
            mTimePicker.show();
		}
	};
	// ---------------------------------
	// --- Listener on location entry (takes back to map) ---
	// ---------------------------------
	public View.OnClickListener ON_LL_AlarmLoc = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent();
			HomeActivity.isNew = false;
			i.setClass(ConfigureAlarm.this, Map.class);
			startActivity(i);
		}
	};
	// ---------------------------------
	// --- Listener on activity checkbox ---
	// ---------------------------------
	public View.OnClickListener ON_LL_IsActive = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			CB_IsActive.toggle();
			alarm.isOn = !alarm.isOn;
		}
	};
	
}
