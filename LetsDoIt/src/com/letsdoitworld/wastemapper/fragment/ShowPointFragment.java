package com.letsdoitworld.wastemapper.fragment;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.letsdoitworld.wastemapper.controllers.WastePointController;
import com.letsdoitworld.wastemapper.objects.ExtraField;
import com.letsdoitworld.wastemapper.objects.WastePoint;
import com.letsdoitworld.wastemapper.utils.ImageDownloader;

public class ShowPointFragment extends Fragment {

  public static final int REQUEST_CODE_MAP = 22;
  protected static final String TAG = "NewPointActivity";

  private Context mContext;
  private File photoFile;
  private View mainView;
  private MapView mapView;
  private GoogleMap mMap;
  private TextView idView, plastic, glass, paper, xxl;
  private LinearLayout valuesContainer;
  private ImageView mPictureView;

  
  public WastePoint currentPoint;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    
    if (photoFile == null) {
      //startCameraActivity();
    }
    
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.new_point);
    mContext = getActivity();

  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {
	  
       mainView = inflater.inflate(R.layout.point_details, container, false);
       mapView = (MapView) mainView.findViewById(R.id.mapview);
       idView = (TextView) mainView.findViewById(R.id.id_point);
       valuesContainer = (LinearLayout) mainView.findViewById(R.id.linear_values);
       plastic= (TextView) mainView.findViewById(R.id.composition_item_plastic_value);
       glass= (TextView) mainView.findViewById(R.id.composition_item_glass_value);
       paper= (TextView) mainView.findViewById(R.id.composition_item_paper_value);
       xxl= (TextView) mainView.findViewById(R.id.composition_item_xxl_value);
       mPictureView = (ImageView) mainView.findViewById(R.id.photo);
       
 //initMap();
      
      /*map = new ArrayList<FieldInfo>();
      fieldSets = new HashMap<String, Set<String>>();


      viewGroup = (ViewGroup) mainView.findViewById(R.id.container);

      sendData = (Button) mainView.findViewById(R.id.confirm);
      
      furtherDetailsWrapper = (LinearLayout) mainView.findViewById(R.id.further_details_wrapper);
      
      progress = new ProgressDialog(mContext);
      progress.setIndeterminate(true);
      String wait = getResources().getString(R.string.message_adding);
      progress.setMessage(wait);
      progress.setCancelable(false);
      
      */
      return mainView;
  }
  
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);
    mapView.onCreate(savedInstanceState);
    mapView.getMap().getUiSettings().setZoomControlsEnabled(false);
    mapView.getMap().getUiSettings().setAllGesturesEnabled(false);
    

    getImageFromURl();
    
    try {
		MapsInitializer.initialize(mContext);
	} catch (GooglePlayServicesNotAvailableException e) {
		e.printStackTrace();
	}
     
    System.out.println("AAAA onVIew Created");
    
    if (currentPoint == null) {
    	System.out.println("CurrentPoint is NULL ... but why ? ");
    	currentPoint = WastePointController.getInstance(mContext).getCurrentWastePoint();
    }
    
    idView.setText("ID: "+ currentPoint.getId());
    fillView();
    fillComposition();
    
    /*if (photo != null) {
    	try {
		    ImageView imageView = (ImageView) mainView.findViewById(R.id.photo);
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
    
    setLocation();*/
    
    mPictureView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (currentPoint.getPhotos().length()>0)
			((MainActivity) getActivity()).showPicture();
		}
	});

  }
	@Override
	public void onAttach(Activity activity) {
	// TODO Auto-generated method stub
		super.onAttach(activity);
	    currentPoint = WastePointController.getInstance(getActivity()).getCurrentWastePoint();

		/*if (imageUri != null) {
			try {
				photo = convertImageUriToFile(imageUri, activity);
				if (photo != null)
					photoFile = photo;
				else
					photo = photoFile;
				
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("PROBLEME AVEC L'IMAGE");
			}
		}*/
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
		setLocation();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}


	private void setLocation() {
    	mMap = mapView.getMap();
    	
    	MarkerOptions maPos = new MarkerOptions();
    	
    	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(currentPoint.getLat()), Double.parseDouble(currentPoint.getLon())), 14));
		maPos.position(new LatLng(Double.parseDouble(currentPoint.getLat()), Double.parseDouble(currentPoint.getLon())));
        maPos.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_1));
        mMap.clear();
        mMap.addMarker(maPos);
	}

	
	private void fillView() {
		
		/*if(currentPoint.get.length() > 0) {
			setPointsInfos("Volume", currentPoint.getVolume());
		}*/
		
		for (ExtraField field : currentPoint.getExtraFields()) {
			  String key = field.getLabel();
			  String value = field.getValue();
			  // do stuff
			  if (value.length() > 0) setPointsInfos(key, value);
			  
		}
		
	}
	
	private void setPointsInfos(String sTitle, String sValue) {
		 
		View v = View.inflate(mContext, R.layout.point_item_layout, null);
		TextView title, value;
		title = (TextView) v.findViewById(R.id.item_title);
		value = (TextView) v.findViewById(R.id.item_value);
		
		title.setText(sTitle);
		value.setText(sValue);
		
		valuesContainer.addView(v);
	}
	
	private void fillComposition() {
		
		if (currentPoint.getComposition_pmp().length() >0) {
			plastic.setText(currentPoint.getComposition_pmp() + "%");

		} else {
			plastic.setText("0%");

		}
		
		
		if (currentPoint.getComposition_glass().length() >0) {
			glass.setText(currentPoint.getComposition_glass() + "%");

		} else {
			glass.setText("0%");

		}
		
		
		if (currentPoint.getComposition_paper().length() >0) {
			paper.setText(currentPoint.getComposition_paper() + "%");

		} else {
			paper.setText("0%");

		}
		
		
		if (currentPoint.getComposition_large().length() >0) {
			xxl.setText(currentPoint.getComposition_large() + "%");

		} else {
			xxl.setText("0%");

		}
		 
	}
	
	public void getImageFromURl() {
	
		try {
			String photo = currentPoint.getPhotos().substring(0, currentPoint.getPhotos().indexOf(":"));
			
			String url = "http://api.letsdoitworld.org/ldiw_waste_map/photo/"+currentPoint.getId()+"/" + photo;
			
			ImageDownloader imgDownloader = new ImageDownloader(mContext);
			imgDownloader.download(url, mPictureView);
		} catch (Exception e) {
			mainView.findViewById(R.id.progress).setVisibility(View.INVISIBLE);
			
		}
		
	}

}
