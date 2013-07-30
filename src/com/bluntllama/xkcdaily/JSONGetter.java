package com.bluntllama.xkcdaily;

import java.util.Calendar;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class JSONGetter extends AsyncTask<Object, JSONObject, JSONObject> {	
	private static final String TAG = "xkcd";
	
	@Override
	protected JSONObject doInBackground(Object... stuff) {
	    return download_json((String)stuff[0]);
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {

	}
	
	private JSONObject download_json(String url) {
		Log.v(TAG, "in json getter");
		JSONParser jp = new JSONParser();
		JSONObject jo = jp.getJSONFromUrl(url);
		long a = 0;
		try {
			a = jo.getLong("num");
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		String newUrl = "http://xkcd.com/" + (1 + new Random().nextInt((int)a-1)) + "/info.0.json";
		
		if(isNewDay())
			return jo;
		else
			return jp.getJSONFromUrl(newUrl);
	}
	public boolean isNewDay() {
		Calendar today = Calendar.getInstance();
		if (today.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY || 
				today.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || 
						today.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
			return true;
		else
			return false;
	}
}