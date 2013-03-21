package com.letsdoitworld.wastemapper.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.letsdoitworld.wastemapper.utils.AppConstants;

/**
 * The aim of this object is to connect to the web service and to send request to it
 * Then it just give the answer back to the calling object
 * this class shoudl be used with the ServerConfiguration static class
 * @author kevinlegoff
 * 
 */
public class RestJsonClient {
	
	/**Send a get request to the server
	 * @param baseUrl the base url of the server
	 * @param service the service to contact example /api/rally_controller
	 * @param method the method to call from the service  for example /login/
	 * @param params  the get parameters of the request
	 * @return the JSONObjec matching the object of the query
	 */
	public static synchronized JSONObject sendJsonRequestGet (String baseUrl) {
		// connection to the web service
		
		// Fabrication de la requete
		//String paramString = formatParamString(params);

		DefaultHttpClient client = new DefaultHttpClient();
		
		HttpGet get = new HttpGet ( baseUrl );
		
		// Preparation de la reponse
		JSONObject json = new JSONObject();
        HttpResponse response;
        try {
        	response = client.execute( get);
            HttpEntity entity = response.getEntity();  	
            // A Simple JSON Response Read
            InputStream instream = entity.getContent();
            String result= convertStreamToString(instream).trim();  

            if( !result.startsWith("{")){
            	result = "{'result':" + result + "}";
            }
            json = new JSONObject(result);
            instream.close();
            
        }catch (InterruptedIOException interrupted) {
        	Log.e("RestJsonClient error" , interrupted.toString()); 
        	json = null;
         } catch (ClientProtocolException e) {
        	 Log.e("RestJsonClient protocol error" ,e.toString()); 
        	 json = null;
        } catch (IOException e) {
        	Log.e("RestJsonClient IO error" , e.toString()); 
        	e.printStackTrace();
            json = null;
        } catch (JSONException e) {
        	Log.e("RestJsonClient JSon error" , e.toString()); 
        	json = null;
        }
    	response = null;
    	//Log.d(" Reponse recue", json.toString() );
        return json;
	}


	/**
	 * Send a post request to the server 
	 * @param baseUrl the base url of the server
	 * @param service the service to contact example /api/rally_controller
	 * @param method the method to call from the service  for example /login/
	 * @param params  the post parameters of the request
	 * @return the JSONObject matching the object of the query
	 */
	public static synchronized JSONObject sendJsonRequestPost (String baseUrl, String cookie, List<BasicNameValuePair> params) {
    	
		
    	DefaultHttpClient httpclient = new DefaultHttpClient();
		// Prepare a request object
    	// preparation des parametre
    	
    	HttpPost httppost = new HttpPost(baseUrl);
    	//httppost.addHeader("Authorization", "Basic "+ auth);
    	if(!TextUtils.isEmpty(cookie)){
    		httppost.setHeader("Cookie", cookie);
    	}
    	
    	HttpResponse response;
        JSONObject json = new JSONObject();
        try {
        	if(params != null) httppost.setEntity(new UrlEncodedFormEntity(params));
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            // A Simple JSON Response Read
            InputStream instream = entity.getContent();
            String result= convertStreamToString(instream).trim();
            
            if( !result.startsWith("{")){
            	result = "{'result':" + result + "}";
            }
            json=new JSONObject(result);
            instream.close();    
        }catch (InterruptedIOException e) {
        	json = null;
        	if(AppConstants.DEBUG) e.printStackTrace();
         } catch (ClientProtocolException e) {
            json = null;
            if(AppConstants.DEBUG) e.printStackTrace();
        } catch (IOException e) {
            json = null;
            if(AppConstants.DEBUG) e.printStackTrace();
        } catch (JSONException e) {
        	json = null;
        	if(AppConstants.DEBUG) e.printStackTrace();
        }
    	response = null;
        return json;
    }

	/** Convert an input stream to a string
	 * @param is the input stream to convert
	 * @return the string
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			Log.d("IO exceptio ", e.toString());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.d("IO exception fermeture ", e.toString());
			}
		}
		return sb.toString();
	}

	// Calls a URI and returns the answer as a JSON object
	public static JSONObject executeHttpGet(String uri) throws Exception {
		HttpGet req = new HttpGet(uri);
		
		Log.d("rest json", uri.toString());
		HttpClient client = new DefaultHttpClient();
		HttpResponse resLogin = client.execute(req);
		BufferedReader r = new BufferedReader(
				new InputStreamReader(resLogin.getEntity()
						.getContent()));
		StringBuilder sb = new StringBuilder();
		String s = null;
		while ((s = r.readLine()) != null) {
			sb.append(s);

		}
		return new JSONObject(sb.toString());
	}


	public static InputStream sendJsonRequestGetCSV(String url) {
		
		DefaultHttpClient client = new DefaultHttpClient();
		
		HttpGet get = new HttpGet ( url );
		
		// Preparation de la reponse
		InputStream instream = null;
        HttpResponse response;
        try {
        	response = client.execute( get);
            HttpEntity entity = response.getEntity();  	
            instream = entity.getContent();
            
        }catch (InterruptedIOException interrupted) {
        	Log.e("RestJsonClient error" , interrupted.toString()); 
         } catch (ClientProtocolException e) {
        	 Log.e("RestJsonClient protocol error" ,e.toString()); 
        } catch (IOException e) {
        	Log.e("RestJsonClient IO error" , e.toString()); 
        	e.printStackTrace();
        }
    	response = null;
        return instream;
	}
	
	
}