package com.letsdoitworld.wastemapper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.text.TextUtils;

public class SharedPrefsHelper {

	private static final String SHARED_PREFS = "com.letsdoitworld.wastemapper"; 
	private static final String PREF_SAVED_LONGITUDE = "com.letsdoitworld.wastemapper.PREF_SAVED_LONGITUDE";
	private static final String PREF_SAVED_LATITUDE = "com.letsdoitworld.wastemapper..PREF_SAVED_LATITUDE";
	private static final String PREF_SESSION_NAME = "com.letsdoitworld.wastemapper.PREF_SESSION_NAME";
	private static final String PREF_SESSION_ID = "com.letsdoitworld.wastemapper.PREF_SESSION_ID";
	private static final String PREF_API_URL = "com.letsdoitworld.wastemapper.PREF_API_URL";
	
	private static final String PREF_SP_BBOX_VALUE = "com.letsdoitworld.wastemapper.PREF_SP_BBOX_VALUE";
	public static final String SP_LANGUAGE = "language";
	public static final String SP_MAX_RESULTS_VALUE = "MaxResultsValue";
	
	public static final String SHARED_USERNAME = "username";
	public static final String SHARED_PASSWORD = "password";
	public static final String SHARED_UPTIME = "upTime";
	private static final String DEFAULT_BBOX_VALUE = "";
	public static final int DEFAULT_MAX_RESULTS_VALUE = 500;
	public static final String DEFAULT_LANGUAGE = "en";
	
	//Settings keys
	public static final String KEY_MAP = "settings_map";
	public static final String KEY_DEPTH = "settings_depth";
	public static final String KEY_DISTANCE = "settings_distance";
	public static final String KEY_COORDINATES = "settings_coordinates";
	public static final String KEY_SPEED = "settings_speed";
	public static final String KEY_TEMPERATURE = "settings_temperature";
	public static final String KEY_FUEL_CONSUMPTION = "settings_fuel_cons";
	public static final String KEY_ANCHOR_DRIFT = "settings_anchor_drift";
	public static final String KEY_VERSION = "settings_version";
	public static final String KEY_LEGAL = "settings_legal";
	public static final String KEY_MAIL = "settings_email";
	public static final String KEY_CONTROL_BATTERY = "settings_gps_track_power";

	public static SharedPreferences getSharedPrefs(final Context context) {
		return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
	}

	public static void saveSession(final Context context, final String session_name, final String session_id) {
		if(AppConstants.DEBUG) System.out.println("session name:"+session_name);
		if(AppConstants.DEBUG) System.out.println("session name:"+session_id);
		final Editor editor = getSharedPrefs(context).edit();
		editor.putString(PREF_SESSION_NAME, session_name);
		editor.putString(PREF_SESSION_ID, session_id);
		editor.commit();
	}
	
	public static void emptySession(final Context context) {
		
		final Editor editor = getSharedPrefs(context).edit();
		editor.remove(PREF_SESSION_NAME);
		editor.remove(PREF_SESSION_ID);
		editor.commit();
	}
	
	public static boolean isSession(final Context context) {
		final SharedPreferences prefs = getSharedPrefs(context);
		String session = "";
		if (prefs.contains(PREF_SESSION_NAME)) {
			session = prefs.getString(PREF_SESSION_NAME, "");
		}
		return !TextUtils.isEmpty(session);
	}
	
	public static boolean isLoggedIn(final Context context) {
		final SharedPreferences prefs = getSharedPrefs(context);
		if (prefs.contains(SHARED_USERNAME) && prefs.contains(SHARED_PASSWORD)) {
			return true;
		}
		return false;
	}
	
	public static boolean hasUptime(final Context context) {
		final SharedPreferences prefs = getSharedPrefs(context);
		if (prefs.contains(SHARED_UPTIME)) {
			return true;
		}
		return false;
	}
	
	public static void saveUptime(final Context context, final int value) {
		
		final Editor editor = getSharedPrefs(context).edit();
		editor.putInt(SHARED_UPTIME, value);
		editor.commit();
	}
	
