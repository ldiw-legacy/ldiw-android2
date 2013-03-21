package com.letsdoitworld.wastemapper.objects;

import java.util.ArrayList;

import org.json.JSONObject;

import android.graphics.drawable.Drawable;

public class Badge implements ParsableObject{
	

	private String name = "";
	private Drawable image;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Drawable getImage() {
		return image;
	}
	public void setImage(Drawable image) {
		this.image = image;
	}
	
	
	@Override
	public void setValue(String tag, String value,
			ArrayList<? extends ParsableObject> array) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ParsableObject getNewObject() {
		// TODO Auto-generated method stub
		return null;
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
