package com.letsdoitworld.wastemapper.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

import com.letsdoitworld.wastemapper.utils.AppConstants;

	public class RemoteRequester {

	public static String TAG = "RemoteRequester";
	
	public static JSONObject getAPIURL(String lat, String lon) {
		
		if(AppConstants.DEBUG) Log.d(TAG, "REQUEST getAPIURL:"+AppConstants.URL_BASIC_ADDRESS +"/lat/" + lat + "/lon/" + lon);
		
		JSONObject input = new JSONObject();
		
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(2);
		params.add(new BasicNameValuePair("lat", String.valueOf(lat)));
	    params.add(new BasicNameValuePair("lon", String.valueOf(lon)));
		
		input = RestJsonClient.sendJsonRequestPost(AppConstants.URL_BASIC_ADDRESS, "", params);
		
		if(input == null) Log.d(TAG, "JSON NULL");
	    else if(AppConstants.DEBUG) Log.d(TAG, "RESPONSE getAPIURL:"+input.toString());
	
		return input;
	}
	
	public static JSONObject login(String url, String username, String password) {
		
		if(AppConstants.DEBUG) Log.d(TAG, "REQUEST LOGIN:"+ url +"/login/" + username + "/password/" + password);
		
		JSONObject input = new JSONObject();
		
		 ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
	
		 pairs.add(new BasicNameValuePair(AppConstants.USERNAME, username));
	     pairs.add(new BasicNameValuePair(AppConstants.PASSWORD, password));
	     
	     input = RestJsonClient.sendJsonRequestPost(url, "", pairs);
	     
	     if(input == null) Log.d(TAG, "JSON NULL");
	     else if(AppConstants.DEBUG) Log.d(TAG, "RESPONSE LOGIN:"+input.toString());
	 	
		return input;
	}
	
	public static JSONObject loginFacebook(String url, String uid, String tocken) {
		
		if(AppConstants.DEBUG) Log.d(TAG, "REQUEST LOGIN:"+ url +"/uid/" + uid + "/token/" + tocken);
		
		JSONObject input = new JSONObject();
		
		 ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
	
		 pairs.add(new BasicNameValuePair(AppConstants.FB_UID, uid));
	     pairs.add(new BasicNameValuePair(AppConstants.FB_ACCESS_TOCKEN, tocken));
	     
	     HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

	     DefaultHttpClient client = new DefaultHttpClient();

	     SchemeRegistry registry = new SchemeRegistry();
	     SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
	     socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
	     registry.register(new Scheme("https", socketFactory, 443));
	     SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
	     DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

	     // Set verifier     
	     HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    		
	     input = RestJsonClient.sendJsonRequestPost(url, "", pairs);
	     
	     if(input == null) Log.d(TAG, "JSON NULL");
	     else if(AppConstants.DEBUG) Log.d(TAG, "RESPONSE LOGIN:"+input.toString());
	 	
		return input;
	}
	
	public static InputStream getWastePoints(String url) {
		
		if(AppConstants.DEBUG) Log.d(TAG, "REQUEST getWastePoints: "+ url);
		
		InputStream input = null;
		
		input = RestJsonClient.sendJsonRequestGetCSV(url);
			
		return input;
	}
	
	
	public static JSONObject addWastePoint(String url, ArrayList<BasicNameValuePair> pairs, String session) {
		
		if(AppConstants.DEBUG) Log.d(TAG, "REQUEST ADD WASTE POINT:"+ url + "/session/" + session);
		
		JSONObject input = new JSONObject();
		
		input = RestJsonClient.sendJsonRequestPost(url, "", pairs);
	
		if(input == null) Log.d(TAG, "JSON NULL");
	    else if(AppConstants.DEBUG) Log.d(TAG, "RESPONSE ADD WASTE POINT:"+input.toString());
	 	
		return input;
	}
	
	public static JSONObject getExtraFrields(String url) {
		
		if(AppConstants.DEBUG) Log.d(TAG, "REQUEST getExtraFrields:"+ url);
		
		JSONObject input = new JSONObject();
		
		input = RestJsonClient.sendJsonRequestGet(url);
		if(input == null) Log.d(TAG, "JSON NULL");
	    else if(AppConstants.DEBUG) Log.d(TAG, "RESPONSE getExtraFrields:"+input.toString());
	 	
		return input;
	}

	
}
