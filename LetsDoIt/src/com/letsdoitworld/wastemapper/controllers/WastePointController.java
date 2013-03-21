package com.letsdoitworld.wastemapper.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.text.TextUtils;

import com.letsdoitworld.wastemapper.model.DatabaseHandler;
import com.letsdoitworld.wastemapper.model.RemoteRequester;
import com.letsdoitworld.wastemapper.objects.ExtraField;
import com.letsdoitworld.wastemapper.objects.WastePoint;
import com.letsdoitworld.wastemapper.utils.AppConstants;
import com.letsdoitworld.wastemapper.utils.SharedPrefsHelper;
import com.letsdoitworld.wastemapper.utils.Utils;

public class WastePointController extends AbstractController {

	public static final String TAG = "WastePointController";
	
	private static WastePointController instance;
	private ArrayList<WastePoint> mWastePointList;
	private ArrayList<WastePoint> mWastePointListUnsent;
	private WastePoint mCurrentWastePoint;
	private ArrayList<ExtraField> mExtraLIst = new ArrayList<ExtraField>();
	private DatabaseHandler mDatabase;
	private Context mContext;
	
	public static WastePointController getInstance(Context context) {
        if (null == instance) {
            instance = new WastePointController(context);
        }
        return instance;
    }
	
	private WastePointController (Context context){
		mContext = context;
		mDatabase = new DatabaseHandler(mContext);
	}
	
	public void getRemoteWastePointsList(String url, Location location){
		
		InputStream ret = null;
		ret = RemoteRequester.getWastePoints(url + AppConstants.ADDRESS_WASTE_POINT_MAXRES + SharedPrefsHelper.getMaxResult(mContext) + AppConstants.ADDRESS_WASTE_POINTS_NEAREST + location.getLongitude() + "," + location.getLatitude());
		
		System.out.println("RESPONSE WASTE = "  + ret);
		
		if(ret == null) parser.setFailed();
		else mWastePointList = (ArrayList<WastePoint>) parser.parseCSVToArray(ret, new WastePoint(mExtraLIst));
		
		if(parser.isSuccess()){
			parser.setSucceeded();
			sortByDistance(location);
			System.out.println("Size:"+mWastePointList.size());
		}
		
		try {
			if(ret != null) ret.close();
		} catch (IOException e) {
			if(AppConstants.DEBUG) e.printStackTrace();
		}
	}
	
	public ArrayList<WastePoint> getWastePointsList(){
		return mWastePointList;
	}
	
	public void sortByDistance(Location location){
		
		Location wp = new Location(location);
        for (int i = 0; i < mWastePointList.size(); i++) {
          wp.setLatitude(mWastePointList.get(i).getDoubleLat());
          wp.setLongitude(mWastePointList.get(i).getDoubleLon());
          mWastePointList.get(i).setDistance((double) location.distanceTo(wp));
        }
        
        Collections.sort(mWastePointList, distanceComparator);
	}
	
