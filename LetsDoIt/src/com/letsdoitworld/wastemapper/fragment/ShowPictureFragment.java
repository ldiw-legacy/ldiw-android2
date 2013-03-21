package com.letsdoitworld.wastemapper.fragment;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.letsdoitworld.wastemapper.R;
import com.letsdoitworld.wastemapper.controllers.WastePointController;
import com.letsdoitworld.wastemapper.objects.WastePoint;
import com.letsdoitworld.wastemapper.utils.ImageDownloader;

public class ShowPictureFragment extends Fragment {

  public static final int REQUEST_CODE_MAP = 22;

  private Context mContext;
  private File photoFile;
  private View mainView;

  
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
	  
       mainView = inflater.inflate(R.layout.picture_fragment, container, false);
      return mainView;
  }


  
  
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);
    
    getImageFromURl();
    
   
  }
	@Override
	public void onAttach(Activity activity) {
	// TODO Auto-generated method stub
		super.onAttach(activity);
	    currentPoint = WastePointController.getInstance(getActivity()).getCurrentWastePoint();

	}
	
	public void getImageFromURl() {
	
		try {
			String photo = currentPoint.getPhotos().substring(0, currentPoint.getPhotos().indexOf(":"));
			
			String url = "http://api.letsdoitworld.org/ldiw_waste_map/photo/"+currentPoint.getId()+"/" + photo;
			
			ImageDownloader imgDownloader = new ImageDownloader(mContext);
			imgDownloader.download(url, (ImageView) mainView.findViewById(R.id.photo));
		} catch (Exception e) {
			mainView.findViewById(R.id.progress).setVisibility(View.INVISIBLE);
			
		}
		
	}

}
