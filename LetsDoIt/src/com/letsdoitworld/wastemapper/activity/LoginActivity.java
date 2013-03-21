package com.letsdoitworld.wastemapper.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.letsdoitworld.wastemapper.R;
import com.letsdoitworld.wastemapper.controllers.AsyncTaskController;
import com.letsdoitworld.wastemapper.objects.User;
import com.letsdoitworld.wastemapper.utils.AppConstants;
import com.letsdoitworld.wastemapper.utils.SharedPrefsHelper;
import com.letsdoitworld.wastemapper.utils.Utils;


public class LoginActivity extends BasicActivity {

	private Button mSignInButton, mFacebookButton, mRegisterButton;
	private EditText mUsernameEdit, mPasswordEdit;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		
		mActionBar.hide();
		
		mSignInButton = (Button) findViewById(R.id.button_signin);
		mFacebookButton = (Button) findViewById(R.id.button_login_facebook);
		mRegisterButton = (Button) findViewById(R.id.button_register_account);
		
		mUsernameEdit = (EditText) findViewById(R.id.edit_text_login);
		mPasswordEdit  = (EditText) findViewById(R.id.edit_text_password);
		 
		setListeners();
	
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.letsdoitworld.wastemapper", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
               System.out.println("KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		System.out.println("BDD save:"+mWastePointController.getNbSavedWastePoints());
		
		mUsernameEdit.setText(SharedPrefsHelper.getUsername(this));
		mPasswordEdit.setText(SharedPrefsHelper.getPassword(this));
		
		if (mLocationController != null) {
			mLocationController.registerBasicActivity(this);

			if(mLocationController.hasFoundLocation()){
	    		if (AppConstants.DEBUG) Log.d(TAG, "load remote info has location");
	    		manager.launchTask(AsyncTaskController.GET_API_URL, true);
	    	}else{
	    		if (AppConstants.DEBUG) Log.d(TAG, "NO LOCATION");
	    		setDialogMessage(getString(R.string.searching_location));
	    		mLocationController.startLocating(true);
	    	}
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mLocationController.stopLocating();
		
	}
	
	@Override
	public void handleFailedTask(int type, String error) {
		System.out.println("LOGIN FAILED:"+type);
		cancelDialog();
		setDialogMessage(getString(R.string.message_wait));
		switch (type){
		
		case AsyncTaskController.LOGIN:
			Utils.showMsg(mContext, error);
			mConnectionController.logout(this);
			
			/*if(mWastePointController.hasExtraFields()){
				handleSuccessTask(AsyncTaskController.GET_EXTRA_FIELD);
			}else{
				manager.launchTask(AsyncTaskController.GET_EXTRA_FIELD, true);
			}*/
			break; 
		case AsyncTaskController.GET_EXTRA_FIELD:
		case AsyncTaskController.GET_API_URL:
			Utils.showMsg(mContext, error);
			break;
		case AsyncTaskController.GET_LOCATION:
			if(SharedPrefsHelper.hasSavedLocation(this)){
				mLocationController.setCurrentLocation(SharedPrefsHelper.loadLastKnowLocation(this));
				mLocationController.stopGPSLocating();
				mLocationController.stopNetworkLocating();
				manager.launchTask(AsyncTaskController.GET_API_URL, true);
			}else{
				Utils.showMsg(this, AppConstants.getErrorMessage(this, AppConstants.ERROR_LOCATION_TIMEOUT));
			}
		break;
		case AsyncTaskController.LOGIN_FACEBOOK:
			System.out.println("FACEBOOK CONNECTION FAILED");
			
			break;
		
		}
	} 
	
	
	@Override
	public void handleSuccessTask(int type) {
		
		System.out.println("LOGIN SUCCESS:"+type);
		setDialogMessage(getString(R.string.message_wait));
		
		switch (type){
		
		case AsyncTaskController.LOGIN:
			if(mWastePointController.hasExtraFields()){
				handleSuccessTask(AsyncTaskController.GET_EXTRA_FIELD);
			}else{
				manager.launchTask(AsyncTaskController.GET_EXTRA_FIELD, true);
			}
			break;
		case AsyncTaskController.LOGIN_FACEBOOK:
			if(mWastePointController.hasExtraFields()){
				handleSuccessTask(AsyncTaskController.GET_EXTRA_FIELD);
			}else{
				manager.launchTask(AsyncTaskController.GET_EXTRA_FIELD, true);
			}
			break;
		case AsyncTaskController.GET_EXTRA_FIELD:
			cancelDialog();
			mConnectionController.saveLoginInfo(this);
			Intent mainMenu = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(mainMenu);
			finish();
			break;
		case AsyncTaskController.GET_LOCATION:
			mLocationController.stopLocating();
			manager.launchTask(AsyncTaskController.GET_API_URL, true);
			break;
			
		case AsyncTaskController.GET_API_URL:
			
			System.out.println("LOGIN not logged");
			cancelDialog();
			
			/*if(SharedPrefsHelper.isLoggedIn(this)){
				System.out.println("LOGIN Is logged in");
				//Auto login
		    	if(SharedPrefsHelper.isLoggedIn(this) && SharedPrefsHelper.isSession(this) && mConnectionController.hasCurrentUser())
				{
		    		 System.out.println("LOGIN current user not empty");
					  handleSuccessTask(AsyncTaskController.LOGIN);
				}else{
					  System.out.println("LOGIN Auto login");
					  User user = new User();
					  user.setUsername(SharedPrefsHelper.getUsername(this));
					  user.setPassword(SharedPrefsHelper.getPassword(this));
					  mConnectionController.setCurrentUser(user);
					  manager.launchTask(AsyncTaskController.LOGIN, true);
				}
 
			}else{
				
			}*/
			
			break;

		}
	}

	
	@Override
	public void setListeners() {
		
		mSignInButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if (mUsernameEdit.getText().toString().length() == 0 || mPasswordEdit.getText().toString().length() == 0) {
					Utils.showMsg(mContext, R.string.ERROR_EMPTY_LOGIN_PASSWORD);
				} else {
					
					User user = new User();
					user.setUsername(mUsernameEdit.getText().toString());
					user.setPassword(mPasswordEdit.getText().toString());
					mConnectionController.setCurrentUser(user);
					login();
				}

			}
		});
		
		mFacebookButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Session.openActiveSession(LoginActivity.this, true, new Session.StatusCallback() {
					
					@Override
					public void call(Session session, SessionState state, Exception exception) {
						// TODO Auto-generated method stub
						
						final String accessTocken = session.getAccessToken();

						if (session.isOpened()) {
							Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
								
								@Override
								public void onCompleted(GraphUser user, Response response) {
									 if (user != null) {
										 System.out.println("Got User info:"+user.getId());
										 System.out.println("Access tocken" + accessTocken);

										 // So let's get auth + stuff etc.. and let's try to register + login via facebook
										 // Login controller will deal with that
										 mConnectionController.setmFb_uid(user.getId());
										 mConnectionController.setmFbSession_tocken(accessTocken);
										 
										 manager.launchTask(AsyncTaskController.LOGIN_FACEBOOK, true);
									 }
								}
							});
									
						}
					} 
				}); 
			}
		});
		
		mRegisterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.openBrowser(LoginActivity.this, AppConstants.REGISTER_ACCOUNT);
			}
		});
	}
	
	private void login(){
		manager.launchTask(AsyncTaskController.LOGIN, true);
	}
	
	@Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }
}