	public void addRemoteWastePoint(String url){

	      //Get the session
	      String sessionString = SharedPrefsHelper.getSessionName(mContext) + "=" + SharedPrefsHelper.getSessionId(mContext);
	      
	      ArrayList<BasicNameValuePair> array = new ArrayList<BasicNameValuePair>();
	      
	      array.add(new BasicNameValuePair("lat", String.valueOf(mCurrentWastePoint.getLat())));
	      array.add(new BasicNameValuePair("lon", String.valueOf(mCurrentWastePoint.getLon())));
	      
	      if (mCurrentWastePoint.getPhotoUri() != null) {
	    	 
	    	  String data = Utils.getCompressPhotoString(mCurrentWastePoint.getPhotoUri(), mContext);
	    	  if(data != null){
	    		  array.add(new BasicNameValuePair("photo_file_1", data));  
	    	  }
	      }

	      if(mCurrentWastePoint.getExtraFields() != null  && mCurrentWastePoint.getExtraFields().size() >0){
	    	   for (int i=0; i< mCurrentWastePoint.getExtraFields().size(); i++) {
	    		   String value = mCurrentWastePoint.getExtraFields().get(i).getValue();
	    		   if(!TextUtils.isEmpty(value)){
	    			     array.add(new BasicNameValuePair( mCurrentWastePoint.getExtraFields().get(i).getField_name(),value));
	    		     }
			      }
	      }
	      
	      JSONObject ret = null;
	      ret = RemoteRequester.addWastePoint(url, array, sessionString);
	      
	      if(parser.isSuccess(ret)){
	    	  System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA 222");

	    	 // mExtraLIst = new ArrayList<ExtraField>();
	    	  WastePoint point = new WastePoint(mExtraLIst);
	    	  point = (WastePoint) parser.parseJsonToObject(point, ret.optJSONObject(AppConstants.SP_JSON_WP));
	    	  if(point != null) {
	    		  mWastePointList.add(point);  
	    	  }else{
	    		  parser.setFailed();
	    	  }
	      }else{
	    	  System.out.println("ADD FAILED INSERT BDD");
	    	  parser.setFailed();
	    	  //Save in db and start service to check every x minutes
	    	  String data = mCurrentWastePoint.getData();
	    	  System.out.println("data:"+data);
	    	  ContentValues contentValues = new ContentValues(4);
	    	  contentValues.put(WastePoint.DATA, data);
	    	  contentValues.put(WastePoint.LATLON, mCurrentWastePoint.getLat()+","+mCurrentWastePoint.getLon());
	    	  contentValues.put(WastePoint.ACTION_TYPE, AppConstants.ACTION_ADD_WASTE_POINT);
	    	  if(!TextUtils.isEmpty(SharedPrefsHelper.getSessionId(mContext)))contentValues.put(AppConstants.SP_JSON_SESSION_ID,  SharedPrefsHelper.getSessionId(mContext));
	    	  if(!TextUtils.isEmpty(SharedPrefsHelper.getSessionName(mContext))) contentValues.put(AppConstants.SP_JSON_SESSION_NAME,  SharedPrefsHelper.getSessionName(mContext));
	    	  
	    	  int update = mDatabase.update(DatabaseHandler.WASTE_POINT, contentValues, WastePoint.LATLON + "='" + mCurrentWastePoint.getLat()+","+mCurrentWastePoint.getLon()+"'", null);
	    	  System.out.println("UPDATE:"+update);
	    	  if(update == 0){
	    		  mDatabase.insert(DatabaseHandler.WASTE_POINT, contentValues);
	    	  }
	      }
	}
	
