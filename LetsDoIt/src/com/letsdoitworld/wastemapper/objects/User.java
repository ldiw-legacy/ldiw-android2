package com.letsdoitworld.wastemapper.objects;

import java.util.ArrayList;

import org.json.JSONObject;

public class User implements ParsableObject{
	
	/*
    "mail": "arthur.dibon@appytech.com",
    "sort": "0",
    "timezone_name": "Europe/Bucharest",
    "og_groups": [],
    "status": "1",
    "theme": null,
    "data": "a:0:{}",
    "init": "arthur.dibon@appytech.com",
    "access": "1357235278",
    "threshold": "0",
    "mode": "0",
    "picture": null,
    "timezone": "7200",
    "created": "1357235254",
    "roles": {
        "2": "authenticated user"
    },
    "name": "Hminos",
    "login": 1357831601,
    "language": null,
    "signature_format": "0",
    "signature": null
	*/
	
	public static String USER = "user";
	public static String UID = "uid";
	public static String MAIL = "mail";
	public static String SORT = "sort";
	private static String TIME_ZONE_NAME = "timezone_name";
	private static String OG_GROUPS = "og_groups";
	public static String STATUS = "status";
	public static String THEME = "theme";
	public static String DATA = "data";
	public static String INIT = "init";
	public static String ACCESS = "access";
	public static String THESHOLD = "threshold";
	public static String MODE = "mode";
	public static String PICTURE = "picture";
	public static String TIMEZONE = "timezone";
	public static String LOGIN = "login";
	
	public static String USER_TIMESTAMP = "timestamp";
	public static final String DEFAULT_SORT_ORDER = UID;
	
	private String uid = "";
	private String username = "";
	private String password = "";
	private String mail = "";
	private String sort = "";
	private String timezone_name = "";
	private String og_groups = "";
	private String status = "";
	private String data = "";
	private String init = "";
	private String access = "";
	private String threshold = "";
	private String mode = "";
	private String picture = "";
	private String timezone = "";
	private String login = "";
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUid() {
		return uid;
	}
	public String getMail() {
		return mail;
	}
	public String getSort() {
		return sort;
	}
	public String getTimezone_name() {
		return timezone_name;
	}
	public String getOg_groups() {
		return og_groups;
	}
	public String getStatus() {
		return status;
	}
	public String getData() {
		return data;
	}
	public String getInit() {
		return init;
	}
	public String getAccess() {
		return access;
	}
	public String getThreshold() {
		return threshold;
	}
	public String getMode() {
		return mode;
	}
	public String getPicture() {
		return picture;
	}
	public String getTimezone() {
		return timezone;
	}
	public String getLogin() {
		return login;
	}
	@Override
	public void setValue(String tag, String value,
			ArrayList<? extends ParsableObject> array) {
		// TODO Auto-generated method stub
		if(tag.equals(UID)) uid = value;
		if(tag.equals(MAIL)) mail = value;
		if(tag.equals(SORT))  sort = value;
		if(tag.equals(TIME_ZONE_NAME))  timezone_name = value;
		if(tag.equals(OG_GROUPS)) og_groups = value;
		if(tag.equals(STATUS))  status = value;
		if(tag.equals(DATA)) data = value;
		if(tag.equals(INIT)) init = value;
		if(tag.equals(ACCESS)) access = value;
		if(tag.equals(THESHOLD)) threshold = value;
		if(tag.equals(MODE)) mode = value;
		if(tag.equals(PICTURE)) picture = value;
		if(tag.equals(TIMEZONE)) timezone = value;
		if(tag.equals(LOGIN)) login = value;
		
	}
	
	@Override
	public ParsableObject getNewObject() {
		// TODO Auto-generated method stub
		return new User();
	}
	@Override
	public ParsableObject parse(JSONObject object) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ParsableObject getSubObject(String tmp) {
		// TODO Auto-generated method stub
		return null;
	}

}
