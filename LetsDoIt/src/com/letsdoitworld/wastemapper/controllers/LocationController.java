package com.letsdoitworld.wastemapper.controllers;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.letsdoitworld.wastemapper.model.BasicInterface;
import com.letsdoitworld.wastemapper.utils.AppConstants;

/** 
 * This class is used to geolocate the user
 * this class is a singleton 
 * @author arthur dibon
 */
public class LocationController  {
	
	private final static String TAG = "LocationFinder";

	private LocationManager locManager;
	private Location currentLocation;

	private static LocationController locationFinder;
	private Context mContext;
	ConnectivityManager mConnectmanager;
	boolean locationFound = false;
	boolean showTimeOut = false;
	private BasicInterface mBasicActivity;
	boolean isSearching = false;
	private boolean stopSearchingLocation = false;
	boolean gps_enabled = false;
	boolean network_enabled = false;
	boolean mShowDialog = false;
	
	private Timer timeoutLocation; 
	
	/**
	 * Constructor of the class
	 * 
	 */
	private LocationController (BasicInterface basicActivity) {
		
		mBasicActivity = basicActivity;
		
		mContext = mBasicActivity.getContext();
		
		if(AppConstants.DEBUG) Log.e("AHHH", "CREATE LOCATOR");
		
		locManager = (LocationManager) mContext.getSystemService( Context.LOCATION_SERVICE );

		timeoutLocation = new Timer();

		//currentLocation = getFirstLocation();
		
		if(currentLocation != null){
			if(AppConstants.DEBUG) Log.e("AHHH", "HAS LOCATION");
		}
	}

	private final LocationListener gpsLocationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
            case LocationProvider.AVAILABLE:
            	if(AppConstants.DEBUG) Log.d(TAG ,  "GPS available again\n");
            	stopSearchingLocation = false;
            	
