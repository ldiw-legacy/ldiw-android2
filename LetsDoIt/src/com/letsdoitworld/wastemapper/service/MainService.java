package com.letsdoitworld.wastemapper.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.letsdoitworld.wastemapper.controllers.ConnectionController;
import com.letsdoitworld.wastemapper.controllers.WastePointController;
import com.letsdoitworld.wastemapper.utils.AppConstants;
import com.letsdoitworld.wastemapper.utils.ConnectivityHelper;

public class MainService extends Service {
  public static final String TAG = "DoItTag";

  private final Handler _handler = new Handler();
  private boolean hasStarted = false;
  private static int DATA_INTERVAL = 60 * 1000;
  private WastePointController mWastePointController;
  private ConnectionController mConnectionController;
  SendWastePoint task = new SendWastePoint();
  
  @Override
  public IBinder onBind(Intent arg0) {
    return null;
  }

  private final Runnable getData = new Runnable()
  {
      @Override
      public void run()
      {
    	  mWastePointController = WastePointController.getInstance(MainService.this);
    	  mConnectionController = ConnectionController.getInstance(MainService.this);
    	  
    	  //check if network and send data
    	  if(ConnectivityHelper.isNetworkConnected(MainService.this)){
    		  if(mWastePointController.getNbSavedWastePoints() > 0){
    			  task.execute();
    			  
    		  }else{
    			  System.out.println("Not more offline waste points");
    			  stopSelf();
    		  }
    		 
    	  }else{
    		  //else retry 5 minutes later
              getDataFrame();  
    	  }
      }
  };
  
  private void getDataFrame() 
  {
	  if(AppConstants.DEBUG) System.out.println("start service check");
	  hasStarted = true;
      _handler.postDelayed(getData, DATA_INTERVAL);
  }

  @Override
  public void onStart(Intent intent, int startId) {
    Log.i(TAG, "onStart");
    if(!hasStarted) getDataFrame();
  }

  @Override
  public void onDestroy() {
    Log.i(TAG, "Service is quiting");
    super.onDestroy();
    _handler.removeCallbacks(getData);
  }
  
  public class SendWastePoint extends AsyncTask<Void, Void, Void>{

	@Override
	protected Void doInBackground(Void... params) {
		 mWastePointController.resendSavedWastePoints(mConnectionController.getBaseUrl().concat(AppConstants.ADDRESS_PUT_WASTE_POINT));
		return null;
	}
	
	@Override
	protected void onPostExecute(Void unused) {
		 if(mWastePointController.getNbSavedWastePoints() == 0){
			  if(AppConstants.DEBUG)System.out.println("OK STOP SERVICE");
			  stopSelf();
		  }
	}
	  
  }

}
