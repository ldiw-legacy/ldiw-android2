package com.letsdoitworld.wastemapper.utils;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityHelper {
	
	public static boolean isGPSEnabledAndActive(Context context){
		LocationManager lm = (LocationManager)context.getSystemService(Activity.LOCATION_SERVICE);
		if(lm != null){
			/*GpsStatus status = lm.getGpsStatus(null);
			status.*/
		}
		return lm == null ? false : lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	public static boolean isMobileNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (cm != null) {
			networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		}
		return networkInfo == null ? false : networkInfo.isConnected();
	}
	
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (cm != null) {
			networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		}
		return networkInfo == null ? false : networkInfo.isConnected();
	}
	
	public static boolean isNetworkConnected(Context activity)
	{
		boolean isWifiConnected = false;
		boolean isMobileConnected = false;

		ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo)
		{
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					isWifiConnected = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					isMobileConnected = true;
		}

		if(isWifiConnected || isMobileConnected)
			return true;
		else
			return false;
	}     
}
