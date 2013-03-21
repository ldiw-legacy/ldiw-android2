package com.letsdoitworld.wastemapper.controllers;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.letsdoitworld.wastemapper.model.RemoteRequester;
import com.letsdoitworld.wastemapper.objects.User;
import com.letsdoitworld.wastemapper.utils.AppConstants;
import com.letsdoitworld.wastemapper.utils.SharedPrefsHelper;
import com.letsdoitworld.wastemapper.utils.Utils;

public class ConnectionController extends AbstractController {

	public static final String TAG = "ConnectionController";
	
	private static ConnectionController instance;
	private String API_URL = "";
	private User mCurrentUser;
	private Context mContext;
	private String mSession_name;
	private String mSession_id;
	private String mFbSession_tocken;
	private String mFb_uid;

	public static ConnectionController getInstance(Context context) {
        if (null == instance) {
            instance = new ConnectionController(context);
        }
        return instance;
    }
	
	public String getmFbSession_tocken() {
		return mFbSession_tocken;
	}

	public void setmFbSession_tocken(String mFbSession_tocken) {
		this.mFbSession_tocken = mFbSession_tocken;
	}

	public String getmFb_uid() {
		return mFb_uid;
	}

	public void setmFb_uid(String mFb_uid) {
		this.mFb_uid = mFb_uid;
	}

	private ConnectionController (Context context){
		mCurrentUser = new User();
		mContext = context;
	}
	
	public void saveLoginInfo(Context context) {
		SharedPrefsHelper.saveUserPass(context, mCurrentUser.getUsername(), mCurrentUser.getPassword());
		SharedPrefsHelper.saveSession(context, mSession_name,  mSession_id);
	}
	
	public User getCurrentUser(){
		return mCurrentUser;
	}
	
	public boolean hasCurrentUser(){
		if(mCurrentUser != null && !TextUtils.isEmpty(mCurrentUser.getUid())) return true;
		return false;
	}
	
	public void login(){
		
		JSONObject ret = null;
		ret = RemoteRequester.login(API_URL.concat(AppConstants.ADDRESS_JSON_LOGIN), mCurrentUser.getUsername(), mCurrentUser.getPassword());
		
		if(parser.isSuccess(ret)){
			mSession_name = ret.optString(AppConstants.SP_JSON_SESSION_NAME);
			mSession_id = ret.optString(AppConstants.SP_JSON_SESSION_ID);
			parser.parseJsonToObject(mCurrentUser, ret.optJSONObject( User.USER));
		}
	}
	
	public void loginWithFacebook(){
		
		JSONObject ret = null;
		ret = RemoteRequester.loginFacebook("https://test.letsdoitworld.org/?q=api/user/fbconnect.json", mFb_uid, mFbSession_tocken);
		
		if(parser.isSuccess(ret)){
			mSession_name = ret.optString(AppConstants.SP_JSON_SESSION_NAME);
			mSession_id = ret.optString(AppConstants.SP_JSON_SESSION_ID);
			parser.parseJsonToObject(mCurrentUser, ret.optJSONObject(User.USER));
		}
	}
	
	public void getAPI_URL(String lat, String lon){
		
		API_URL = SharedPrefsHelper.getAPI_URL(mContext);
		String BBOX = SharedPrefsHelper.getBBOX(mContext);
		
		if(API_URL != null && !TextUtils.isEmpty(API_URL)){
			
			//Check the current lat , lon are in the safe box
			String [] box = BBOX.split(",");
			
			if(box.length == 4){
				
				double currentLon = Double.parseDouble(lon);
				double currentLat = Double.parseDouble(lat);
				double minLon = Double.parseDouble(box[0]);
				double minLat = Double.parseDouble(box[1]);
				double maxLon = Double.parseDouble(box[2]);
				double maxLat = Double.parseDouble(box[3]);
				
				if( (currentLon > minLon && currentLon < maxLon) && (currentLat > minLat && currentLat < maxLat)){
					System.out.println("OK dans le safe box");
					
					//First check if last api check is not to old
					int timestamp = SharedPrefsHelper.getUptime(mContext);
				    int now = Utils.genTimeStamp();
				    if (now - timestamp < 3) {
				    	parser.setSucceeded();
				    }else{
				    	API_URL = null;
				    }
				}else{
					API_URL = null;
				}
			}
			
		}
		
		
		if(API_URL == null || TextUtils.isEmpty(API_URL)){
			JSONObject ret = null;
			
			ret = RemoteRequester.getAPIURL(lat, lon);
			
			if(ret == null) parser.setFailed();
			else {
				API_URL = ret.optString(AppConstants.API_BASE_URL_KEY);
				SharedPrefsHelper.saveAPI_URL(mContext, API_URL);
				
				BBOX = ret.optString(AppConstants.SAVE_BOX);
				SharedPrefsHelper.saveBBOX(mContext, BBOX);
				SharedPrefsHelper.saveUptime(mContext, Utils.genTimeStamp());
				
				if(AppConstants.DEBUG) Log.d("ConnectionController", "API URL:"+API_URL);
			}
			
		}
	}

	public void setCurrentUser(User user) {
		mCurrentUser = user;
	}
	
	public String getBaseUrl(){
		if(API_URL == null || TextUtils.isEmpty(API_URL)) API_URL = SharedPrefsHelper.getAPI_URL(mContext);
		return API_URL;
	}

	public void logout(Context context) {
		SharedPrefsHelper.emptyUserPass(context);
	    SharedPrefsHelper.emptySession(context);
	}

}
