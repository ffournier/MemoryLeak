package com.android2ee.memoryleakapplication;

import java.io.File;
import java.io.IOException;

import android.os.Debug;
import android.util.Log;

/**
 * Save the dump file in the given path
 * @author florian
 *
 */
public class HeapDumpingUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
	private static final String HPROF_DUMP_BASENAME = "Android2ee.dalvik-hprof";
	private final String dataDir;
 
	public HeapDumpingUncaughtExceptionHandler(String dataDir) {
		this.dataDir = dataDir;
	}
	 
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.w(getClass().getSimpleName(), "uncaughtException :" + dataDir);
		String absPath = new File(dataDir, HPROF_DUMP_BASENAME).getAbsolutePath();
		// Dump the memory when we have an OutOfMemoryError
		if(ex.getClass().equals(OutOfMemoryError.class)) {
			try {
				// Dump "hprof" data to the specified file. This may cause a GC.
				Debug.dumpHprofData(absPath);
			} catch (IOException e) {
				Log.e(getClass().getSimpleName(), e.getMessage());
			}
		}
	}
}