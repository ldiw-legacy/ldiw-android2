package com.letsdoitworld.wastemapper.provider;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.view.ActionProvider;
import com.letsdoitworld.wastemapper.R;
import com.letsdoitworld.wastemapper.activity.MainActivity;
import com.letsdoitworld.wastemapper.controllers.WastePointController;

public class UploadOfflinePoints extends ActionProvider {

	Context mContext;

	
	public UploadOfflinePoints(Context context) {
		super(context);
        mContext = context;
	}
 
    @Override
    public View onCreateActionView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.offline_action_bar_item,null);
        
        TextView number = (TextView) view.findViewById(R.id.item_offline_value);
        
        number.setText(""+ WastePointController.getInstance(mContext).getNbSavedWastePoints());
        
        view.findViewById(R.id.item_offline_icon).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, MainActivity.class);
				intent.putExtra("SEND_OFFLINE", true);
				mContext.startActivity(intent);
				//Toast.makeText(mContext, "Click on upload offline wastepoints", Toast.LENGTH_LONG).show();
			}
		});
        
        return view;
    }
}
