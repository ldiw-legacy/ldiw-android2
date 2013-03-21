package com.letsdoitworld.wastemapper.model;

import android.content.Context;


public interface BasicInterface {

	public void handleSuccessTask(int type) ;
	
	public void showDialog();
	
	public void cancelDialog();

	public void handleFailedTask(int mType, String error);
	
	public Context getContext();
	

}
