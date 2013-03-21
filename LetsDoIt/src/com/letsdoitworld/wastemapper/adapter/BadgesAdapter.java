package com.letsdoitworld.wastemapper.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.letsdoitworld.wastemapper.R;
import com.letsdoitworld.wastemapper.objects.Badge;

public class BadgesAdapter extends ArrayAdapter<Badge> implements Filterable{

	public  ArrayList<Badge> items;
    private Context context;
   
    public BadgesAdapter(Context context, ArrayList<Badge> items) {
            super(context, 0, items);
            this.items = items;
            
            this.context = context;
                
        }

       
        @Override
        public int getCount() {
        	if (items == null) {
        		return 0;
        	}
            return items.size();
          }
        
        public Badge getItem(int position) {
            return items.get(position);
        }

        public long getItemId(int position) {
            return items.get(position).hashCode();
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);

            ViewHolder holder;
            
        	convertView = inflater.inflate(R.layout.badge_item, parent, false);
        	
        	holder = new ViewHolder();
        	holder.badgePicture = (ImageView) convertView.findViewById(R.id.item_img_badge);
        	holder.badgeTitle = (TextView) convertView.findViewById(R.id.item_badge_value);
        	
        	
        	holder.badgePicture.setImageDrawable(items.get(position).getImage());
        	holder.badgeTitle.setText(""+items.get(position).getName());
        	
            return convertView;	      
        }
    
  
        
        static class ViewHolder {
        	
        	ImageView badgePicture ;
            TextView badgeTitle;
            
            
            boolean hasImage = false;
        }
        
}	