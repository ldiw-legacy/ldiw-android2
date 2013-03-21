package com.letsdoitworld.wastemapper.adapter;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.letsdoitworld.wastemapper.R;
import com.letsdoitworld.wastemapper.objects.WastePoint;

public class WastePointsAdapter extends ArrayAdapter<WastePoint> implements Filterable{

	public  ArrayList<WastePoint> items;
    private Context context;
    private int layout;
   
    

    public WastePointsAdapter(Context context, int textViewResourceId, ArrayList<WastePoint> items) {
            super(context, textViewResourceId, items);
            this.items = items;
            
            this.context = context;
            layout = textViewResourceId;
            // currencyController = CurrencyController.getInstance(context);
            // propertyTypeController = PropertyTypeController.getInstance(context);
                
        }

       
        @Override
        public int getCount() {
        	if (items == null) {
        		return 0;
        	}
            return items.size();
          }
        
        public WastePoint getItem(int position) {
            return items.get(position);
        }

        public long getItemId(int position) {
            return items.get(position).hashCode();
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final int positionNb = position;
            ViewHolder holder;
            
        	convertView = inflater.inflate(layout, parent, false);
        	
        	holder = new ViewHolder();
        	holder.itemTextFeedValue = (TextView) convertView.findViewById(R.id.item_text_feed_value);
        	holder.pointDistance = (TextView) convertView.findViewById(R.id.item_feed_time_and_distance);
        	holder.commentLayout = (LinearLayout) convertView.findViewById(R.id.item_linear_comment);
        	holder.comment= (ImageView) convertView.findViewById(R.id.item_img_comment);
        	holder.like= (ImageView) convertView.findViewById(R.id.item_img_like);
        	holder.picture = (ImageView) convertView.findViewById(R.id.item_img_profile);
        	holder.type = (ImageView) convertView.findViewById(R.id.item_img_type);
        	
        	        	
        	//holder.itemTextFeedValue.setText(items.get(position).getId());
        	
        	
        	
        	/*Random r = new Random();
        
        	int typeElemt = r.nextInt(2);
        	int i1=r.nextInt(3);
        	*/
        	//switch (typeElemt) {
			//case 0: // Add new point / Confirm new point
				
	        		holder.itemTextFeedValue.setText("ID = " +items.get(position).getId());
	           
	        	try {
	        		holder.pointDistance.setText(String.format("%.2f", (Double.parseDouble(items.get(position).getDistance_meters())/ 1000)) + "km from here");
	        	} catch (NumberFormatException e) {
	        		holder.pointDistance.setText(items.get(position).getDistance_meters());
	        	}
	        	
	        	//holder.type.setVisibility(View.VISIBLE);
	        	
				
		//		break;

			/*case 1: // Joined the place
				
				holder.itemTextFeedValue.setText("NAME joined Let\'s do it!");
	          
	        	holder.pointDistance.setText("x time ago");
	        	holder.type.setVisibility(View.GONE);
	        	
				
				break;
			default:
				break;
			}*/
        	
        	/*for (int i = 0; i<i1; i++) {
        		View v = View.inflate(context, R.layout.item_comment, null);
    			TextView comment;
    			ImageView img;
    			comment = (TextView) v.findViewById(R.id.text_comment);
    			img = (ImageView) v.findViewById(R.id.image_comment);
            	
    			img.setImageResource(R.drawable.badge_smallwhite);
    			comment.setText("This is the "+ (i+1) +" comment for item " + items.get(position).getId());
    			
    			holder.commentLayout.addView(v);
        	}*/
        	
        	
        	/*holder.comment.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("AAAAAAAAAAAA COMMENT ITEM " + items.get(positionNb).getId());
				}
			});
        	
        	holder.like.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("AAAAAAAAAAAA LIKE ITEM " + items.get(positionNb).getId());
				}
			});*/
        	
            return convertView;	      
        }
    
  
        
        static class ViewHolder {
        	
        	TextView itemTextFeedValue;
            TextView pointDistance ;
            LinearLayout commentLayout;
            ImageView comment, like;
            ImageView picture, type;
            
            
            boolean hasImage = false;
        }
        
}	