package com.letsdoitworld.wastemapper.utils;

import android.content.Context;

import com.letsdoitworld.wastemapper.R;


public class AppConstants {

	public static final boolean DEBUG = true;
	public static final String URL_BASIC_ADDRESS = "http://api.letsdoitworld.org/?q=get-api-base-url.json";
	public static final String API_BASE_URL_KEY = "api_base_url";
	public static final String SAVE_BOX = "safe_bbox";
	public static final String SHARED_PREFS = "com.letsdoitworld.wastemapper.shared.preferences";
	public static final String REGISTER_ACCOUNT = "https://www.letsdoitworld.org/user/register";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String FB_UID = "fb_uid";
	public static final String FB_ACCESS_TOCKEN = "access_token";
	 
	public static final String SP_DISTANCES = "distances";
	public static final String SP_LOCATION = "location";
	public static final String SP_POINTS_ARRAY = "pointsArray";
	public static final String SP_POINTS_LATITUDE = "pointsLatitude";
	public static final String SP_POINTS_LONGITUDE = "pointsLongitude";
	public static final String SP_POINTS_IDS = "pointsIds";
	public static final String SP_POINT_ID = "pointId_";
	public static final String SP_JSON_ARRAY_EXTRA_FIELDS = "JSONArrayExtraFields";
	public static final String SP_JSON_LOGIN = "JSONLogin";
	public static final String SP_MODE = "AppMode";
	public static final String SP_NEAREST_FEATURE = "isNearestTo";
	public static final String SP_USERNAME = "loggedUsername";
	public static final String SP_JSON_SESSION_NAME = "session_name";
	public static final String SP_JSON_SESSION_ID = "sessid";
	public static final String SP_JSON_WP = "wp";
	
	public static final String ADDRESS_JSON_EXTRA_FIELDS = "/waste-point-extra-fields.json";
	public static final String ADDRESS_JSON_LOGIN = "/user/login.json";
	public static final String ADDRESS_WASTE_POINT_MAXRES = "/waste_points.csv&max_results=";
	public static final String ADDRESS_WASTE_POINTS_BBOX = "&BBOX=";
	public static final String ADDRESS_WASTE_POINTS_NEAREST = "&nearest_points_to=";
	public static final String ADDRESS_LANGUAGE = "&language_code=";
	
	public static final String ADDRESS_PUT_WASTE_POINT = "/wp.json";
	public static final String ADDRESS_WASTE_POINT_DETAILS = "/wp/";
	public static final String ADDRESS_MODIFY_CONFIRM_EXTENSION_QM = ".json?_method=PUT";
	public static final String ADDRESS_MODIFY_CONFIRM_EXTENSION_AND = ".json&_method=PUT";
	
	public static final String TYPE_TEXT = "text";
	public static final String TYPE_FLOAT = "float";
	public static final String TYPE_INTEGER = "integer";
	public static final String TYPE_BOOLEAN = "boolean";
	
	public static final String ALLOWED_VALUES = "allowed_values";
	public static final String TYPICAL_VALUES = "typical_values";
	public static final String FIELD_NAME = "field_name";
	public static final String TYPE = "type";
	public static final String LABEL = "label";
	public static final String SUFFIX = "suffix";
	
	public static final String ERROR_GENERIC = "ERROR_GENERIC";
	public static final String ERROR_NO_GPS_LOCATION_YET = "ERROR_NO_GPS_LOCATION_YET";
	public static final String ERROR_NO_LOCATION_PROVIDER = "ERROR_NO_LOCATION_PROVIDER";
	public static final String ERROR_LOCATION_TIMEOUT = "ERROR_NO_LOCATION_PROVIDER";
	public static final String ERROR_NO_CONNECTION = "ERROR_NO_CONNECTION";
	public static final String ERROR_LOGIN_FAILED = "ERROR_LOGIN_FAILED";
	public static final String ERROR_EMPTY_LOGIN_PASSWORD = "ERROR_EMPTY_LOGIN_PASSWORD";
	public static final String ACTION_ADD_WASTE_POINT = "ACTION_ADD_WASTE_POINT";
	public static final String ACTION_CONFIRM_WASTE_POINT = "ACTION_CONFIRM_WASTE_POINT";
	
	
	public static String getErrorMessage(Context context, String ERROR){
		
		if(ERROR.equals(ERROR_NO_GPS_LOCATION_YET)){
			return context.getString(R.string.message_wait_for_location);
		}
		if(ERROR.equals(ERROR_NO_CONNECTION)){
			return context.getString(R.string.text_no_connection);
		}
		if(ERROR.equals(ERROR_LOGIN_FAILED)){
			return context.getString(R.string.toast_login_fail);
		}
		if(ERROR.equals(ERROR_EMPTY_LOGIN_PASSWORD)){
			return context.getString(R.string.ERROR_EMPTY_LOGIN_PASSWORD);
		}
		
		return context.getString(R.string.ERROR_GENERIC);
		
	}
	
	

}
