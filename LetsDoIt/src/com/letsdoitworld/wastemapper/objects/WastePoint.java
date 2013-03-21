package com.letsdoitworld.wastemapper.objects;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

import com.letsdoitworld.wastemapper.utils.AppConstants;

public class WastePoint implements ParsableObject{
	
	/*
     "lon": "39.876751406322",
     "composition_large": "",
     "composition_glass": "220781.9",
     "distance_meters": "Country",
     "composition_pmp": "Saudi Arabia",
     "photos": "",
     "id": "51392",
     "composition_paper": "}",
     "composition_other": "0",
     "geo_areas_json": "{",
     "description": "See detail of this illegal dump on TrashOut here http://www.trashout.me/illegal-dump/1426002 ",
     "nr_of_nodes": "",
     "volume": "1",
     "nr_of_tires": ":",
     "lat": "21.358612241868"
	*/
	
	public static String LON = "lon";
	public static String COMPOSITION_LARGE = "composition_large";
	public static String COMPOSITION_GLASS = "composition_glass";
	public static String DISTANCE_METERS = "distance_meters";
	private static String COMPOSITION_PMP = "composition_pmp";
	public static String COMPOSITION_PAPER = "composition_paper";
	public static String COMPOSITION_OTHER = "composition_other";
	private static String PHOTOS = "photos";
	public static String ID = "id";
	public static String GEO_AREA_JSON = "geo_areas_json";
	public static String NB_OF_NODES = "nr_of_nodes";
	public static String VOLUME = "volume";
	public static String LAT = "lat";
	public static String _ID = "_ID";
	private static final String PHOTO_FILE = "photo";
	public static final String LATLON = "latlon";
	
	public static final String DEFAULT_SORT_ORDER = _ID;
	public static final String DATA = "data";
	public static final String ACTION_TYPE = "action";
	
	private String lon = "";
	private String composition_large = "";
	private String composition_glass = "";
	private String distance_meters = "";
	private String composition_pmp = "";
	private String photos = "";
	private String id = "";
	private String composition_paper = "";
	private String composition_other = "";
	private String geo_areas_json = "";
	private String nr_of_nodes = "";
	private String volume = "";
	private String lat = "";
	
	private double doubleLat = 0;
	private double doubleLon = 0;
	private double distance = 0;
	
	private File photoFile;
	private Uri imageUri;
	
	//List of unknow fields that we may receive, basic key , value like "key":"value"
	//Need to add listener on View Element to change field value during edition
	//private HashMap<String, String> mExtraFields = new HashMap<String, String>();
	private ArrayList<ExtraField> mListExtra = new ArrayList<ExtraField>();
	
	public static final String[] WASTE_POINTS_PROJECTION = new String[] {
		WastePoint._ID, WastePoint.DATA, WastePoint.ACTION_TYPE, AppConstants.SP_JSON_SESSION_ID, AppConstants.SP_JSON_SESSION_NAME
	};
	
	public WastePoint(ArrayList<ExtraField> mListExtra){
		this.mListExtra = new ArrayList<ExtraField>();
		for(ExtraField field: mListExtra){
			ExtraField tmp = new ExtraField(field);
			this.mListExtra.add(tmp);
		}
	}
	
	public String getLon() {
		return lon;
	}

	public String getComposition_large() {
		return composition_large;
	}

	public String getComposition_glass() {
		return composition_glass;
	}

	public String getDistance_meters() {
		return distance_meters;
	}

	public String getComposition_pmp() {
		return composition_pmp;
	}

	public String getPhotos() {
		return photos;
	}

	public String getId() {
		return id;
	}
	
	public String getComposition_paper() {
		return composition_paper;
	}

	public String getComposition_other() {
		return composition_other;
	}

	public String getGeo_areas_json() {
		return geo_areas_json;
	}

	public String getNr_of_nodes() {
		return nr_of_nodes;
	}

	public String getVolume() {
		return volume;
	}

	public String getLat() {
		return lat;
	}
	
