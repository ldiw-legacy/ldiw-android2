package com.letsdoitworld.wastemapper.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.letsdoitworld.wastemapper.R;
import com.letsdoitworld.wastemapper.activity.MainActivity;
import com.letsdoitworld.wastemapper.controllers.ConnectionController;
import com.letsdoitworld.wastemapper.objects.User;
import com.letsdoitworld.wastemapper.utils.ImageDownloader;

public class ProfileFragment extends Fragment {

     
	private View mainView;
	private TextView nameText, adressText, numberPointText, numberBadgesText;
	private ImageView userProfilePicture;	
	private Button myBadges;
	
	private User currentUser;
	
	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
   
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = activity;
		 currentUser = ConnectionController.getInstance(activity).getCurrentUser();

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		 mainView = inflater.inflate(R.layout.my_profile, container, false);

		 
		 nameText = (TextView) mainView.findViewById(R.id.name_text);
		 adressText = (TextView) mainView.findViewById(R.id.adress_text);
		 numberPointText = (TextView) mainView.findViewById(R.id.number_points_text);
		 numberBadgesText = (TextView) mainView.findViewById(R.id.badges_number);
		 
		 userProfilePicture = (ImageView) mainView.findViewById(R.id.image_profile_picture);
		 
		 myBadges = (Button) mainView.findViewById(R.id.badge_picture);
		 
		 return mainView;
			
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		
		nameText.setText(currentUser.getUsername().toString().toUpperCase());
		adressText.setText(currentUser.getMail().toString().toUpperCase());
		
		ImageDownloader downloader = new ImageDownloader(mContext);
				
		if (!currentUser.getPicture().equals("null") && currentUser.getPicture().length()>0)
		downloader.download("http://letsdoitworld.org/"+currentUser.getPicture(), userProfilePicture);
		
		myBadges.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((MainActivity) getActivity()).showBadges();
			}
		});
		
	}
	  
  
  
}
