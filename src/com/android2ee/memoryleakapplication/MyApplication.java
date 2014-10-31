package com.android2ee.memoryleakapplication;

import android.app.Application;

/**
 * 
 * @author florian
 *
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		/**
		 * Full path to a directory assigned to the package for its persistent data. 
		 * catch any error in the current Thread and dump the memory if an outofmemoryerror 
		 * can save the dump file in other path, as sdcard
		 */
		 Thread.currentThread().setUncaughtExceptionHandler(
				 new HeapDumpingUncaughtExceptionHandler(getApplicationInfo().dataDir));
	}

}
