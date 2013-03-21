package com.letsdoitworld.wastemapper.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.letsdoitworld.wastemapper.objects.Connection;
import com.letsdoitworld.wastemapper.objects.ParsableObject;
import com.letsdoitworld.wastemapper.utils.AppConstants;

public class Parser {
	
	private BufferedReader br;

    /** The default separator to use  */
    public static final char DEFAULT_SEPARATOR = ',';

    /**
     * The default quote character
     */
    public static final char DEFAULT_QUOTE_CHARACTER = '"';

    /**
     * The default line to start reading.
     */
    public static final int DEFAULT_SKIP_LINES = 0;
	
	private Connection connection = new Connection();
	
	public Parser() {
		super();
	}
	
    public Connection getConnection(){
	   return connection;
    }
    
    public boolean isSuccess(JSONObject ret){
    	simpleParse(ret);
    	return connection.isSuccess();
    }
    
    public boolean isSuccess(){
    	return connection.isSuccess();
    }
    
    @SuppressWarnings("unchecked")
  	public ParsableObject parseJsonToObject(ParsableObject object, JSONObject input){
  		
      	if(object ==  null) return null;
      	
  		Iterator<String> iterator = input.keys();
  		String tmp = "";
  		String tmpvalue = "";
  		ArrayList<ParsableObject> arraysPo = new ArrayList<ParsableObject>();
  		int i = 0;
  		
  		try {
  			
  			while(iterator.hasNext()){

  				tmp = iterator.next();
  				tmpvalue = input.getString(tmp);
  				
  				JSONArray jsonArray = input.optJSONArray(tmp);
     				
     				if(jsonArray != null ){
     					for(i=0; i < jsonArray.length(); i++){
	  						JSONObject jsonObject = jsonArray.optJSONObject(i);
	  						if(jsonObject != null){
	  							arraysPo.add(parseJsonToObject( object.getSubObject(tmp),jsonObject));
							}
     					}
     				}
     				
  				object.setValue(tmp, tmpvalue, arraysPo);
  				arraysPo = new ArrayList<ParsableObject>(); 
  			}
  			
  		} catch (JSONException e) {
  			if(AppConstants.DEBUG)System.out.println("error message parse to json object:"+e.getMessage());
  			e.printStackTrace();
  		}
  		catch (Exception e) {
  			e.printStackTrace();
  		}
  		return object;
  	}
      
     
  	public ArrayList<? extends ParsableObject> parseToJsonList(String tag, ParsableObject object, JSONObject input){
  		
  		ArrayList<ParsableObject> arrayPO = new ArrayList<ParsableObject>();
  		
  		if(input.length() == 0){
  			connection.setServerOn(false);
  			return arrayPO;
  		}
  		
  		JSONArray mainObject = input.optJSONArray(tag);
  		
  		try {
  		
  			for(int i=0; i< mainObject.length(); i ++){
  				
  				arrayPO.add(parseJsonToObject(object.getNewObject(), (JSONObject)mainObject.get(i)));
  			}
  		} catch (JSONException e) {
  			if(AppConstants.DEBUG)System.out.println("error message parse to json list:"+e.getMessage());
  			e.printStackTrace();
  		}
  		catch (Exception e) {
  			e.printStackTrace();
  		}
  		
  		return arrayPO;
  	}    
   
	@SuppressWarnings("unchecked")
	public void simpleParse(JSONObject input){
				
		connection = new Connection();
		
		if(input == null || input.length() == 0){
			connection.setStatus("1");
			return;
		}
		
		Iterator<String> iterator = input.keys();
		String tmp = "";
		String tmpvalue = "";

		while(iterator.hasNext()){
			tmp = iterator.next();
			tmpvalue = input.optString(tmp);
			connection.setValue(tmp, tmpvalue, null);
		}
	}
    
   	public boolean checkSuccess(JSONObject input, String tag, String value){
   		
   		boolean ret = false;
    	
    	if(input.length() == 0){
   			return ret;
   		}
   		
   		try{
   		
   			String tmpvalue = input.getString(tag);
   			if(!TextUtils.isEmpty(tmpvalue) && tmpvalue.equals(value)){
   				ret = true;
   			}
   		
   		} catch (JSONException e) {
   			if(AppConstants.DEBUG)	e.printStackTrace();
   		}
   		catch (Exception e) {
   			if(AppConstants.DEBUG)	e.printStackTrace();
   		}
   		
   		return ret;
   	}
       
    

	public Connection getNewConnection() {
		connection = new Connection();
		return connection;
	}