                break;
            case LocationProvider.OUT_OF_SERVICE:
            	if(AppConstants.DEBUG) Log.d(TAG ,  "GPS out of service\n");
            	mBasicActivity.handleFailedTask(AsyncTaskController.GET_LOCATION,  "GPS out of service");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
            	if(AppConstants.DEBUG) Log.d(TAG , "GPS temporarily unavailable\n");
            	mBasicActivity.handleFailedTask(AsyncTaskController.GET_LOCATION,  "GPS temporarily unavailable");
            	break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
        	if(AppConstants.DEBUG) Log.d(TAG , "GPS Provider Enabled\n");
        	gps_enabled = true;
        	if(mShowDialog){
        		updateMessage();
        	}
        	
        }

		@Override
        public void onProviderDisabled(String provider) {
			if(AppConstants.DEBUG) Log.d(TAG ,  "GPS Provider Disabled\n");
        	gps_enabled = false;
        	if(mShowDialog){
        		updateMessage();
        	}
        }

        @Override
        public void onLocationChanged(Location location) {
        	
        	isSearching = false;
        	if(AppConstants.DEBUG)Log.d( TAG, "AAAAAAAAAAA Location changed from " + location.getProvider());
        	//Toast.makeText(mContext, "LOCATION CHANGED", Toast.LENGTH_LONG).show();

        	currentLocation = location;
            	
        	if(!locationFound){
        		locationFound = true;
    			
    			if(AppConstants.DEBUG)Log.e(TAG, "currentLocation.getLongitude():"+currentLocation.getLongitude());
    			mBasicActivity.handleSuccessTask(AsyncTaskController.GET_LOCATION);
                //locManager.removeUpdates(networkLocationListener);

        	}
        	mBasicActivity.handleSuccessTask(AsyncTaskController.GET_LOCATION_ALL_THE_TIME);
        	
        	if(AppConstants.DEBUG) Log.d(TAG ,  "New Network location: " + String.format("%9.6f", location.getLatitude()) + ", "  + String.format("%9.6f", location.getLongitude()) + "\n");

        }
        
        
    };

    private final LocationListener networkLocationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
            case LocationProvider.AVAILABLE:
            	if(AppConstants.DEBUG)Log.d(TAG ,  "Network location available again\n");
            	stopSearchingLocation = true;
                break;
            case LocationProvider.OUT_OF_SERVICE:
            	if(AppConstants.DEBUG)Log.d(TAG , "Network location out of service\n");
            	mBasicActivity.handleFailedTask(AsyncTaskController.GET_LOCATION,  "Network location out of service");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
            	if(AppConstants.DEBUG)Log.d(TAG ,  "Network location temporarily unavailable\n");
            	mBasicActivity.handleFailedTask(AsyncTaskController.GET_LOCATION,  "Network location temporarily unavailable");
                break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
        	if(AppConstants.DEBUG)Log.d(TAG ,  "Network Provider Enabled\n");
        	network_enabled = true;
        	if(mShowDialog){
        		updateMessage();
        	}
        }

        @Override
        public void onProviderDisabled(String provider) {
        	if(AppConstants.DEBUG)Log.d(TAG , "Network Provider Disabled\n");
        	network_enabled = false;
        	if(mShowDialog){
        		updateMessage();
        	}
        }

        @Override
        public void onLocationChanged(Location location) {
        	isSearching = false;
        	if(AppConstants.DEBUG)Log.d( TAG, "AAAAAAAAAAA Location changed from " + location.getProvider());
        	//Toast.makeText(mContext, "LOCATION CHANGED", Toast.LENGTH_LONG).show();

    		currentLocation = location;
        	
        	if(!locationFound){
        		locationFound = true;
    			
    			mBasicActivity.handleSuccessTask(AsyncTaskController.GET_LOCATION);
                //locManager.removeUpdates(networkLocationListener);

        	}
        	mBasicActivity.handleSuccessTask(AsyncTaskController.GET_LOCATION_ALL_THE_TIME);
        	
        	if(AppConstants.DEBUG) Log.d(TAG ,  "New Network location: " + String.format("%9.6f", location.getLatitude()) + ", "  + String.format("%9.6f", location.getLongitude()) + "\n");

        }
    };
	
	public static LocationController getInstance (BasicInterface basicActivity) {
		if (locationFinder == null) {
			locationFinder =  new LocationController(basicActivity);
		}else{
			locationFinder.registerBasicActivity(basicActivity);
		}
		return locationFinder;
	}
	
	public static LocationController getInstance () {
		return locationFinder;
	}

	/**
	 * 
	 * @return
	 */
	private Location getFirstLocation() {
		
		Location lastKnownLocation = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if (lastKnownLocation == null) {
			if(AppConstants.DEBUG) Log.e (TAG, "Location null from GPS provider");

			locationFound = false;
			lastKnownLocation = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if(lastKnownLocation != null){
				if(AppConstants.DEBUG)Log.e (TAG, "has network last known location");
				locationFound = true;

			}else{
				if(AppConstants.DEBUG)Log.e (TAG, "Location null from Netowork provider");
				locationFound = false;
			}
		}else{
			if(AppConstants.DEBUG)Log.e (TAG, "HAS LOCATION GPS ALREALDY");
			locationFound = true;
		}

		showTimeOut = locationFound;
		timeoutLocation.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(AppConstants.DEBUG)System.out.println("NNNNNNN -> TIMER END = " + locationFound);
				if (!showTimeOut) {
					
					mBasicActivity.handleFailedTask(AsyncTaskController.GET_LOCATION,  AppConstants.ERROR_LOCATION_TIMEOUT);
				}
				timeoutLocation.cancel();
				timeoutLocation=null;
			}
		}, 20000);
		
		return lastKnownLocation;
	}
	
	 private void updateMessage() {
		 
		 mBasicActivity.showDialog();
		 
		/*String message = "";
		if(gps_enabled && network_enabled){
			message = mContext.getString(R.string.searching_location_gpg_netowork);
			mBasicActivity.showDialog(AppConstants.DIALOG_ALL_LOCATION, message);
		}else if(gps_enabled){
			message = mContext.getString(R.string.searching_location_gpg);
			mBasicActivity.showDialog(AppConstants.DIALOG_GPS_LOCATION, message);
		}else{
			message = mContext.getString(R.string.searching_location_netowork);
			mBasicActivity.showDialog(AppConstants.DIALOG_NETWORK_LOCATION, message);
		}*/
	}

	public void startLocating (boolean showDialog) {
		
		if(AppConstants.DEBUG)Log.d(TAG,  "START LOCATING:"+isSearching);

		mShowDialog = showDialog;
		if(!mShowDialog) mBasicActivity.cancelDialog();
		
		if(!stopSearchingLocation){
			
			if(AppConstants.DEBUG)System.out.println("NNNN LOC FOUND1 to false");
			locationFound = false;
			isSearching = true;
	
			 if (locManager != null) { 
				 locManager.removeUpdates(gpsLocationListener);
				 locManager.removeUpdates(networkLocationListener);
			  }
			  
			  if (locManager == null) {
				  locManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
			  }
			
			
			//Start the location service 
			//ArrayList<String> providers = (ArrayList<String>) locManager.getAllProviders();
			network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			
			if(!network_enabled && !gps_enabled){
				openGPSSetting();
			}else{
				//we start to update by the network 
				if(AppConstants.DEBUG)Log.d(TAG,  "AAAA starting NETWORK geolocation");
				if(AppConstants.DEBUG)Log.d(TAG,  "AAAA NETWORK_PROVIDER ENALBLE?"+network_enabled);
				if(network_enabled) locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 00, 0, networkLocationListener );

				// we start updating the GPS location every 15 mins
				if(AppConstants.DEBUG)Log.d(TAG,  "starting GPS geolocation");
				if(gps_enabled)locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 00, 0, gpsLocationListener);
				
				if(AppConstants.DEBUG)System.out.println("network_enabled:"+network_enabled);
				if(AppConstants.DEBUG)System.out.println("gps_enabled"+gps_enabled);
				if(mShowDialog){
					updateMessage();
				}else{
					mBasicActivity.cancelDialog();
				}
			}
		}
	}
	
	public void setShowDialog(boolean showDialog){
		mShowDialog = showDialog;
	}
	
	public boolean hasWifiOrGPSACtivated(){
		
		boolean ret = false;
		
		ArrayList<String> providers = (ArrayList<String>) locManager.getAllProviders();
		
		for (String provider : providers) {
			if (provider.equals (LocationManager.GPS_PROVIDER) && locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				// we start updating the GPS location every 15 mins
				if(AppConstants.DEBUG)Log.d(TAG,  "HAS GPS geolocation");
				ret = true;
			} else if (provider.equals (LocationManager.NETWORK_PROVIDER) && isConnectedToNetwork() ){
				//we start to update by the network 
				if(AppConstants.DEBUG)Log.d(TAG,  "HAS NETWORK geolocation");
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * Stop updating the position of the user
	 */
	public void stopGPSLocating() {
		if(AppConstants.DEBUG)Log.d("TAG", "Stop gps updtating");
		 locManager.removeUpdates(gpsLocationListener);
	}
	
	/**
	 * Stop updating the position of the user
	 */
	public void stopNetworkLocating() {
		if(AppConstants.DEBUG)Log.d("TAG", "Stop network updtating");
		 locManager.removeUpdates(networkLocationListener);
	}
	
	public void stopLocating() {
		if(AppConstants.DEBUG)Log.d("TAG", "Stop updtating");
		stopSearchingLocation = false;
		isSearching = false;
		locManager.removeUpdates(gpsLocationListener);
		locManager.removeUpdates(networkLocationListener);
		
	}

	public boolean isNewLocationMoreAccruate () {
		return false;
	}

	private boolean isConnectedToNetwork() {
		
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void openGPSSetting () {
		mBasicActivity.handleFailedTask(AsyncTaskController.GET_LOCATION, AppConstants.ERROR_NO_LOCATION_PROVIDER);
	}

	public void registerBasicActivity(BasicInterface activity){
		mBasicActivity = activity;
	}
	
	public boolean hasFoundLocation() {
		if(AppConstants.DEBUG)Log.d( TAG, "has found location:"+locationFound);
		return locationFound;
	}
	
	public boolean isSearching() {
		return isSearching;
	}

	public boolean isStopSearchingLocation() {
		return stopSearchingLocation;
	}

	public void setStopSearchingLocation(boolean stopSearchingLocation) {
		this.stopSearchingLocation = stopSearchingLocation;
	}

	public void setCurrentLocation(Location loadLastKnowLocation) {
		currentLocation = loadLastKnowLocation;
	}

}