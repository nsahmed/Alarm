package com.csula.commuteralarm;

import java.util.List;

import com.csula.commuteralarm.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlarmArrayAdapter extends ArrayAdapter<Alarm> {
	private final Context context;
	private final List<Alarm> values;

	public AlarmArrayAdapter(Context context, List<Alarm> values) {
		super(context, R.layout.list_item, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_item, parent, false);
		
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		TextView subText = (TextView) rowView.findViewById(R.id.ca_home_subtext);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.activity_image);
		
		// Alarm name main text
		String name = values.get(position).name;
		// Active days subtext
		String days = values.get(position).getDaysText();
		// Start-End time subtext
		String time = values.get(position).getTimeText(true) + " - " + values.get(position).getTimeText(false);
		textView.setText(name);
		subText.setText(days + " | " + time);
		// Set the image according to activity state
		imageView.setImageResource(R.drawable.iconoff);
		if(values.get(position).isOn){
			imageView.setImageResource(R.drawable.iconon);
		}

		return rowView;
	}
} 