	public ArrayList<String> parseList(String tag, JSONObject json) {
		
		ArrayList<String> array = new ArrayList<String>();
		
		JSONArray jsonArray = json.optJSONArray(tag);
		
		try {
			for(int i=0; i < jsonArray.length(); i++){
					array.add(jsonArray.getString(i));
			}
		} catch (JSONException e) {
			if(AppConstants.DEBUG)	e.printStackTrace();
		}
		
		return array;
	}

	public void setFailed() {
		connection = new Connection();
		connection.setStatus("1");
	}
	
	public void setSucceeded() {
		connection = new Connection();
		connection.setStatus("0");
	}
	
	//CSV
	 /**
	   * Decodes InputStream of CSV format
	   */
	  public ArrayList<? extends ParsableObject>  parseCSVToArray(InputStream is,  ParsableObject object) {
		  System.out.println("1");
		 
		connection = new Connection();
		  
	    if (is == null) {
	      return null;
	    }
	    ArrayList<ParsableObject> arraysPo = new ArrayList<ParsableObject>();
	    
	    br = new BufferedReader(new InputStreamReader(is));
	    String nextLine = null;
	    try {
	      nextLine = br.readLine();
	      String[] names = parseLine(nextLine);
	      while ((nextLine = br.readLine()) != null) {
	    	  arraysPo.add(arrayToMap(object, names, parseLine(nextLine)));
	    	  object = object.getNewObject();
	      }
	      is.close();
	    } catch (InterruptedIOException interrupted) {
        	Log.e("RestJsonClient error" , interrupted.toString()); 
         } catch (ClientProtocolException e) {
        	 Log.e("RestJsonClient protocol error" ,e.toString()); 
        } catch (IOException e) {
        	Log.e("RestJsonClient IO error" , e.toString()); 
        	e.printStackTrace();
	    }
	
	    return arraysPo;
	  }

	  /**
	   * Create Parsable object from two String arrays
	   */
	  public static ParsableObject arrayToMap(ParsableObject map, String[] names, String[] values) {
	    if (names == null || values == null)
	      return null;
	    int i = names.length;
	    if (i > values.length)
	       i = values.length;
	    for (int x = 0; x < i; x++) {
			map.setValue(names[x], values[x], null);
	    }
	    return map;
	  }


	    /**
	     * Parses an incoming String and returns an array of elements.
	     *
	     * @param nextLine
	     *            the string to parse
	     * @return the comma-tokenized list of elements, or null if nextLine is null
	     * @throws IOException if bad things happen during the read
	     */
	    private String[] parseLine(String nextLine) throws IOException {

	        if (nextLine == null) {
	            return null;
	        }

	        List<String> tokensOnThisLine = new ArrayList<String>();
	        StringBuffer sb = new StringBuffer();
	        boolean inQuotes = false;
	        do {
	        	if (inQuotes) {
	                // continuing a quoted section, reappend newline
	                sb.append("\n");
	                nextLine = br.readLine();
	                if (nextLine == null)
	                    break;
	            }
	            for (int i = 0; i < nextLine.length(); i++) {

	                char c = nextLine.charAt(i);
	                if (c == DEFAULT_QUOTE_CHARACTER) {
	                	// this gets complex... the quote may end a quoted block, or escape another quote.
	                	// do a 1-char lookahead:
	                	if( inQuotes  // we are in quotes, therefore there can be escaped quotes in here.
	                	    && nextLine.length() > (i+1)  // there is indeed another character to check.
	                	    && nextLine.charAt(i+1) == DEFAULT_QUOTE_CHARACTER ){ // ..and that char. is a quote also.
	                		// we have two quote chars in a row == one quote char, so consume them both and
	                		// put one on the token. we do *not* exit the quoted text.
	                		sb.append(nextLine.charAt(i+1));
	                		i++;
	                	}else{
	                		inQuotes = !inQuotes;
	                		// the tricky case of an embedded quote in the middle: a,bc"d"ef,g
	                		if(i>2 //not on the begining of the line
	                				&& nextLine.charAt(i-1) != DEFAULT_SEPARATOR //not at the begining of an escape sequence
	                				&& nextLine.length()>(i+1) &&
	                				nextLine.charAt(i+1) != DEFAULT_SEPARATOR //not at the	end of an escape sequence
	                		){
	                			sb.append(c);
	                		}
	                	}
	                } else if (c == DEFAULT_SEPARATOR && !inQuotes) {
	                    tokensOnThisLine.add(sb.toString());
	                    sb = new StringBuffer(); // start work on next token
	                } else {
	                    sb.append(c);
	                }
	            }
	        } while (inQuotes);
	        tokensOnThisLine.add(sb.toString());
	        return (String[]) tokensOnThisLine.toArray(new String[0]);

	    }

	    /**
	     * Closes the underlying reader.
	     *
	     * @throws IOException if the close fails
	     */
	    public void close() throws IOException{
	        br.close();
	    }

	
}
