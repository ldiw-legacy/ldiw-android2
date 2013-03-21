package com.letsdoitworld.wastemapper.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.letsdoitworld.wastemapper.R;
import com.letsdoitworld.wastemapper.controllers.AsyncTaskController;
import com.letsdoitworld.wastemapper.fragment.BadgesFragment;
import com.letsdoitworld.wastemapper.fragment.MapFragment;
import com.letsdoitworld.wastemapper.fragment.PointsListFragment;
import com.letsdoitworld.wastemapper.fragment.PreferencesFragment;
import com.letsdoitworld.wastemapper.fragment.ProfileFragment;
import com.letsdoitworld.wastemapper.fragment.ShowPictureFragment;
import com.letsdoitworld.wastemapper.fragment.ShowPointFragment;
import com.letsdoitworld.wastemapper.utils.Utils;
import com.nineoldandroids.animation.ObjectAnimator;


/**
 * Tab Activity
 *  Load the tabs
 *  This is the main Parent Activity
 * @author appytech
 *
 */
public class MainActivity extends BasicActivity {

	  public static final int REQUEST_CODE_CAMERA = 12;
	  public static final int REQUEST_CODE_GALLERY = 13;

	
	public final static String TAG = "MainActivity";
	private Uri imageUri;

	private Fragment currentFragment;   
	
	private LinearLayout container, menu;
	boolean isMenuShown = false;
	boolean isOnChildView = false;
	boolean isOnPictureView = false;

	private TextView mMenuNearby, mMenuFriends, mMenuMapView, mMenuMyProfile, mMenuPref;
	
	private PointsListFragment menu1;
	private MapFragment menu3;
	private ProfileFragment menu4;
	private PreferencesFragment menu5;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
				