	public String getData() {
		JSONObject json = new JSONObject();
		try {
			//json.putOpt(COMPOSITION_GLASS,composition_glass);
			//json.putOpt(COMPOSITION_LARGE,composition_large);
			//json.putOpt(COMPOSITION_OTHER,composition_other);
			//json.putOpt(COMPOSITION_PAPER,composition_paper);
			//json.putOpt(COMPOSITION_PMP,composition_pmp);
			json.putOpt(LON, lon);
			json.putOpt(PHOTOS,photos);
			json.putOpt(ID,id);
			json.putOpt(GEO_AREA_JSON,geo_areas_json);
			json.putOpt(NB_OF_NODES,nr_of_nodes);
			json.putOpt(LAT,lat);
			if(getPhotoUri() != null) json.putOpt(PHOTO_FILE, imageUri.toString());
			json.putOpt(DISTANCE_METERS, distance_meters);
			
			for(int i = 0; i< mListExtra.size(); i++){
				json.putOpt(mListExtra.get(i).getField_name(), mListExtra.get(i).getValue());
			}
			
		} catch (JSONException e) {
			if(AppConstants.DEBUG)e.printStackTrace();
		}
		return json.toString();
	}

	@Override
	public void setValue(String tag, String value,
			ArrayList<? extends ParsableObject> array) {
		
		if(tag.equals(COMPOSITION_GLASS)) composition_glass = value;
		else if(tag.equals(COMPOSITION_LARGE))  composition_large = value;
		else if(tag.equals(COMPOSITION_OTHER))  composition_other = value;
		else if(tag.equals(COMPOSITION_PAPER)) composition_paper = value;
		else if(tag.equals(DISTANCE_METERS))  distance_meters = value;
		else if(tag.equals(PHOTOS)) photos = value;
		else if(tag.equals(PHOTOS)) photos = value;
		else if(tag.equals(ID)) id = value;
		else if(tag.equals(GEO_AREA_JSON)) geo_areas_json = value;
		else if(tag.equals(NB_OF_NODES)) nr_of_nodes = value;
		else if(tag.equals(PHOTO_FILE)) {
			imageUri = Uri.parse(value);
		}
		else if(tag.equals(LAT)) {
			lat = value;
			doubleLat = Double.parseDouble(lat);
		}
		else if(tag.equals(LON)) {
			lon = value;
			doubleLon = Double.parseDouble(lon);
		}else{
			//mExtraFields.put(tag, value);
			for(int i = 0; i< mListExtra.size(); i++){
				if(mListExtra.get(i).getField_name().equals(tag)){
					mListExtra.get(i).setValue(value);
					break;
				}
			}
		}
	}
	
	
	
	@Override
	public ParsableObject getNewObject() {
		return new WastePoint(mListExtra);
	}
	@Override
	public ParsableObject parse(JSONObject object) {
		return null;
	}
	@Override
	public ParsableObject getSubObject(String tmp) {
		return null;
	}

	public double getDoubleLat() {
		return doubleLat;
	}
	
	public double getDoubleLon() {
		return doubleLon;
	}

	public void setDistance(double distanceTo) {
		distance = distanceTo;
	}
	
	public double getDistance(){
		return distance;
	}
	
	@Override
	public String toString(){
		return getData();
	}

	public ArrayList<ExtraField> getExtraFields() {
		return mListExtra;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public void setNr_of_nodes(String nr_of_nodes) {
		this.nr_of_nodes = nr_of_nodes;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}
	
	public String getExtraValue(String fiedName){
		for(ExtraField field : mListExtra){
			if(field.getField_name().equals(fiedName)){
				return field.getValue();
			}
		}
		return "";
	}
	
	public void setExtraValue(String fiedName, String value){
		for(ExtraField field : mListExtra){
			if(field.getField_name().equals(fiedName)){
				field.setValue(value);
				return;
			}
		}
	}
	
	public String[] extraKeySet(){
		String[] keys = new String[mListExtra.size()];
		for(int i =0; i< mListExtra.size(); i++){
			keys[i] =  mListExtra.get(i).getField_name();
		}
		return keys;
	}

	public HashMap<String, String> entrySet() {
		HashMap<String, String> map = new HashMap<String, String>();
		
		for(int i =0; i< mListExtra.size(); i++){
			map.put(mListExtra.get(i).getField_name(), mListExtra.get(i).getValue());
		}
		
		return map;
	}

	public Uri getPhotoUri(){
		return this.imageUri;
	}
	
	public void setPhotoUri(Uri imageUri) {
		this.imageUri = imageUri;
	}
}
