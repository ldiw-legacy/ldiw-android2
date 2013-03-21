package com.letsdoitworld.wastemapper.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Utils {
	
	/**
	 * Format Long number stocked as String to be readable by user
	 * @param number without format (ex=100000000)
	 * @return formated number (ex=10 000 000 or 10,000,000)
	 */
	public static String formatNumbers(String number){
		
		String ret = "";
		
		if(!TextUtils.isEmpty(number)){
			double d = Double.parseDouble(number);
			long lg = (long)d;
			ret = DecimalFormat.getInstance().format(lg);
		}
		
		return ret;
	}
	
	/**
	 * Format Float number stocked as String to be readable by user
	 * @param number without format (ex=100000000)
	 * @return formated number (ex=10 000 000 or 10,000,000)
	 */
	public static String formatSurface(String number){
		
		String ret = "";
		
		if(!TextUtils.isEmpty(number)){
			float lg = Float.parseFloat(number);
			ret = DecimalFormat.getInstance().format(lg);
		}
		
		return ret;
	}
	
	/**
	 * Format Long number to be readable by user
	 * @param number without format (ex=100000000)
	 * @return formated number (ex=10 000 000 or 10,000,000)
	 */
	public static String formatNumbers(long number){
		
		return DecimalFormat.getInstance().format(number);
	}
	
   public static void showMsg(Context context, String string){
		
    	Toast msg = Toast.makeText(context,
				string, Toast.LENGTH_LONG);
		msg.setGravity(Gravity.TOP, msg.getXOffset() / 1,
				msg.getYOffset() / 1);
		msg.show();
	}
    
	public static void showMsg(Context context, int string){
		
    	Toast msg = Toast.makeText(context,
				string, Toast.LENGTH_LONG);
		msg.setGravity(Gravity.TOP, msg.getXOffset() / 1,
				msg.getYOffset() / 1);
		msg.show();
	}
	
	
	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public synchronized static final Date parse(String date) throws ParseException {
		//simpleDateFormat.setLenient(false);
		Log.d ("Date conversion", "Date recieved " + date+ " date converted " + simpleDateFormat.parse(date));
		return simpleDateFormat.parse(date);
	}
	
	public static String getconvertdate1(String date)
	{
	    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
	    Date parsed = new Date();
	    try
	    {
	        parsed = simpleDateFormat.parse(date);
	    }
	    catch (ParseException e)
	    {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    String outputText = outputFormat.format(parsed);
	    return outputText;
	}
	
	
	public static String formatTime(String time) {
		if(time.length() > 5){
			return time.substring(0, 5);
		}
		return time;
	}
	
	public static int genTimeStamp() {
	    Calendar calendar = Calendar.getInstance();
	    return calendar.get(Calendar.YEAR) * 10000 + calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.MONTH) * 100;
	  }
	
	public static boolean gpsIsEnabled(Context context) {
	    LocationManager manager = (LocationManager) context.getSystemService(Activity.LOCATION_SERVICE);
	    return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	public static Bitmap getThumbnail(Uri uri, Context context) throws FileNotFoundException, IOException{
		
        InputStream input = context.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        onlyBoundsOptions.outWidth = 480;
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        //double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 4;
        bitmapOptions.inDither=true;//optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

	private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }
	
	public static String getCompressPhotoString(Uri uri, Context context) {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
          Bitmap bitmapPhoto = Utils.getThumbnail(uri, context);
          bitmapPhoto.compress(CompressFormat.JPEG, 60, outStream);
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
        String data = null;
        try {
          data = outStream.toString("Latin1");
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
		return data;
	}
	
	public static File convertImageUriToFile(Uri imageUri, Activity activity) {
	    Cursor cursor = null;
	    try {
	      String[] proj = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.ORIENTATION };
	      if (activity == null) {
	    	  System.out.println("ACTIVITY IS NULL...");
	      }
	      cursor = activity.managedQuery(imageUri, proj, null, null, null);
	      int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	      int orientation_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
	      if (cursor.moveToFirst()) {
	        // String orientation =
	        cursor.getString(orientation_ColumnIndex);
	        return new File(cursor.getString(file_ColumnIndex));
	      }
	      return null;
	    } finally {
	      if (cursor != null) {
	       //cursor.close();
	      }
	    }
	  }

	/**
	 * Open the web browser 
	 * @url the url of the page that the browser should open 
	 * @param URL
	 */
	public static void openBrowser (Context c, String URL) {
		Uri uriUrl = Uri.parse(URL);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		launchBrowser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c.startActivity(launchBrowser); 
	}
}
