package com.letsdoitworld.wastemapper.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.letsdoitworld.wastemapper.R;
import com.letsdoitworld.wastemapper.activity.MainActivity;
import com.letsdoitworld.wastemapper.adapter.WastePointsAdapter;
import com.letsdoitworld.wastemapper.controllers.AsyncTaskController;

public class PointsListFragment extends BasicListFragment {

  private Context mContext;
  private ProgressDialog progress;
  private boolean done;
  private WastePointsAdapter pointArrayAdapter;
  private ListView list;
  private TextView emptyText;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    View rootView = inflater.inflate(R.layout.list, container, false);
    mContext = getActivity();
    emptyText = (TextView) rootView.findViewById(R.id.empty);
    return rootView; 
  }
  
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
  }


  @Override
  public void onResume() {
	  super.onResume();
	  if (mWastePointController.getWastePointsList() == null || mWastePointController.getWastePointsList().size()==0){
			manager.launchTask(AsyncTaskController.GET_WASTE_POINTS, false);
		} else {
			 initList();
		}
  }

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void handleSuccessTask(int type) {
		
		if (type == AsyncTaskController.GET_WASTE_POINTS) {
			//Load adapter here
			initList();
		}
		
	}


	private void initList() {
		// TODO Auto-generated method stub
		//pointArrayAdapter = new PointsArrayAdapter(mContext, resource, textViewResourceId, listener)
		
		pointArrayAdapter = new WastePointsAdapter(mContext, R.layout.list_feeds_item, mWastePointController.getWastePointsList());
		setListAdapter(pointArrayAdapter);
		try {
			getListView().setDivider(null);
			emptyText.setVisibility(View.GONE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				mWastePointController.setCurrentWastePoint(mWastePointController.getWastePointsList().get(arg2));
				((MainActivity)PointsListFragment.this.getActivity()).showPointDetails();

			}
		});
	}


	@Override
	public void handleFailedTask(int mType, String error) {
		System.out.println("FAILED WASTE ACTIVITY " + error);
		
		if (mType == AsyncTaskController.GET_WASTE_POINTS) {
			emptyText.setText("Problem when getting the waste point..");
			
		}
	}

}
