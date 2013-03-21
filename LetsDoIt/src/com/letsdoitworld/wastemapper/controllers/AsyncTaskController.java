package com.letsdoitworld.wastemapper.controllers;

import java.util.Locale;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.letsdoitworld.wastemapper.model.BasicInterface;
import com.letsdoitworld.wastemapper.utils.AppConstants;
import com.letsdoitworld.wastemapper.utils.ConnectivityHelper;


public class AsyncTaskController {

	public final static String tag = "AsyncTaskController";
	
	public BasicInterface mBasicActivity;
	public ConnectionController mConnectionController;
	public LocationController mLocationController;
	public WastePointController mWastePointController;
	public MyAsyncHandler handler;

	private MyAsyncTask mAsyncTask;
	public static String TAG =  "Task controller";
	
	
	//CALL CODE TO CONTROLLERS
	public static final int GET_API_URL = 1;
	public static final int LOGIN = 2;
	public static final int LOGOUT = 3;
	public static final int GET_LOCATION = 4;
	public static final int GET_LOCATION_ALL_THE_TIME = 5;
	public static final int GET_WASTE_POINTS = 6;
	public static final int ADD_WASTE_POINT = 7;
	public static final int GET_EXTRA_FIELD = 8;
	public static final int MODIFY_CONFIRM_WASTE_POINT= 9;
	public static final int SEND_OFFLINE_WASTE_POINTS= 10;
	public static final int LOGIN_FACEBOOK = 11;
	
	private int mType = 0;
	
	private boolean mShowDialog = true;
	
	public AsyncTaskController(BasicInterface basicActivity){
		
		mBasicActivity = basicActivity;
		mConnectionController = ConnectionController.getInstance(mBasicActivity.getContext());
		mLocationController = LocationController.getInstance(mBasicActivity);
		mWastePointController = WastePointController.getInstance(mBasicActivity.getContext());
	
	}
	
	public void launchTask(int type, boolean showDialog){
		this.mShowDialog = showDialog;
		this.mType = type;
		mAsyncTask = new MyAsyncTask();
		mAsyncTask.execute();
	}
	
	private class MyAsyncHandler extends Handler{
		@Override  
	    public void handleMessage(Message msg) {
			
	    }  
	}
	
	public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... unused) {

			if(checkConnectivity()){
				
				try{
					switch(mType){
					case GET_API_URL:
						Location location = mLocationController.getCurrentLocation();
						if(location == null) {
							System.out.println("location null");
							mConnectionController.getAPI_URL("", "");
						}
						else {
							System.out.println("location ok async");
							mConnectionController.getAPI_URL(location.getLatitude() + "", location.getLongitude() + "");
						}
						break;
					case LOGIN :
						mConnectionController.login();
						break;
					case LOGOUT :
						break;
					case GET_WASTE_POINTS :
						mWastePointController.getRemoteWastePointsList(mConnectionController.getBaseUrl(), mLocationController.getCurrentLocation());
						break;
					case SEND_OFFLINE_WASTE_POINTS :
						mWastePointController.resendSavedWastePoints(mConnectionController.getBaseUrl().concat(AppConstants.ADDRESS_PUT_WASTE_POINT));
						break;
					case ADD_WASTE_POINT :
						mWastePointController.addRemoteWastePoint(mConnectionController.getBaseUrl().concat(AppConstants.ADDRESS_PUT_WASTE_POINT));
						break;
					case MODIFY_CONFIRM_WASTE_POINT :
						/*
						 * URL: POST <api_base_url>/wp/<waste_point_id>.json?_method=PUT (modify/confirm existing WP, if <api_base_url> does not contain '?')
						   URL: POST <api_base_url>/wp/<waste_point_id>.json&_method=PUT (modify/confirm existing WP, if <api_base_url> contains '?')
						 */
						String extension = AppConstants.ADDRESS_MODIFY_CONFIRM_EXTENSION_QM;
						if(mConnectionController.getBaseUrl().contains("?")) extension = AppConstants.ADDRESS_MODIFY_CONFIRM_EXTENSION_AND;
						mWastePointController.confirmRemoteWastePoint(mConnectionController.getBaseUrl().concat(AppConstants.ADDRESS_WASTE_POINT_DETAILS).concat(mWastePointController.getCurrentWastePoint().getId()).concat(extension));
						break;
					case GET_EXTRA_FIELD :
						mWastePointController.getRemoteExtraFields(mConnectionController.getBaseUrl() + AppConstants.ADDRESS_JSON_EXTRA_FIELDS + AppConstants.ADDRESS_LANGUAGE + Locale.getDefault().getLanguage());
						break;
					case LOGIN_FACEBOOK:
						mConnectionController.loginWithFacebook();
						break;
					} 
					
				} catch (Exception e) {

				}
			}
			
			
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(mShowDialog) mBasicActivity.showDialog();
		}

		@Override
		protected void onPostExecute(Void unused) {
			
			String error = "";
			boolean success = true;
			
			if(checkConnectivity()){
				
				switch(mType){
				
					case GET_API_URL:
						success = mConnectionController.isSuccess();
						error= AppConstants.ERROR_GENERIC;
						break;
					case LOGIN:
						success = mConnectionController.isSuccess();
						error= AppConstants.ERROR_LOGIN_FAILED;
						break;
					case GET_WASTE_POINTS :
					case ADD_WASTE_POINT :
					case MODIFY_CONFIRM_WASTE_POINT :
					case GET_EXTRA_FIELD :
					case SEND_OFFLINE_WASTE_POINTS:
						success = mWastePointController.isSuccess();
						error= AppConstants.ERROR_GENERIC;
						break;
				}
			}else{
				success = false;
				error = AppConstants.ERROR_NO_CONNECTION;
			}
			
			if(success){
				mBasicActivity.handleSuccessTask(mType);
			}
			else{
				mBasicActivity.handleFailedTask(mType, AppConstants.getErrorMessage(mBasicActivity.getContext(), error));
				if (AppConstants.DEBUG) Log.d(TAG, "task number failde" + mType);
			}
		}
		
		/**
		 * Check internet connectivity
		 * @return true if network is connected, else show dialog and return false
		 */
		protected boolean checkConnectivity() {
			
			return ConnectivityHelper.isNetworkConnected(mBasicActivity.getContext());
	    	
		}
	}
	
}
