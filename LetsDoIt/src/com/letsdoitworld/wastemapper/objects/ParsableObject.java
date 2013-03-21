package com.letsdoitworld.wastemapper.objects;

import java.util.ArrayList;

import org.json.JSONObject;

public interface ParsableObject {
	
	public void setValue(String tag, String value, ArrayList<? extends ParsableObject> array);
	
	public ParsableObject getNewObject();
	
	public ParsableObject parse(JSONObject object);

	public ParsableObject getSubObject(String tmp);
	
}
