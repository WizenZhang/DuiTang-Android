package com.example.duitang.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreference·â×°
 * @author Wizen
 *
 */
public class PrefUtils {
	public static final String PREF_NAME = "config";
	
	public static String getString(Context ctx, String key, String defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

	public static void setString(Context ctx, String key, String value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
}