		this.setContentView(R.layout.main);
        
		
		 // set defaults for logo & home up 
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setDisplayUseLogoEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setTitle(getString(R.string.nearby));//(R.drawable.letsdoit_main);
        mActionBar.setIcon(R.drawable.menu_icon);
        mActionBar.setHomeButtonEnabled(true);
        
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_USE_LOGO|ActionBar.DISPLAY_SHOW_HOME);

        
        
        menu1 = new PointsListFragment();
        
        menu3 = new MapFragment();
        
        menu4 = new ProfileFragment();
        
        menu5 = new PreferencesFragment();
        
        // set up tabs nav
       // mActionBar.addTab(mActionBar.newTab().setText(getString(R.string.tab_actvities)).setTabListener(new MyTabsListener(new PointsListActivity())));
        //mActionBar.addTab(mActionBar.newTab().setText(getString(R.string.tab_map)).setTabListener(new MyTabsListener(new MapFragment())));
       // mActionBar.addTab(mActionBar.newTab().setText(getString(R.string.tab_my_account)).setTabListener(new MyTabsListener(new MapFragment())));
       
    	 // default to tab navigation
        //showTabsNav();
        
        menu = (LinearLayout) findViewById(R.id.menu_container);
        container = (LinearLayout) findViewById(R.id.fragment_container); 
        
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, menu1);
        ft.commit();
        
        currentFragment = menu1;
        
        mMenuNearby = (TextView) findViewById(R.id.menu1);				
        mMenuNearby.setBackgroundColor(mContext.getResources().getColor(R.color.letsdoitgreen));

       // mMenuFriends = (TextView) findViewById(R.id.menu2);
        mMenuMapView = (TextView) findViewById(R.id.menu3);
        mMenuMyProfile = (TextView) findViewById(R.id.menu4);
        mMenuPref = (TextView) findViewById(R.id.menu5);
		setListeners();

	}	 
	
	
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        invalidateOptionsMenu(); 
	}




	private void showTabsNav() {
		ActionBar ab = getSupportActionBar();
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		/*if (ab.getNavigationMode() != ActionBar.NAVIGATION_MODE_STANDARD) {
			ab.setDisplayShowTitleEnabled(false);
			ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}*/
	}
	
	/*private void hideTabsBar() {
		ActionBar ab = getSupportActionBar();
		if (ab.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS) {
			//ab.setDisplayShowTitleEnabled(false);
			ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
	}*/
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		
		if (mWastePointController.getNbSavedWastePoints() > 0) {
		
			MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.offline, menu);
		} else {
		   SubMenu sub = menu.addSubMenu("").setIcon(R.drawable.button_add_point);
	       sub.add(0, R.id.item_take_picture, 0, getString(R.string.take_photo));
	       sub.add(0, R.id.item_from_gallery, 0, getString(R.string.choose_from_gallery));
	       sub.add(0, R.id.item_skip_picture, 0, getString(R.string.skip_photo));
	       sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	       
		}
		
        if (isOnChildView) {
        
	        SubMenu sub2 = menu.addSubMenu("").setIcon(R.drawable.button_more);
	        sub2.add(0, R.string.clean, 0, getString(R.string.clean));
	        sub2.add(0, R.string.confirm, 0, getString(R.string.confirm));
	        sub2.add(0, R.string.report, 0, getString(R.string.report));
	        sub2.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        }
 
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println("onOptionsItemSelected:"+item.getTitle()+", "+item.getItemId());
		
		switch (item.getItemId()) {
		case android.R.id.home:
			
			if (isOnChildView) {
				if (isOnPictureView)  {
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
					ShowPointFragment frag = new ShowPointFragment();
					ft.replace(R.id.fragment_container, frag);
					ft.commit();
					isOnPictureView = false;
				} else {
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		       		ft.replace(R.id.fragment_container, currentFragment);
		       		ft.commit();
		       		isOnChildView = false;
		            mActionBar.setIcon(R.drawable.menu_icon);
		            invalidateOptionsMenu();
				}
				
					
				
			} else {
				if (isMenuShown)closeMenu();
				else openMenu();
			}
			
			
			break;
		case R.id.item_skip_picture:
			Intent intent = new Intent(this, NewPointActivity.class);
			startActivity(intent);
			break;
		case R.id.item_take_picture:
			String fileName = "new-photo-name.jpg";
		    ContentValues values = new ContentValues();
		    values.put(MediaStore.Images.Media.TITLE, fileName);
		    values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
		    imageUri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		    Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		    intentPicture.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		    startActivityForResult(intentPicture, REQUEST_CODE_CAMERA);
		    break;
		case R.id.item_from_gallery:
			Intent intentGallery = new Intent();
			intentGallery.setType("image/*");
			intentGallery.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intentGallery, "Select Picture"), REQUEST_CODE_GALLERY);
			break;
		
		case R.string.clean:
			//TODO something
			break;
		case R.string.confirm:
			manager.launchTask(AsyncTaskController.MODIFY_CONFIRM_WASTE_POINT, true);
			break;
			
		case R.string.report:
			//TODO
			break;
		case R.id.offline_item:
			//TODO
			System.out.println("AAAAAAAAAAAAAAAAAAHHH CLICK ? ");
			break;
		default:
			break;
		}
		return true;
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		if(intent.hasExtra("SEND_OFFLINE")){
			manager.launchTask(AsyncTaskController.SEND_OFFLINE_WASTE_POINTS, true);
		}
		
	}

	public static Boolean hasPhoneConnection(Context c) {
		
	    ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);  
	    if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
	    	return true;
	    } else if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
	    	return true;
	    } else {
	    	return false;
	    }
	}


	@Override
	protected void setListeners() {
		 
		
		mMenuNearby.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mActionBar.setTitle(getString(R.string.nearby));
				mMenuNearby.setBackgroundColor(mContext.getResources().getColor(R.color.letsdoitgreen));
				//mMenuFriends.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				mMenuMapView.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				mMenuMyProfile.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				mMenuPref.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));

				closeMenu();
				if (currentFragment != menu1) {
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		       		ft.replace(R.id.fragment_container, menu1);
		       		ft.commit();
		       		currentFragment = menu1;
				}
			}
		});
	       
		/*mMenuFriends.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View arg0) {
				mActionBar.setTitle(getString(R.string.friends));

				mMenuNearby.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				mMenuFriends.setBackgroundColor(mContext.getResources().getColor(R.color.letsdoitgreen));
				mMenuMapView.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				mMenuMyProfile.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				mMenuPref.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));

				closeMenu();


			}
		});*/
		
        
		mMenuMapView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				 mActionBar.setTitle(getString(R.string.map_view));

				
				mMenuNearby.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				//mMenuFriends.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				mMenuMapView.setBackgroundColor(mContext.getResources().getColor(R.color.letsdoitgreen));
				mMenuMyProfile.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				mMenuPref.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));

				closeMenu();

				if (currentFragment != menu3) {
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		       		ft.replace(R.id.fragment_container, menu3);
		       		ft.commit();
		       		currentFragment = menu3;

				}
			}
		});
		
		mMenuMyProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				mActionBar.setTitle(getString(R.string.my_profile));

				mMenuNearby.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				//mMenuFriends.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				mMenuMapView.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				mMenuMyProfile.setBackgroundColor(mContext.getResources().getColor(R.color.letsdoitgreen));
				mMenuPref.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));

				closeMenu();

				if (currentFragment != menu4) {
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		       		ft.replace(R.id.fragment_container, menu4);
		       		ft.commit();
		       		currentFragment = menu4;

				}
			}
		});
		
		
		mMenuPref.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mActionBar.setTitle(getString(R.string.settings));

				mMenuNearby.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				//mMenuFriends.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				mMenuMapView.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				mMenuMyProfile.setBackgroundColor(mContext.getResources().getColor(R.color.menu_background_unselected));
				mMenuPref.setBackgroundColor(mContext.getResources().getColor(R.color.letsdoitgreen));

				closeMenu();

				if (currentFragment != menu5) {
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		       		ft.replace(R.id.fragment_container, menu5);
		       		ft.commit();
		       		currentFragment = menu5;

				}
				
			}
		});
		
		
	}
	
	class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;
		 
		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}
		 
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			Toast.makeText(mContext, "Reselected!", Toast.LENGTH_LONG).show();
		}
		 
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			currentFragment = fragment;
			ft.replace(R.id.fragment_container, fragment);
		}
		 
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			//ft.remove(fragment);
		}
		 
	}
	
	
	 @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   if (requestCode == REQUEST_CODE_CAMERA) {
	      if (resultCode == Activity.RESULT_OK) {
	        try {
	        	
	        	Intent intent = new Intent(this, NewPointActivity.class);
	        	intent.putExtra("imageUri", imageUri);
				startActivity(intent);
	        
	        } catch (Exception e) {
	        }

	      }
	    } else if (requestCode == REQUEST_CODE_GALLERY) {
		      if (resultCode == Activity.RESULT_OK) {
			        try {
			        	
			        	Intent intent = new Intent(this, NewPointActivity.class);
			        	intent.putExtra("imageUri", data.getData());
						startActivity(intent);
			        
			        } catch (Exception e) {
			        }

			      }
			    }
	    super.onActivityResult(requestCode, resultCode, data);
	 }
	 
	 public void showPointDetails() {
		 
		 
		 isOnChildView = true;
		 
		 mActionBar.setTitle(getString(R.string.Waste_spot));
		 mActionBar.setIcon(R.drawable.navigation_previous_item);
		 
		 
		 FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		 ShowPointFragment frag = new ShowPointFragment();
		 ft.replace(R.id.fragment_container, frag);
		 ft.commit();
		 
         invalidateOptionsMenu();

	 }
	 
	 
	 public void showPicture() {
		 
		 isOnPictureView  = true;
		 mActionBar.setTitle(getString(R.string.picture));
		 mActionBar.setIcon(R.drawable.navigation_previous_item);
		 
		 
		 FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		 ShowPictureFragment frag = new ShowPictureFragment();
		 ft.replace(R.id.fragment_container, frag);
		 ft.commit();
		 
         invalidateOptionsMenu(); 

	 }
	 
	 
	 public void showBadges() {
		 
		 isOnChildView = true;
		 mActionBar.setTitle(getString(R.string.badges));
		 mActionBar.setIcon(R.drawable.navigation_previous_item);
		 
		 
		 FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		 BadgesFragment frag = new BadgesFragment();
		 ft.replace(R.id.fragment_container, frag);
		 ft.commit();
		 
         invalidateOptionsMenu(); 

	 }
	 
	 
	 public void openMenu() {

		 isMenuShown = true;
		 
		 float density = getResources().getDisplayMetrics().density;
		 
		 ObjectAnimator anim = ObjectAnimator.ofFloat(menu, "translationX", -(250*density),0);
		 ObjectAnimator anim2 = ObjectAnimator.ofFloat(container, "translationX", 0,250*density);

		 anim.setInterpolator(new AccelerateDecelerateInterpolator());
		 anim2.setInterpolator(new AccelerateDecelerateInterpolator());

		 anim.start();
		 anim2.start();
		 menu.setVisibility(View.VISIBLE);
		 	 
	 }
	 
	 public void closeMenu() {

		 float density = getResources().getDisplayMetrics().density;
	
		 isMenuShown = false;
	
		 ObjectAnimator anim = ObjectAnimator.ofFloat(container, "translationX",(250*density),0);
		 ObjectAnimator anim2 = ObjectAnimator.ofFloat(menu, "translationX", 0, -250*density);
		 anim.setInterpolator(new AccelerateDecelerateInterpolator());
		 anim2.setInterpolator(new AccelerateDecelerateInterpolator());
		 anim.start();
		 anim2.start();
		 menu.setVisibility(View.VISIBLE);
	 }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		if (isOnChildView) {
			
			if(isOnPictureView) {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ShowPointFragment frag = new ShowPointFragment();
				 ft.replace(R.id.fragment_container, frag);
				 ft.commit();
				 isOnPictureView = false;
			} else {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	       		ft.replace(R.id.fragment_container, currentFragment);
	       		ft.commit();
	       		isOnChildView = false;
	            mActionBar.setIcon(R.drawable.menu_icon);
	            mActionBar.setTitle(getString(R.string.map_view));
	    		invalidateOptionsMenu();
			}

		} else {
			
			super.onBackPressed();

		}
		
	}

	@Override
	public void handleSuccessTask(int type) {
		cancelDialog();
		switch (type) {
		case AsyncTaskController.MODIFY_CONFIRM_WASTE_POINT:
			mWastePointController.restWastePointsList();
			Utils.showMsg(this, "TODO confirm ok");
			break;
		case AsyncTaskController.SEND_OFFLINE_WASTE_POINTS:
			invalidateOptionsMenu();
			Utils.showMsg(this, "TODO offline points send ok");
			break;

		default:
			break;
		}
		
	}

	@Override
	public void handleFailedTask(int type, String error) {
		cancelDialog();
		switch (type) {
		case AsyncTaskController.MODIFY_CONFIRM_WASTE_POINT:
			Utils.showMsg(this, error);
			break;
		case AsyncTaskController.SEND_OFFLINE_WASTE_POINTS:
			Utils.showMsg(this, error);
			break;
		default:
			break;
		}
		
	}
	 

	
}
