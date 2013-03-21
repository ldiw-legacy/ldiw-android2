package com.letsdoitworld.wastemapper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.letsdoitworld.wastemapper.R;
import com.letsdoitworld.wastemapper.controllers.ConnectionController;
import com.letsdoitworld.wastemapper.utils.SharedPrefsHelper;

public class SplashActivity extends Activity {
	private static final String STATE_ISWORING = "com.letsdoitworld.wastemapper.STATE_ISWORING";
	protected static final long TIME_SPLASH = 2 * 1000; // ms
	
	private boolean isWorking;
	private SplashWorker splashWorker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		isWorking = false;

		if (savedInstanceState != null) {
			isWorking = savedInstanceState.getBoolean(STATE_ISWORING);
		}
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(STATE_ISWORING, isWorking);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if (!isWorking) {
			splashWorker = new SplashWorker();
			splashWorker.execute(TIME_SPLASH);
		}
	}
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		if (isFinishing() && splashWorker != null && splashWorker.getStatus() == AsyncTask.Status.RUNNING) {
			splashWorker.cancel(true);
		}
	}

	public void finishSplash() {
		
		Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

		if(SharedPrefsHelper.isSession(this) && ConnectionController.getInstance(this).hasCurrentUser()){
			intent = new Intent(SplashActivity.this, MainActivity.class);
		}
		
		isWorking = false;
		
		if (intent != null) {
			startActivity(intent);
		}
		
		finish();
		
	}

	private class SplashWorker extends AsyncTask<Long, Void, Void> {
		private Intent intent;
		private int dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		} 

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

				
			finishSplash();
			
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Void doInBackground(Long... delay) {
			
	
			// Wait a bit
			try {
				Thread.sleep(delay[0]); 
			} catch (InterruptedException e) {
				// Ignore
			}

			return null;
		}
	}
}
