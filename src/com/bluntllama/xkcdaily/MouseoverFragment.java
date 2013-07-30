package com.bluntllama.xkcdaily;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MouseoverFragment extends Fragment {
	private static final String TAG = "xkcd";
	
	private SharedPreferences prefs;
	ImageData imageDeets;
	Context c;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.layout_mouseover_fragment, container, false);
        
        c = inflater.getContext();
        
        imageDeets = new ImageData();
        getDetails();
        
        TextView title = (TextView)rootView.findViewById(R.id.text_title);
		TextView date = (TextView)rootView.findViewById(R.id.text_date);
		TextView number = (TextView)rootView.findViewById(R.id.text_number);
		TextView mouseover = (TextView)rootView.findViewById(R.id.text_mouseover);
		
		title.setText(imageDeets.title);
		date.setText(imageDeets.date);
		number.setText("#" + Long.toString(imageDeets.num));
		mouseover.setText(imageDeets.alt);
        
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
}