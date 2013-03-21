package com.letsdoitworld.wastemapper.objects;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.letsdoitworld.wastemapper.utils.AppConstants;

public class ExtraField implements ParsableObject{
	
	/*
	 *  "field_name": "volume",
            "min": 0,
            "edit_instructions": "Volume of the object, in m³",
            "max": 10000000,
            "typical_values": [
                [
                    0.0003,
                    "It all fits into my pocket"
                ],
                [
                    0.004,
                    "Fits in a plastic shopping bag"
                ],
                [
                    0.5,
                    "Fits into luggage trunk of a car"
                ],
                [
                    3,
                    "Fits into industrial truck/cargo vehicle"
                ],
                [
                    30,
                    "It's even more than that"
                ]
            ],
            "label": "Volume",
            "type": "float",
            "suffix": "m³",
            "allowed_values": [
                [
                    "field_composition_pmp",
                    "Plastics, metals, packaging"
                ],
	 */
	public static String FIELD_NAME = "field_name";
	public static String MIN = "min";
	public static String EDIT_INSTRUCTIONS = "edit_instructions";
	public static String MAX = "max";
	private static String TYPICAL_VALUES = "typical_values";
	public static String LABEL = "label";
	public static String TYPE = "type";
	private static String SUFFIX = "suffix";
	private static String ALLOWED_VALUES = "allowed_values";
	
	private String field_name = "";
	private String min = "";
	private String edit_instructions = "";
	private String max = "";
	private LinkedHashMap<String, String> typical_values = new LinkedHashMap<String, String>();
	private String label = "";
	private String type = "";
	private String suffix = "";
	private LinkedHashMap<String, String> allowed_values = new LinkedHashMap<String, String>();
	private String value = ""; 
	
	public ExtraField(){
		
	}
	
	public ExtraField(ExtraField copy){
		field_name = copy.field_name;
		min = copy.min;
		edit_instructions = copy.edit_instructions;
		max = copy.max;
		label = copy.label;
		type = copy.type;
		suffix = copy.suffix;
		value = "";
		
		int i = 0;
		 
		/*if(copy.getTypical_values() != null && copy.getTypical_values().size() > 0)
		{
			String[] typical_values_key = (String[]) copy.getTypical_values().keySet().toArray();
			
			for(i = 0; i< copy.getTypical_values().size(); i++){
				typical_values.put( typical_values_key[i], copy.getTypical_values().get(typical_values_key[i]));
			}
		}
		
		if(copy.getAllowed_values() != null && copy.getAllowed_values().size() > 0)
		{
			String[] allowed_values_key = (String[]) copy.getAllowed_values().keySet().toArray();
			
			for(i = 0; i< copy.getAllowed_values().size(); i++){
				allowed_values.put( allowed_values_key[i], copy.getTypical_values().get(allowed_values_key[i]));
			}
		}*/
	}
	
	@Override
	public void setValue(String tag, String value,
			ArrayList<? extends ParsableObject> array) {
		
		if(tag.equals(FIELD_NAME)) field_name = value;
		else if(tag.equals(MIN))  min = value;
		else if(tag.equals(EDIT_INSTRUCTIONS))  edit_instructions = value;
		else if(tag.equals(MAX)) max = value;
		else if(tag.equals(LABEL))  label = value;
		else if(tag.equals(TYPE)) type = value;
		else if(tag.equals(SUFFIX)) suffix = value;
		else if(tag.equals(TYPICAL_VALUES)) {
			
			JSONArray arraytmp;
			try {
				arraytmp = new JSONArray(value);
				JSONArray keysValues;
				for(int i = 0; i< arraytmp.length() ; i++){
					keysValues = new JSONArray(arraytmp.optString(i));
					if(keysValues != null && keysValues.length() > 1){
						typical_values.put(keysValues.optString(0), keysValues.optString(1));
					}
				}
			} catch (JSONException e) {
				if(AppConstants.DEBUG) e.printStackTrace();
			}
			
		}
		else if(tag.equals(ALLOWED_VALUES)) {
			
			JSONArray arraytmp;
			try {
				arraytmp = new JSONArray(value);
				JSONArray keysValues;
				for(int i = 0; i< arraytmp.length() ; i++){
					keysValues = new JSONArray(arraytmp.optString(i));
					if(keysValues != null && keysValues.length() > 1){
						allowed_values.put(keysValues.optString(0), keysValues.optString(1));
					}
				}
			} catch (JSONException e) {
				if(AppConstants.DEBUG) e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public ParsableObject getNewObject() {
		return new ExtraField();
	}
	@Override
	public ParsableObject parse(JSONObject object) {
		return null;
	}
	@Override
	public ParsableObject getSubObject(String tmp) {
		return null;
	}

	public String getField_name() {
		return field_name;
	}

	public String getMin() {
		return min;
	}

	public String getEdit_instructions() {
		return edit_instructions;
	}

	public String getMax() {
		return max;
	}

	public LinkedHashMap<String, String> getTypical_values() {
		return typical_values;
	}

	public String getLabel() {
		return label;
	}

	public String getType() {
		return type;
	}

	public String getSuffix() {
		return suffix;
	}

	public LinkedHashMap<String, String> getAllowed_values() {
		return allowed_values;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean isEmpty(){
		return TextUtils.isEmpty(value);
	}
	
}
