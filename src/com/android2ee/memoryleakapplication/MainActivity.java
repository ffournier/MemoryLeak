package com.android2ee.memoryleakapplication;

import java.text.DecimalFormat;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * @author florian
 *
 */
public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button = (Button) findViewById(R.id.mybutton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				logHeap(MainActivity.this);
			}
		});
	}
	
	/**
	 * log heap by Debug
	 * @param df : the format display
	 */
	private static void logDebugHeap(DecimalFormat df) {
		Log.d("mytag", "Debug. logHeap =================================");
        Double allocated = new Double(Debug.getNativeHeapAllocatedSize())/ 1048576.0;
        Double available = new Double(Debug.getNativeHeapSize())/1048576.0;
        Double free = new Double(Debug.getNativeHeapFreeSize())/1048576.0;
        Log.d("mytag", "Debug.heap native: allocated " + df.format(allocated) + "MB of " + 
        																	df.format(available) + "MB (" + 
        																	df.format(free) + "MB free)");
        
        // can get  the global allocation by function Debug.getGlobal... or binder by Debug.getBinder..., thread by Debug.getThread...
        
        // get memInfo
		android.os.Debug.MemoryInfo memInfoDebug = new Debug.MemoryInfo();
		Debug.getMemoryInfo(memInfoDebug);
		if (memInfoDebug != null) {
			/*Log.w("mytag", "Debug.heap : totalPrivateDirty " + df.format((memInfoDebug.getTotalPrivateDirty()/ 1024.0) + "MB"));
			Log.w("mytag", "Debug.heap : totalPss " + df.format((memInfoDebug.getTotalPss()/ 1024.0) + "MB"));
			Log.w("mytag", "Debug.heap : totalSharedDirty " + df.format((memInfoDebug.getTotalSharedDirty()/ 1024.0) + "MB"));
			int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			if (currentapiVersion >= android.os.Build.VERSION_CODES.KITKAT){
				Log.w("mytag", "Debug.heap : totalSharedClean " + df.format((memInfoDebug.getTotalSharedClean()/ 1024.0) + "MB"));
				Log.w("mytag", "Debug.heap : totalPrivateClean " + df.format((memInfoDebug.getTotalPrivateClean()/ 1024.0) + "MB"));
				Log.w("mytag", "Debug.heap : totalSwappablePss " + df.format((memInfoDebug.getTotalSwappablePss()/ 1024.0) + "MB"));
			}*/
				
		}
	}
	
	/**
	 * log heap by Runtime
	 * @param df : the format display
	 */
	private static void logRuntimeHeap(DecimalFormat df) {
		
		Log.d("mytag", "Runtime. logHeap =================================");
	    Log.d("mytag", "Runtime.Memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory()/1048576)) + "MB of " + 
	        																df.format(new Double(Runtime.getRuntime().maxMemory()/1048576))+ "MB (" + 
	        																df.format(new Double(Runtime.getRuntime().freeMemory()/1048576)) +"MB free)");
	
	}
	
	/**
	 * log heap by the ActivityManager
	 * @param context : Context to have access to the function getSystemService
	 * @param df : the format display
	 */
	private static void logActivityManagerHeap(Context context, DecimalFormat df) {
		
		Log.d("mytag", "ActivityManager. logHeap =================================");
	    ActivityManager actManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		actManager.getMemoryInfo(memInfo);
		if (memInfo != null) {
			Log.w("mytag", "ActivityManager.heap : total " + df.format(memInfo.totalMem / 1048576.0) + "MB of " + 
																				df.format(memInfo.availMem / 1048576.0) + "MB (" +
																				df.format(memInfo.threshold / 1048576.0) + "MB threshold  and " +
																				(memInfo.lowMemory ? "is lowmemory" : "is not lowmemory"));
		}
	}
	
	/**
	 * Log all heap find in API
	 * @param context : Context to have access to the function getSystemService
	 */
	private static void logHeap(Context context) {
        // format log
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);
        df.setMinimumFractionDigits(3);
        
        // debug heap
        logDebugHeap(df);
        
        // Runtime heap
        logRuntimeHeap(df);
        
        // ActivityManagerHeap
        logActivityManagerHeap(context,df);
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		
		// do a leak
		myThread thread = new myThread();
		thread.start();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		// generate an error
		OutOfMemoryError error = new OutOfMemoryError("my generated error");
		throw error;
	}
	
	
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
