package com.letsdoitworld.wastemapper.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.letsdoitworld.wastemapper.R;
import com.letsdoitworld.wastemapper.adapter.BadgesAdapter;
import com.letsdoitworld.wastemapper.controllers.ConnectionController;
import com.letsdoitworld.wastemapper.objects.Badge;
import com.letsdoitworld.wastemapper.objects.User;

public class BadgesFragment extends Fragment {

     
	private View mainView;
    private GridView gridview;

	
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
		
		 mainView = inflater.inflate(R.layout.my_badges, container, false);

		 gridview = (GridView)mainView.findViewById(R.id.gridView);
		
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
		
		ArrayList<Badge> badges = new ArrayList<Badge>();
		
		for (int i=0; i<13; i++) {
			
			Badge b = new Badge();
			
			b.setName("International traveller");
			b.setImage(getResources().getDrawable( R.drawable.badges_bg_normal));		
			
			
			badges.add(b);
		}
		
		
		
		BadgesAdapter myAdapter = new BadgesAdapter(mContext, badges);
		gridview.setAdapter(myAdapter);
		
	}
	
}
