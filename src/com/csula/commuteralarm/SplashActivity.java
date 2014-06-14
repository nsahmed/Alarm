package com.csula.commuteralarm;


import com.csula.commuteralarm.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class SplashActivity extends Activity{

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	private ImageView splash;

	/** Called when the activity is first created. */	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		splash = (ImageView)findViewById(R.id.imageView1);

		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};
		splash.setOnTouchListener(gestureListener);
	}

	public void Transition(boolean direction){
    	
    	
    	Intent i2 = new Intent(/*getApplicationContext()*/ SplashActivity.this, HomeActivity.class);
    	startActivity(i2);
    	
    	//Toast.makeText(MainActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
    	if(direction){
    		//Toast.makeText(MainActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
    		overridePendingTransition( R.anim.anim_to_right_in, R.anim.anim_to_right );
    	}
    	else{
    		overridePendingTransition( R.anim.anim_to_left_in, R.anim.anim_to_left);
    	}
    	finish();
    	
    }
	
	class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //Toast.makeText(MainActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                	Transition(false);
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //Toast.makeText(MainActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                	Transition(true);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

            @Override
        public boolean onDown(MotionEvent e) {
              return true;
        }
            
            
            
        
    }

}


