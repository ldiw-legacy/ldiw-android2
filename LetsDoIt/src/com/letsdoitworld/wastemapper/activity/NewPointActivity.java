package com.letsdoitworld.wastemapper.activity;

import java.io.File;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.letsdoitworld.wastemapper.R;
import com.letsdoitworld.wastemapper.controllers.AsyncTaskController;
import com.letsdoitworld.wastemapper.fragment.ChangePointMapFragment;
import com.letsdoitworld.wastemapper.fragment.NewPointFragment;
import com.letsdoitworld.wastemapper.objects.ExtraField;
import com.letsdoitworld.wastemapper.utils.DialogFactory;


public class NewPointActivity extends BasicActivity {

  public static final int REQUEST_CODE_MAP = 22;
  public static final int REQUEST_CODE_CAMERA = 12;
  protected static final String TAG = "NewPointActivity";
 
  private Uri imageUri;
  private Button sendData;
  private File photoFile;
  private ViewSwitcher switcher;
  private ProgressDialog progress;
  private View mainView;
  
  private boolean isChangeMapTime;
  private double currentPointLat, currentPointLon;
  private NewPointFragment newPointFragment;
  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
		
		this.setContentView(R.layout.main);
		
		setListeners();
		
		initNewPoint();
		
		if(getIntent().hasExtra("imageUri")) {
			imageUri = getIntent().getExtras().getParcelable("imageUri");
			mWastePointController.getCurrentWastePoint().setPhotoUri(imageUri);
		}
		
		 // set defaults for logo & home up
		
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setIcon(R.drawable.navigation_cancel);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setHomeButtonEnabled(true);
				
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		newPointFragment = new NewPointFragment();
      
  	    if (imageUri != null) newPointFragment.setImageUri(imageUri);
      
  	    fragmentTransaction.add(R.id.fragment_container, newPointFragment);
  	    fragmentTransaction.commit();
      
  		 // default to tab navigation
  	    // showTabsNav();    
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuItem item = menu.add(0, R.drawable.ok, 0,"").setIcon(R.drawable.navigation_accept);
		
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				
		return super.onCreateOptionsMenu(menu);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println("onOptionsItemSelected:"+item.getTitle()+", "+item.getItemId());
		
		switch (item.getItemId()) {
			case android.R.id.home:
				
				if(isChangeMapTime) {
					currentPointLat=mLocationController.getCurrentLocation().getLatitude();
					currentPointLon=mLocationController.getCurrentLocation().getLongitude();
					
					//Default the current location, user can after re-define the waste point location
					mWastePointController.getCurrentWastePoint().setLat(mLocationController.getCurrentLocation().getLatitude() + "");
					mWastePointController.getCurrentWastePoint().setLon(mLocationController.getCurrentLocation().getLongitude() + "");
					
					FragmentManager fragmentManager = getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					newPointFragment = new NewPointFragment();
			  	    fragmentTransaction.replace(R.id.fragment_container, newPointFragment);
			  	    fragmentTransaction.commit();
			  	    isChangeMapTime= false;
				} else {
					finish();
				}
				break;
			
			case R.drawable.ok:
				
				if(isChangeMapTime) {
					FragmentManager fragmentManager = getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					newPointFragment = new NewPointFragment();

			  	    fragmentTransaction.replace(R.id.fragment_container, newPointFragment);
			  	    fragmentTransaction.commit();
			  	   isChangeMapTime= false;
				} else {
					boolean success = true;
					for(ExtraField f:mWastePointController.getCurrentWastePoint().getExtraFields()){
						
						System.out.println("EXTRA FIELD --- " + f.getField_name());
						System.out.println("EXTRA VALUE --- " + f.getValue());
						System.out.println("EXTRA TYPE --- " + f.getType());
						System.out.println("EXTRA MAX --- " + f.getMax());

						try {
							if (f.getType().equals("integer") || f.getType().equals("float")) {
								
								if (Double.parseDouble(f.getMax()) < Double.parseDouble(f.getValue())) {
									success=false;
									Toast.makeText(mContext, getString(R.string.error_value_too_big).replace("name", f.getLabel())+" "+ f.getMax() , Toast.LENGTH_LONG).show();
								} else if (Double.parseDouble(f.getMin()) > Double.parseDouble(f.getValue())){
									Toast.makeText(mContext, getString(R.string.error_value_too_small).replace("name", f.getLabel())+" "+ f.getMin() , Toast.LENGTH_LONG).show();
									success=false;
								} 
							}
							

						} catch (Exception e) {
							e.printStackTrace();
						}
							
						
						
						
					}
					
					if (success) manager.launchTask(AsyncTaskController.ADD_WASTE_POINT, true);
					
				}
				
				break;
			default:
				break;
		}
		return true;
	}

		
	
	private void initNewPoint(){
		mWastePointController.restWastePoint();
		
		currentPointLat=mLocationController.getCurrentLocation().getLatitude();
		currentPointLon=mLocationController.getCurrentLocation().getLongitude();
		
		//Default the current location, user can after re-define the waste point location
		mWastePointController.getCurrentWastePoint().setLat(mLocationController.getCurrentLocation().getLatitude() + "");
		mWastePointController.getCurrentWastePoint().setLon(mLocationController.getCurrentLocation().getLongitude() + "");
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		
	}	 
	Dialog d;
	@Override
	public void handleSuccessTask(int type) {
		cancelDialog();
		mWastePointController.restWastePointsList();
		
		OnClickListener l = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				d.dismiss();

				finish();
			}
		};

		d = DialogFactory.getSuccessAddPoint(this, l);

		
        d.show();

	}
	
	@Override
	public void handleFailedTask(int type, String error) {
		cancelDialog();
		
		switch (type) {
		case AsyncTaskController.ADD_WASTE_POINT:
			
			OnClickListener l = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					d.dismiss();

					finish();
				}
			};

			d = DialogFactory.getFailedAddPoint(this, l);

			
	        d.show();
			
			//if(mWastePointController.getNbSavedWastePoints() > 0){
			//	startService(new Intent(this, MainService.class));
			//}
			break;

		default:
			break;
		}
		
	}
	
	public void changeMapPosition() {
		
		isChangeMapTime = true;
		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ChangePointMapFragment frag = new ChangePointMapFragment();
		ft.replace(R.id.fragment_container, frag);
		ft.commit();
	}
	
	
	public void changeLatLon(double lat, double lon) {
		currentPointLat = lat;
		currentPointLon = lon;
		
		mWastePointController.getCurrentWastePoint().setLat(lat + "");
		mWastePointController.getCurrentWastePoint().setLon(lon + "");
	}

}
