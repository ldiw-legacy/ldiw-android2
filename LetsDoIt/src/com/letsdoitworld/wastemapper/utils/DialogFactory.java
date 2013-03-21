package com.letsdoitworld.wastemapper.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.letsdoitworld.wastemapper.R;
/**
 * Util class for dialog creation
 *
 */
public class DialogFactory {

	
	public static Dialog getSuccessAddPoint(final Context context, OnClickListener l) {
		
		final Dialog alert = new Dialog(context);
		alert.requestWindowFeature(Window.FEATURE_NO_TITLE); 

		
		// Set an EditText view to get user input 
		LayoutInflater li = LayoutInflater.from(context);
		RelativeLayout someLayout = (RelativeLayout)li.inflate(R.layout.custom_pop_up, null);
		
		//final TextView text = (TextView) someLayout.findViewById(R.id.custom_service_text_view);
		//final CheckBox check1 = (CheckBox) someLayout.findViewById(R.id.checkbox_do_not_show_again);
		
		Button button = (Button) someLayout.findViewById(R.id.button_pop_up_continue);
		
		button.setOnClickListener(l);
		
		alert.setContentView(someLayout);

		//text.setText(context.getString(R.string.dialog_customer_support));

		
		
		return alert;
	}
	
	public static Dialog getFailedAddPoint(final Context context, OnClickListener l) {
		
		final Dialog alert = new Dialog(context);
		alert.requestWindowFeature(Window.FEATURE_NO_TITLE); 

		
		// Set an EditText view to get user input 
		LayoutInflater li = LayoutInflater.from(context);
		RelativeLayout someLayout = (RelativeLayout)li.inflate(R.layout.custom_pop_up, null);
		
		final TextView title = (TextView) someLayout.findViewById(R.id.title_popup);
		final TextView text = (TextView) someLayout.findViewById(R.id.popup_text1);
		
		title.setText(context.getString(R.string.failed));
		text.setText(context.getString(R.string.failed_waste_point_added));
		
		//final CheckBox check1 = (CheckBox) someLayout.findViewById(R.id.checkbox_do_not_show_again);
		
		Button button = (Button) someLayout.findViewById(R.id.button_pop_up_continue);
		
		button.setOnClickListener(l);
		
		alert.setContentView(someLayout);

		//text.setText(context.getString(R.string.dialog_customer_support));

		
		
		return alert;
	}
}
