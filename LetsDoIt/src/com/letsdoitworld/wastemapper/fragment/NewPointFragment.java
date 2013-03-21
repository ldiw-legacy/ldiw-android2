package com.letsdoitworld.wastemapper.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.letsdoitworld.wastemapper.R;
import com.letsdoitworld.wastemapper.activity.MainActivity;
import com.letsdoitworld.wastemapper.activity.NewPointActivity;
import com.letsdoitworld.wastemapper.controllers.LocationController;
import com.letsdoitworld.wastemapper.controllers.WastePointController;
import com.letsdoitworld.wastemapper.objects.ExtraField;
import com.letsdoitworld.wastemapper.objects.WastePoint;
import com.letsdoitworld.wastemapper.utils.Utils;

public class NewPointFragment extends BasicFragment {

  public static final int REQUEST_CODE_MAP = 22;
  protected static final String TAG = "NewPointActivity";
  
  private int ID_REGISTRATION = 123456789;

  private LinearLayout layout;
  private Context mContext;
  private Uri imageUri;
  private File photoFile;
  private ProgressDialog progress;
  private View mainView;
  private MapView mapView;
  private GoogleMap mMap;
  
  private WastePointController pointController;
  private WastePoint currentWastePoint;
  
  private FrameLayout mapListener;

