package com.bluntllama.xkcdaily;

import org.json.JSONException;
import org.json.JSONObject;

class ImageData {
		String title;
		String alt;
		String date;
		String url;
		long num;
		
		public ImageData() {
			num = 0;
			url = "";
			title = "";
			alt = "";
			date = "";				
		}
		public ImageData(JSONObject obj) {
			try {
				num = obj.getLong("num");
				url = obj.getString("img");
				title = obj.getString("title");
				alt = obj.getString("alt");
				date = obj.getString("month") + "/" + obj.getString("day") + "/" + obj.getString("year");
				//Log.v(TAG, "successfully deciphered json data in comic");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}