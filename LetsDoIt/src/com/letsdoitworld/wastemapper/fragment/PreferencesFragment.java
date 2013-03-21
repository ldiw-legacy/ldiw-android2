package com.letsdoitworld.wastemapper.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letsdoitworld.wastemapper.R;

public class PreferencesFragment extends BasicFragment {

     
	private View mainView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
   
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = activity;
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		 mainView = inflater.inflate(R.layout.pref_frag, container, false);

		
		 
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
		// TODO Auto-generated method stub
		
	}
	  
  
  
}