  File photo;
  private ImageView imageView;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.new_point);
    mContext = getActivity();

  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {
	  
       mainView = inflater.inflate(R.layout.new_point, container, false);
      //initMap();

      layout = (LinearLayout) mainView.findViewById(R.id.extra_field_containers);

      progress = new ProgressDialog(mContext);
      progress.setIndeterminate(true);
      String wait = getResources().getString(R.string.message_adding);
      progress.setMessage(wait);
      progress.setCancelable(false);
      
      mapView = (MapView) mainView.findViewById(R.id.mapview);
      mapListener = (FrameLayout) mainView.findViewById(R.id.frame_map_listener);
      return mainView;
  }


  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);
    mapView.onCreate(savedInstanceState);
    mapView.getMap().getUiSettings().setZoomControlsEnabled(false);
    mapView.getMap().getUiSettings().setAllGesturesEnabled(false);
   
    try {
		MapsInitializer.initialize(mContext);
	} catch (GooglePlayServicesNotAvailableException e) {
		e.printStackTrace();
	}
    
    imageView = (ImageView) mainView.findViewById(R.id.photo);
	
    if (photo != null) {
    	try {
    		Rect out = new Rect();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 12;
			Bitmap bitmapPhoto;
			
				bitmapPhoto = BitmapFactory.decodeStream(new FileInputStream(photo), out, options);
		
	
		imageView.setImageBitmap(bitmapPhoto);
	
		
		
		//switcher.showNext();
		//switcher.invalidate();
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    
    setLocation();
    
    setListeners();
    createFields();
 
  }
	@Override
	public void onAttach(Activity activity) {
	// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext= activity;
		pointController = WastePointController.getInstance(activity);
		currentWastePoint = pointController.getCurrentWastePoint();
		 
		if (imageUri != null) {
			try {
				photo = Utils.convertImageUriToFile(imageUri, activity);
				if (photo != null)
					photoFile = photo;
				else 
					photo = photoFile;
				
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("PROBLEME AVEC L'IMAGE");
			}
		}
	}

	public void setImageUri(Uri imageUri) {
		this.imageUri = imageUri;

	}
  
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	private void startCameraActivity() {
	    String fileName = "new-photo-name.jpg";
	    ContentValues values = new ContentValues();
	    values.put(MediaStore.Images.Media.TITLE, fileName);
	    values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
	    imageUri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
	    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
	    startActivityForResult(intent, MainActivity.REQUEST_CODE_CAMERA);
	}



	private void setLocation() {
   
		mMap = mapView.getMap();
    	
    	LocationController mLocController = LocationController.getInstance();
    	MarkerOptions maPos = new MarkerOptions();
    	
    	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(currentWastePoint.getLat()), Double.parseDouble(currentWastePoint.getLon())), 14));
    	
		maPos.position(new LatLng(Double.parseDouble(currentWastePoint.getLat()), Double.parseDouble(currentWastePoint.getLon())));
        maPos.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_1));
        mMap.clear();
        mMap.addMarker(maPos);
         
	}
	
	
	private void createFields() {
				
		System.out.println("AAAAAA " + pointController.getExtraFields().size());
		for (int index=0; index<pointController.getExtraFields().size(); index++) {
		
			//We don't want the composition in the extra field class :) 
			
			ExtraField f = pointController.getExtraFields().get(index);
			final int index2 = index;
			View v = View.inflate(mContext, R.layout.item_new_point, null);
			TextView title;
			final EditText value;
			title = (TextView) v.findViewById(R.id.text_title);
			value = (EditText) v.findViewById(R.id.text_value);
			
			title.setText(f.getLabel());
			value.setHint(f.getEdit_instructions());
			value.setTextColor(Color.BLACK);
			
			if (f.getAllowed_values().size() > 0) { 
				
				for (int i=0; i<f.getAllowed_values().size(); i++){
					if (f.getAllowed_values().keySet().toArray()[i].equals(currentWastePoint.getExtraFields().get(index2).getValue())) {
						value.setText(""+f.getAllowed_values().values().toArray()[i]);
					}
				}

			} else if (f.getTypical_values().size() > 0) {
				for (int i=0; i<f.getTypical_values().size(); i++){
					if (f.getTypical_values().keySet().toArray()[i].equals(currentWastePoint.getExtraFields().get(index2).getValue())) {
						value.setText(""+f.getTypical_values().values().toArray()[i]);
					}
				}
			} else {
				value.setText(currentWastePoint.getExtraFields().get(index2).getValue());
			}
			
			if (f.getType().equals("integer") || f.getType().equals("float")) {
				value.setInputType(InputType.TYPE_CLASS_NUMBER);
			} else { 
				value.setInputType(InputType.TYPE_CLASS_TEXT);
			}
			
			
			
			value.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					currentWastePoint.getExtraFields().get(index2).setValue(arg0.toString());
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
				
				@Override
				public void afterTextChanged(Editable arg0) {
					
					
				}
			});
			
			
			layout.addView(v);

			if (f.getTypical_values().size() > 0) {
				
				final RadioButton[] rb = new RadioButton[f.getTypical_values().size()];
			    final RadioGroup rg = new RadioGroup(mContext); //create the RadioGroup
			    rg.setOrientation(RadioGroup.VERTICAL);
			    
			    Object[] values = (Object[]) f.getTypical_values().values().toArray();
			    Object[] keys = (Object[]) f.getTypical_values().keySet().toArray();
			    
			    final String[] stringArray = Arrays.asList(values).toArray(new String[values.length]);
			    final String[] keyStringArray = Arrays.asList(keys).toArray(new String[keys.length]);
			    for(int i=0; i<f.getTypical_values().size(); i++){
			        rb[i]  = new RadioButton(mContext);
			        rg.addView(rb[i]);
			        rb[i].setText(stringArray[i]);
			    			        
			        rb[i].setId(100+i);
			    }
			    
			    rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						value.setText(stringArray[checkedId-100]);
						currentWastePoint.getExtraFields().get(index2).setValue(keyStringArray[checkedId-100]);		
					}
				});
			    
			 	value.setEnabled(false);

			    layout.addView(rg);
			} else if (f.getAllowed_values().size() > 0) {
				
				final RadioButton[] rb = new RadioButton[f.getAllowed_values().size()];
			    RadioGroup rg = new RadioGroup(mContext);
			    rg.setOrientation(RadioGroup.VERTICAL);
			    
			    Object[] values = (Object[]) f.getAllowed_values().values().toArray();
			 	Object[] keys = (Object[]) f.getAllowed_values().keySet().toArray();
			    
			 	value.setEnabled(false);
			 	
			    final String[] stringArray = Arrays.asList(values).toArray(new String[values.length]);
			    final String[] keyStringArray = Arrays.asList(keys).toArray(new String[keys.length]);
			    for(int i=0; i<f.getAllowed_values().size(); i++){
			        rb[i]  = new RadioButton(mContext);
			        rg.addView(rb[i]);
			        rb[i].setText(stringArray[i]);
			        rb[i].setId(i);	        
			    }
			    
			    rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						value.setText(stringArray[checkedId]);
						currentWastePoint.getExtraFields().get(index2).setValue(keyStringArray[checkedId]);		

					}
				});
			    
			    
			    layout.addView(rg);
			} 	
		}
	}

	@Override
	public void handleSuccessTask(int type) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void handleFailedTask(int mType, String error) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void setListeners() {
		mapListener.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((NewPointActivity) getActivity()).changeMapPosition();
			}
		});		
			
	}

 /* @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      createExitDialog();
      return true;
    } else
      return super.onKeyDown(keyCode, event);
  }*/

}