	public static int getUptime(final Context context) {
		return getSharedPrefs(context).getInt(SHARED_UPTIME, 0);
	}
	
	public static void saveBBOX(final Context context, final String value) {
		
		final Editor editor = getSharedPrefs(context).edit();
		editor.putString(PREF_SP_BBOX_VALUE, value);
		editor.commit();
	}
	
	public static String getBBOX(final Context context) {
		return getSharedPrefs(context).getString(PREF_SP_BBOX_VALUE, DEFAULT_BBOX_VALUE);
	}
	
	public static void saveMaxResult(final Context context, final int value) {
		
		final Editor editor = getSharedPrefs(context).edit();
		editor.putInt(PREF_SP_BBOX_VALUE, value);
		editor.commit();
	}
	
	public static int getMaxResult(final Context context) {
		return getSharedPrefs(context).getInt(SP_MAX_RESULTS_VALUE, DEFAULT_MAX_RESULTS_VALUE);
	}
	
	
	public static void saveLanguage(final Context context, final String value) {
		final Editor editor = getSharedPrefs(context).edit();
		editor.putString(SP_LANGUAGE, value);
		editor.commit();
	}
	
	public static String getLanguage(final Context context) {
		return getSharedPrefs(context).getString(SP_LANGUAGE, DEFAULT_LANGUAGE);
	}
	public static void saveUserPass(Context context, String user, String pass) {
		
	    SharedPreferences.Editor editor = getSharedPrefs(context).edit();
	    editor.putString(SHARED_USERNAME, user);
	    editor.putString(SHARED_PASSWORD, pass);
	    editor.commit();
	}
	
	public static void setUpTime(Context context, int uptime){
		 SharedPreferences.Editor editor = getSharedPrefs(context).edit();
		 editor.putInt(SHARED_UPTIME, uptime);
		 editor.commit();
	}
	
	public static void emptyUserPass(Context context) {
		
	    SharedPreferences.Editor editor = getSharedPrefs(context).edit();
	    editor.remove(SHARED_USERNAME);
	    editor.remove(SHARED_PASSWORD);
	    editor.remove(SHARED_UPTIME);
	    editor.commit();
	 }
	
	public static String getUsername(Context context) {
		return getSharedPrefs(context).getString(SHARED_USERNAME, "");
	}
	
	public static String getPassword(Context context) {
		return getSharedPrefs(context).getString(SHARED_PASSWORD, "");
	}
	
	public static void saveLastKnowLocation(final Context context, final Location location) {
		
		final Editor editor = getSharedPrefs(context).edit();
		editor.putFloat(PREF_SAVED_LATITUDE, (float)(location.getLatitude()));
		editor.putFloat(PREF_SAVED_LONGITUDE, (float)(location.getLongitude()));
		editor.commit();
	}
	
	public static Location loadLastKnowLocation(final Context context) {
		final SharedPreferences prefs = getSharedPrefs(context);
		
		Location location = new Location("network");
		;
		
		if (prefs.contains(PREF_SAVED_LATITUDE)) {
			location.setLatitude((double) prefs.getFloat(PREF_SAVED_LATITUDE, 0));
		}
		
		if (prefs.contains(PREF_SAVED_LONGITUDE)) {
			location.setLongitude((double) prefs.getFloat(PREF_SAVED_LONGITUDE, 0));
		}
		
		return location;
	}

	public static boolean hasSavedLocation(Context context) {
		return getSharedPrefs(context).getFloat(PREF_SAVED_LATITUDE, 0) != 0;
	}

	public static String getSessionName(Context mContext) {
		return getSharedPrefs(mContext).getString(PREF_SESSION_NAME, "");
	}
	
	public static String getSessionId(Context mContext) {
		return getSharedPrefs(mContext).getString(PREF_SESSION_ID, "");
	}

	public static String getAPI_URL(Context mContext) {
		return getSharedPrefs(mContext).getString(PREF_API_URL, "");
	}
	
	public static void saveAPI_URL(Context context, String api) {
		 SharedPreferences.Editor editor = getSharedPrefs(context).edit();
		    editor.putString(PREF_API_URL, api);
		    editor.commit();
	}

}
