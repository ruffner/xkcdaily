package com.bluntllama.xkcdaily;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ZoomImageFragment extends Fragment {
	private static final String TAG = "xkcd";
	
	private SharedPreferences prefs;
	ImageData imageDeets;
	Bitmap image = null;
	Context c;
	Activity act;
	
	public ZoomImageFragment(Activity a) {
		act = a;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.layout_zoom_image_fragment, container, false);
        
        c = inflater.getContext();
        
        imageDeets = new ImageData();
		
		if(isSavedImageAvailable())
			image = getSavedImage();
		else
			image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		
		getDetails();
		
        TouchImageView img = (TouchImageView) rootView.findViewById(R.id.zoom_view2);
		img.setImageBitmap(image);
        img.setMaxZoom(4f);
        
        return rootView;
    }
    
    public void getDetails() {
		prefs = PreferenceManager.getDefaultSharedPreferences(c);
		imageDeets.num = prefs.getLong("num", 0);
		imageDeets.title = prefs.getString("title", "No connection.");
		imageDeets.alt = prefs.getString("alt", "No connection.");
		imageDeets.date = prefs.getString("date", "No connection.");
		imageDeets.url = prefs.getString("url", "No connection.");
		Log.v(TAG, "just restored image deetails to details activity");
		Log.v(TAG, "title is" + imageDeets.title);
	}
    
    public boolean isSavedImageAvailable() {
		FileInputStream imageRestore = null;
		try {
			imageRestore = c.openFileInput("image");
		} catch (FileNotFoundException e) {
			Log.i(TAG, "no saved image available");
			return false;
		}
		if(imageRestore != null)
			return true;
		else return false;
	}
	
	public Bitmap getSavedImage() {
		FileInputStream imageRestore = null;
		try {
			imageRestore = c.openFileInput("image");
		} catch (FileNotFoundException e) {
			Log.e(TAG, "could not find saved image");
		}
		Bitmap b = null;
		if(imageRestore != null)
			b = BitmapFactory.decodeStream(imageRestore);
		try {
			imageRestore.close();
		} catch (IOException e) {
			Log.e(TAG, "could not close imageRestore stream.");
			e.printStackTrace();
		}
		return b;
		
	}
}