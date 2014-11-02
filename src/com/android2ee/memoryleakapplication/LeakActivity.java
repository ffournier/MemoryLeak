package com.android2ee.memoryleakapplication;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Activity who create the leak memory
 * @author florian
 *
 */
public class LeakActivity extends Activity {

	private static Drawable sBackground;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView label = new TextView(this);
		label.setText(getString(R.string.leak_bad));
		  
		// do a leak that's detect by MAT
		if (sBackground == null) {
		    sBackground = getResources().getDrawable(R.drawable.bitmap);
		}
		label.setBackgroundDrawable(sBackground);

		setContentView(label);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		
		// do a leak that's no detect by MAT
		myThread thread = new myThread();
		thread.start();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	
	/**
	 * Thread to create a leak
	 * @author florian
	 *
	 */
	private class myThread extends Thread {

		@Override
		public void run() {
			super.run();
			
			while (true) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
