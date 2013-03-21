package com.letsdoitworld.wastemapper.model;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;

import com.letsdoitworld.wastemapper.objects.WastePoint;
import com.letsdoitworld.wastemapper.utils.AppConstants;


/**
 * Provides access to a database of tags. 
 */
public class DatabaseHandler {

    private static String DATABASE_NAME = "letsdoit.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_WASTE_POINT = "WastePoint";

    private static HashMap<String, String> sWastePointProjectionMap;

    public static final int WASTE_POINT = 1;

    /**
     * This class helps open, create, and upgrade the database file.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context, String name) {
			//test = context.getResources().getResourceName(R.string.database);
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	
        	db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_WASTE_POINT + " ("
        			+ WastePoint._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + WastePoint.DATA + " TEXT,"
                    + WastePoint.ACTION_TYPE + " TEXT,"
                    + AppConstants.SP_JSON_SESSION_ID + " TEXT,"
                    + AppConstants.SP_JSON_SESSION_NAME + " TEXT,"
                    + WastePoint.LATLON + " TEXT"
					+ ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WASTE_POINT);
            onCreate(db);
        }
    }

    private DatabaseHelper mOpenHelper;

    public DatabaseHandler(Context context){
    	mOpenHelper = new DatabaseHelper(context, DATABASE_NAME);
    }

    public Cursor query(int value, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        
    	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String orderBy;
        String where = "";
        String table = "";
        String groupBy = null;
        Map<String, String> projectionMap = null;

        switch (value) {
	        
	        case WASTE_POINT:
	        	table = TABLE_WASTE_POINT;
	        	projectionMap = sWastePointProjectionMap;
	        	if (TextUtils.isEmpty(sortOrder)) {
	                orderBy = WastePoint._ID;
	            } else {
	                orderBy = sortOrder;
	            }
	        	break;
	        
	        default:
	        	throw new IllegalArgumentException("Unknown request ");
        }

        // Get the database and run the query
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        
        qb.setTables(table);
        qb.setProjectionMap(projectionMap);
        if(!TextUtils.isEmpty(where))qb.appendWhere(where);
        Cursor c = qb.query(db, projection, selection, selectionArgs, groupBy, null, orderBy);
        return c;
    }
    
    public int insert(int value, ContentValues arrayList) {
        // Validate the requested uri
    	if(AppConstants.DEBUG)System.out.println(arrayList);
    	SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (value) {
        	case WASTE_POINT:
	            return (int) db.insert(TABLE_WASTE_POINT, WastePoint._ID, arrayList);
	    }
        return 0;
    }
    
    /**
     * Need to delete elements in tag_participant and all purchases
     */
    public int delete(int value, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        
        switch (value) {
        case WASTE_POINT:
            count = db.delete(TABLE_WASTE_POINT, where, whereArgs);
            break;
        default:
            throw new IllegalArgumentException("Unknown request ");
        }

        return count;
    }

    public int update(int value, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count = 0;
        switch (value) {
        
     
        case WASTE_POINT:
            count = db.update(TABLE_WASTE_POINT, values, where, whereArgs);
            break;
       
        default:
            throw new IllegalArgumentException("Unknown request ");
        }

        return count;
    }

    static {
        sWastePointProjectionMap = new HashMap<String, String>();
        sWastePointProjectionMap.put( WastePoint._ID, WastePoint._ID);
        sWastePointProjectionMap.put( WastePoint.DATA, WastePoint.DATA);
        sWastePointProjectionMap.put( WastePoint.ACTION_TYPE, WastePoint.ACTION_TYPE);
        sWastePointProjectionMap.put( AppConstants.SP_JSON_SESSION_ID, AppConstants.SP_JSON_SESSION_ID);
        sWastePointProjectionMap.put( AppConstants.SP_JSON_SESSION_NAME, AppConstants.SP_JSON_SESSION_NAME);
        sWastePointProjectionMap.put( WastePoint.LATLON, WastePoint.LATLON);
    }
}
