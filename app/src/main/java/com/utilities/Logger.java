package com.utilities;

import android.util.Log;

/**
 * class for view log.
 * 
 */
public class Logger {

	/** method for print log. */
	public static void logger(String msg) {
		if (ApplicationConstatns.isInDebugMode) {
			Log.i("EverSend", msg);
		}
	}

	public static void logger(String TAG, String msg) {
		if (ApplicationConstatns.isInDebugMode) {
			Log.i(TAG, msg);
		}
	}

	public static void errorLog(String message) {
		if (ApplicationConstatns.isInDebugMode) {
			Log.e("EverSend", message);
		}
	}

}