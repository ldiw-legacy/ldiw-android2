package com.letsdoitworld.wastemapper.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.letsdoitworld.wastemapper.R;
import com.letsdoitworld.wastemapper.activity.NewPointActivity;
import com.letsdoitworld.wastemapper.controllers.AsyncTaskController;

public class ChangePointMapFragment extends BasicMapFragment {

	 private GoogleMap mMap;
	 private LatLng mPosFija;

	 MarkerOptions maPos;
	 
	 private boolean isMapInitialized = false;
	 
    public ChangePointMapFragment() {
    	super();
    }

    public static ChangePointMapFragment newInstance(LatLng posicion) {
    	ChangePointMapFragment frag = new ChangePointMapFragment();
        frag.mPosFija = posicion;
        return frag;
    }

    @Override
    public GoogleMap getMap() {
        // TODO Auto-generated method stub
        return super.getMap();
    }

    @Override
    public void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        //initMap();
        return view;
    }

    
    
    @Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		initMap();
		
		if(isMapInitialized) {
			drawItemsOnMap();
		}
		
	}
    

	private void initMap() {
		
        maPos = new MarkerOptions();
				
		if (mLocationController.hasFoundLocation()) {
			
			if(mWastePointController.getWastePointsList() == null || mWastePointController.getWastePointsList().size() == 0){
				manager.launchTask(AsyncTaskController.GET_WASTE_POINTS, false);
			}else{
				handleSuccessTask(AsyncTaskController.GET_WASTE_POINTS);
			}
		}
        
    }

	public void drawItemsOnMap() {
	
		if (mLocationController.hasFoundLocation()) {
			getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocationController.getCurrentLocation().getLatitude(), mLocationController.getCurrentLocation().getLongitude()), 14));
			maPos.position(new LatLng(mLocationController.getCurrentLocation().getLatitude(), mLocationController.getCurrentLocation().getLongitude()));
	        maPos.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_1));
	        getMap().clear();
	        getMap().addMarker(maPos);
		
	        
	        maPos.draggable(true);

	        getMap().setOnMarkerDragListener(new OnMarkerDragListener() {
				
				@Override
				public void onMarkerDragStart(Marker arg0) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onMarkerDragEnd(Marker m) {
					((NewPointActivity) getActivity()).changeLatLon(m.getPosition().latitude, m.getPosition().longitude);					
				}
				
				@Override
				public void onMarkerDrag(Marker arg0) {
					// TODO Auto-generated method stub	
				}
			});
        
	        /*if(mWastePointController.getWastePointsList() != null) {
	        
		        for (WastePoint point :mWastePointController.getWastePointsList()) {
		        	
		        	MarkerOptions element = new MarkerOptions();
		        	
		        	element.position(new LatLng(point.getDoubleLat(), point.getDoubleLon()));
		        	element.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
		        	element.title("Item "+ point.getId());
		        	if (point.getExtraFields().size() > 0)	{
		        		if (point.getExtraValue("description").length() > 0)
		        			element.snippet(point.getExtraValue("description"));
		        	}
		        	getMap().addMarker(element);	    		        	
		        }
	        }
	        
	        getMap().setOnMarkerClickListener(new OnMarkerClickListener() {
				
				@Override
				public boolean onMarkerClick(Marker m) {
					// TODO Auto-generated method stub
					m.showInfoWindow();
					
					return true;
				}
			});
			
	        getMap().setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				
				@Override
				public void onInfoWindowClick(Marker m) {
					// TODO Auto-generated method stub
					((MainActivity)ChangePointMapFragment.this.getActivity()).showPointDetails();
					String s = m.getTitle().substring(5, m.getTitle().length());
					for (int i=0; i<mWastePointController.getWastePointsList().size(); i++) {
						if (mWastePointController.getWastePointsList().get(i).getId().equals(s)) {
							mWastePointController.setCurrentWastePoint(mWastePointController.getWastePointsList().get(i));
						}
					}
				}
			});*/

		}
	}
	
	@Override
	public void handleSuccessTask(int type) {
		if (type == AsyncTaskController.GET_WASTE_POINTS) {
			drawItemsOnMap();
		}
	}

	@Override
	public void handleFailedTask(int mType, String error) {
		if (mType == AsyncTaskController.GET_WASTE_POINTS) {
			getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocationController.getCurrentLocation().getLatitude(), mLocationController.getCurrentLocation().getLongitude()), 14));
			maPos.position(new LatLng(mLocationController.getCurrentLocation().getLatitude(), mLocationController.getCurrentLocation().getLongitude()));
	        maPos.icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
	        getMap().clear();
	        getMap().addMarker(maPos);
		}
	}
	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		
	}

}
	
