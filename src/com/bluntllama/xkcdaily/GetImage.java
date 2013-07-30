package com.bluntllama.xkcdaily;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
public class GetImage extends AsyncTask<Object, Bitmap, Bitmap> {	
	private static final String TAG = "xkcd";
	
	@Override
	protected Bitmap doInBackground(Object... stuff) {
	    return download_Image((String)stuff[0]);
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		
	}
	
	private Bitmap download_Image(String url) {
		Bitmap bm = null;
	    try {
	        URL aURL = new URL(url);
	        URLConnection conn = aURL.openConnection();
	        conn.connect();
	        InputStream is = conn.getInputStream();
	        BufferedInputStream bis = new BufferedInputStream(is);
	        bm = BitmapFactory.decodeStream(bis);
	        bis.close();
	        is.close();
	    } catch (IOException e) {
	        Log.e(TAG,"Error getting the image from server.");
	    } 
	    return bm;
	}	
}