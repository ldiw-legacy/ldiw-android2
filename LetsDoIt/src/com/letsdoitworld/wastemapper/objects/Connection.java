package com.letsdoitworld.wastemapper.objects;

import java.util.ArrayList;

import org.json.JSONObject;

import android.text.TextUtils;

public class Connection implements ParsableObject{
	
	// Active attributs
	private String token = "";
	private String status = "0";
	private String debug = "";
	private String result = "";
	private boolean isServerOn = true;
	private String messageError = "";
	
	private static String TOKEN = "token";
	private static String STATUS = "status";
	private static String DEBUG = "Debug";
	private static String RESULT = "result";
	private static String ERROR_CODE = "errorCode";
	
	 public String getCodeError(){
		   return messageError;
	 } 
	
	 public boolean hasError(){
		   if(TextUtils.isEmpty(messageError)) return false;
		   return true;
	 }
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getStatus() {
		return status;
	}
	
	public String getDebug() {
		return debug;
	}
	
	public boolean isServerOn() {
		return isServerOn;
	}
	
	public void setServerOn(boolean isOn) {
		if(!isOn) status = "1";
		isServerOn = isOn;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public ParsableObject getNewObject() {
		return new Connection();
	}

	@Override
	public void setValue(String tag, String value,  ArrayList<? extends ParsableObject> array) {
		
		if(tag.equals(TOKEN)) {
			this.token = value;
		}
		if(tag.equals(STATUS)) {
			this.status = value;
		}
		if(tag.equals(RESULT)) {
			this.result = value;
		}
		if(tag.equals(DEBUG)) {
			this.debug = value;
		}
		if(tag.equals(ERROR_CODE)) {
			this.messageError = value;
		}
	}

	@Override
	public ParsableObject parse(JSONObject oject) {
		return null;
	}

	@Override
	public ParsableObject getSubObject(String tmp) {
		return null;
	}

	public boolean isSuccess() {
		System.out.println("result");
		if(!TextUtils.isEmpty(result)) {
			
			if(result.contains("Wrong username")) {
				System.out.println("result false");
				return false;
			}else{
				return true;
			}
		}
		if(status.equals("0")) return true;
		return false;
	}

}