	public int getNbSavedWastePoints(){
	
		Cursor cursorWastePoints = mDatabase.query(DatabaseHandler.WASTE_POINT, WastePoint.WASTE_POINTS_PROJECTION, "", null, "");
		int count = 0;
		if(cursorWastePoints != null) {
			count = cursorWastePoints.getCount();
			cursorWastePoints.close();
			cursorWastePoints = null;
		}
		return count;
	}
		
	
	public void resendSavedWastePoints(String url){
		
			Cursor cursorWastePoints = mDatabase.query(DatabaseHandler.WASTE_POINT, WastePoint.WASTE_POINTS_PROJECTION, "", null, "");

			if(cursorWastePoints != null){
		    	  System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA 333");

				WastePoint point = new WastePoint(mExtraLIst);
				ArrayList<BasicNameValuePair> array = new ArrayList<BasicNameValuePair>();
				while(cursorWastePoints.moveToNext()){
					
					String id = cursorWastePoints.getString(cursorWastePoints.getColumnIndex(WastePoint._ID));
					String data = cursorWastePoints.getString(cursorWastePoints.getColumnIndex(WastePoint.DATA));
					String actionType = cursorWastePoints.getString(cursorWastePoints.getColumnIndex(WastePoint.ACTION_TYPE));
					String sessionId = cursorWastePoints.getString(cursorWastePoints.getColumnIndex(AppConstants.SP_JSON_SESSION_ID));
					String sessionName = cursorWastePoints.getString(cursorWastePoints.getColumnIndex(AppConstants.SP_JSON_SESSION_NAME));
					String sessionString = sessionName + "=" + sessionId;
					
					System.out.println("RETRIEVED FROM BDD:"+data);
					
					
					try {
						point = (WastePoint) parser.parseJsonToObject(point, new JSONObject(data));
						array.add(new BasicNameValuePair("lat", String.valueOf(point.getLat())));
					    array.add(new BasicNameValuePair("lon", String.valueOf(point.getLon())));
					      
				        if (point.getPhotoUri() != null) {
				    	 
				    	  String dataPhoto = Utils.getCompressPhotoString(point.getPhotoUri(), mContext);
				    	  if(dataPhoto != null){
				    		  array.add(new BasicNameValuePair("photo_file_1", dataPhoto));  
				    	   }
				        }

				        if(point.getExtraFields() != null  && point.getExtraFields().size() >0){
				    	   for (int i=0; i< point.getExtraFields().size(); i++) {
				    		   String value = point.getExtraFields().get(i).getValue();
				    		   if(!TextUtils.isEmpty(value)){
				    			     array.add(new BasicNameValuePair( point.getExtraFields().get(i).getField_name(),value));
				    		     }
						      }
				        }
					      
					    JSONObject ret = RemoteRequester.addWastePoint(url, array, sessionString);
					      
					    if(parser.isSuccess(ret)){
					    	  System.out.println("SUCCESS READDING");
					    	  point = (WastePoint) parser.parseJsonToObject(point, ret.optJSONObject(AppConstants.SP_JSON_WP));
					    	  if(point != null) {
					    		  mWastePointList.add(point);  
					    	  }
					    	  mDatabase.delete(DatabaseHandler.WASTE_POINT, WastePoint._ID + "=" +id, null);
					    }
					} catch (JSONException e) {
						if(AppConstants.DEBUG) e.printStackTrace();
					}
				}
				cursorWastePoints.close();
		}
	}
	
	public void confirmRemoteWastePoint(String url){

	      //Get the session
	      String sessionString = SharedPrefsHelper.getSessionName(mContext) + "=" + SharedPrefsHelper.getSessionId(mContext);
	      
	      JSONObject ret = null;
	      ret = RemoteRequester.addWastePoint(url, null, sessionString);
	        
	}
	
	/*
	 * Compare 2 waste points by distance
	 */
	private Comparator<WastePoint> distanceComparator = new Comparator<WastePoint>() {
		
		@Override
		public int compare(WastePoint wastePoint1, WastePoint wastePoint2) {
			
			double distance1 = wastePoint1.getDistance();
			double distance2 = wastePoint2.getDistance();
			
			return (distance2>distance1 ? -1 : (distance2==distance1 ? 0 : 1));
		}
	};

	public WastePoint getCurrentWastePoint() {
		return mCurrentWastePoint;
	}

	public void setCurrentWastePoint(WastePoint mCurrentWastePoint) {
		this.mCurrentWastePoint = mCurrentWastePoint;
	}

	public void getRemoteExtraFields(String url) {
		JSONObject ret = null;
	      int count = 5;
	      while (ret == null && count > 0) {
	    	  ret = RemoteRequester.getExtraFrields(url);
	          count--;
	      }
	      
	      if(parser.isSuccess(ret)){
	    	  System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
	    	  mExtraLIst = (ArrayList<ExtraField>) parser.parseToJsonList("result", new ExtraField(), ret);
	      }
		
	}

	public void restWastePoint() {
		mCurrentWastePoint = new WastePoint(mExtraLIst);
	}
	
	
	public ArrayList<ExtraField> getExtraFields(){
		return mExtraLIst;
	}

	public void restWastePointsList() {
		mWastePointList = new ArrayList<WastePoint>();
	}

	public boolean hasExtraFields() {
		if(mExtraLIst != null && mExtraLIst.size() > 0) return true; 
		return false;
	}
}
