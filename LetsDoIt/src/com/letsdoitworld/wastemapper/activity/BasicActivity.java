package com.letsdoitworld.wastemapper.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.letsdoitworld.wastemapper.R;
import com.letsdoitworld.wastemapper.controllers.AsyncTaskController;
import com.letsdoitworld.wastemapper.controllers.ConnectionController;
import com.letsdoitworld.wastemapper.controllers.LocationController;
import com.letsdoitworld.wastemapper.controllers.WastePointController;
import com.letsdoitworld.wastemapper.model.BasicInterface;

public abstract class BasicActivity extends SherlockFragmentActivity implements BasicInterface{

public final static String TAG = "BasicActivity";
	
	public final String REQUEST_LOGIN = "REQUEST_LOGIN";
	
	static final int LOGIN_DIALOG = 1;
	
	public ProgressDialog progressDialog;
	public Dialog loginDialog;

	protected Context mContext;
	
	public AsyncTaskController manager;
	public String message = "";
 
	public ConnectionController mConnectionController;
	public LocationController mLocationController;
	public WastePointController mWastePointController;
	//private DatabaseHandler mDatabase;
	
	/** Called when the activity is first created. */
    protected SharedPreferences mPrefs;
	
    public ActionBar mActionBar;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* FOR EMPTY CONTROLLERS
		if( !this.getClass().getSimpleName().contains("LoginActivity") && (mConnectionController == null || !mConnectionController.hasCurrentUser())){
			Intent intentRedirect = new Intent(this, LoginActivity.class);
			startActivity(intentRedirect);
			finish();
		}*/
		
		mActionBar = getSupportActionBar();
		if (mActionBar != null)
        mActionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_green));

        
		mContext = this;
		manager = new AsyncTaskController(BasicActivity.this);
		mConnectionController = ConnectionController.getInstance(mContext);
		mLocationController = LocationController.getInstance(BasicActivity.this);
		mWastePointController = WastePointController.getInstance(mContext);
		
		message = getString(R.string.message_wait);
		progressDialog = new ProgressDialog(mContext);

		progressDialog.setMessage(message);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		
		loginDialog = new Dialog(mContext);
		
	}
	
	public void setDialogMessage(String message){
		this.message = message;
		progressDialog.setMessage(message);
	}

    protected abstract void setListeners();

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(progressDialog != null)
			progressDialog.dismiss();
	}
	
	public void showDialog() {
		if(!progressDialog.isShowing()){
			
			progressDialog = new ProgressDialog(mContext);

			progressDialog.setMessage(message);
			progressDialog.setCancelable(false);
			progressDialog.show();	
		}
	}
	
	public void cancelDialog() {
		if(progressDialog.isShowing())
			progressDialog.dismiss();
	}

	public void refresh(){}
	
	@Override
	public Context getContext() {
		return this.mContext;
	}
	
	/**
	 * Open the web browser 
	 * @url the url of the page that the browser should open 
	 * @param URL
	 */
	public void openBrowser (String URL) {
		Uri uriUrl = Uri.parse(URL);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl); 
		startActivity(launchBrowser);  
	}
	
	/**
	 * Open the web browser 
	 * @url the url of the page that the browser should open 
	 * @param URL
	 */
	public void openPDF (String URL) {
		Uri uriUrl = Uri.parse("https://docs.google.com/viewer?url="+Uri.parse(URL));
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl); 
		startActivity(launchBrowser);  
	}
}
