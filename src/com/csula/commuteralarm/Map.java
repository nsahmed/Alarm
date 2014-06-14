package com.csula.commuteralarm;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.SupportMapFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Map extends FragmentActivity implements OnSeekBarChangeListener,
OnMarkerDragListener, OnMapLongClickListener {
	
	TextView TV_Map;	// Instructions text
	Button BTN_Map;		// Select button
	
	// Default location if no destination was specified yet
	private static final LatLng DEFAULT_LOC = new LatLng(34.066438, -118.167232);
	
	// Default radius assigned on adding new destination
	private static final double DEFAULT_RADIUS = 1000;
	public static final double RADIUS_OF_EARTH_METERS = 6371009;
	
	private GoogleMap mMap;
	private int mStrokeColor;
	private int mFillColor;
	private DraggableCircle mCircle;
	
    private class DraggableCircle {
        private final Marker centerMarker;
        private final Marker radiusMarker;
        public final Circle circle;
        public double radius;
        public DraggableCircle(LatLng center, double radius) {
            this.radius = radius;
            centerMarker = mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));
            radiusMarker = mMap.addMarker(new MarkerOptions()
                    .position(toRadiusLatLng(center, radius))
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE)));
            circle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(5)
                    .strokeColor(mStrokeColor)
                    .fillColor(mFillColor));
        }
        public DraggableCircle(LatLng center, LatLng radiusLatLng) {
            this.radius = toRadiusMeters(center, radiusLatLng);
            centerMarker = mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));
            radiusMarker = mMap.addMarker(new MarkerOptions()
                    .position(radiusLatLng)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE)));
            circle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeColor(mStrokeColor)
                    .fillColor(mFillColor));
        }
        public boolean onMarkerMoved(Marker marker) {
            if (marker.equals(centerMarker)) {
                circle.setCenter(marker.getPosition());
                radiusMarker.setPosition(toRadiusLatLng(marker.getPosition(), radius));
                return true;
            }
            if (marker.equals(radiusMarker)) {
                 radius = toRadiusMeters(centerMarker.getPosition(), radiusMarker.getPosition());
                 circle.setRadius(radius);
                 TV_Map.setText("Adjust trigger radius ("+String.format("%1$,.1f", radius)+" m)");
                 return true;
            }
            return false;
        }
        public void onStyleChange() {
            circle.setFillColor(mFillColor);
            circle.setStrokeColor(mStrokeColor);
        }
    }
    
    /** Generate LatLng of radius marker */
    private static LatLng toRadiusLatLng(LatLng center, double radius) {
        double radiusAngle = Math.toDegrees(radius / RADIUS_OF_EARTH_METERS) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    private static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);
        return result[0];
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        TV_Map = (TextView)findViewById(R.id.tv_map);
        TV_Map.setText("Select Destination");
        BTN_Map = (Button)findViewById(R.id.btn_map_ok);
        
        BTN_Map.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// If a location isn't selected yet
				if(mCircle == null){
					Toast.makeText(Map.this,  "Select a destination first", Toast.LENGTH_SHORT).show();
				}
				else if(mCircle.radius < 100){
					Toast.makeText(Map.this,  "Radius too small", Toast.LENGTH_SHORT).show();
				}
				else{
					HomeActivity.currentAlarm.radius = mCircle.radius;
					HomeActivity.currentAlarm.dest = mCircle.circle.getCenter();
					Intent i = new Intent();
					i.setClass(Map.this, ConfigureAlarm.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			}});
        
        setUpMapIfNeeded();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);

        mFillColor = Color.argb(96, 255, 255, 0);
        mStrokeColor = Color.BLACK;

        // No destination and circle to draw if the alarm was just created
        if(HomeActivity.isNew){
        	mCircle = null;
        	// Move the map so that it is centered on the initial circle
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOC, 13.5f));
        }
        else if(mCircle == null){
        	// create it
	        DraggableCircle circle = new DraggableCircle(HomeActivity.currentAlarm.dest, HomeActivity.currentAlarm.radius);
	        mCircle = circle;
	        TV_Map.setText("Adjust trigger radius ("+String.format("%1$,.1f", circle.radius)+" m)");
	        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HomeActivity.currentAlarm.dest, 13.5f));
        }
        
        // set mylocation
        mMap.setMyLocationEnabled(true);
    }

	@Override
	public void onMapLongClick(LatLng point) {  
		if(mCircle == null){
	        // create it
	        DraggableCircle circle = new DraggableCircle(point, DEFAULT_RADIUS);
	        mCircle = circle;
	        TV_Map.setText("Adjust trigger radius ("+String.valueOf(circle.radius)+" m)");
		}
		// if it was already created move (disabled for now)
		else{
			//mCircle.circle.setCenter(point);
		}
	}
	
	// Update circle and markers
	@Override
	public void onMarkerDrag(Marker marker) {
		onMarkerMoved(marker);
	}

	// Update circle and markers
	@Override
	public void onMarkerDragEnd(Marker marker) {
		onMarkerMoved(marker);
	}

	// Update circle and markers
	@Override
	public void onMarkerDragStart(Marker marker) {
		onMarkerMoved(marker);
	}
	
	// Update circle and markers
	private void onMarkerMoved(Marker marker) {
		mCircle.onMarkerMoved(marker);
    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// no action
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// no action
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// no action
		
	}
}