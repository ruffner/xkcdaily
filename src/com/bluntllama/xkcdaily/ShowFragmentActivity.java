package com.bluntllama.xkcdaily;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ShowFragmentActivity extends FragmentActivity {
	private static final String TAG = "xkcd";

    private static final int NUM_PAGES = 2;
    private CustomViewPager mPager = null;
    private PagerAdapter mPagerAdapter;
    private SharedPreferences prefs;
    private boolean isOnImageView = true;
    
    Context c;
    Activity a;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	c = this;
    	a = this;
    	setContentView(R.layout.activity_show_fragment);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
		setTitle(prefs.getString("title", "No connection."));
		
		final Button b = (Button)findViewById(R.id.button1);
    	b.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			if(isOnImageView) {
    				mPager.setCurrentItem(1);
    				b.setText(R.string.label_picture_button);
    				isOnImageView = false;
    			} else {
    				mPager.setCurrentItem(0);
    				b.setText(R.string.mouseover_button);
    				isOnImageView = true;
    			}
    		}
    	});

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (CustomViewPager) findViewById(R.id.pager);
        if(mPager == null)
        	Log.e(TAG, "mPager is null!");
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        if(mPagerAdapter == null)
        	Log.e(TAG, "mPagerAdapter is null!");
        mPager.setAdapter(mPagerAdapter);
        
        mPager.setScrollContainer(false);
        
        ViewGroup.LayoutParams lp = mPager.getLayoutParams();
        Bitmap img = getSavedImage();
        if(img.getHeight() > img.getWidth()) {
        	lp.height = img.getHeight() + 60;
        	lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        else {
        	lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        	lp.height = img.getHeight() + 200;
        }
		mPager.setLayoutParams(lp);
        
        mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				switch(position) {
				case 0:
					b.setText(R.string.mouseover_button);
					isOnImageView = true;
					break;
				case 1:
					b.setText(R.string.label_picture_button);
					isOnImageView = false;
					break;
				}
				
			}
        });
        
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }
	
	public void closeWindow(View v) {
		finish();
	}
	
	public void shareLink(View v) {
        Intent urlMessageIntent = new Intent(android.content.Intent.ACTION_SEND);  
        urlMessageIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        urlMessageIntent.setType("text/plain");  
        urlMessageIntent.putExtra(Intent.EXTRA_TEXT, "http://www.xkcd.com/" + Long.toString(prefs.getLong("num", 1)));
        startActivity(Intent.createChooser(urlMessageIntent, "Send the link using:"));
	}
	
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

		@Override
        public Fragment getItem(int position) {
        	switch(position) {
	        	case 0:
	        		return new ZoomImageFragment(a);
	        	case 1:
	        		return new MouseoverFragment();
        	}
            return new ZoomImageFragment(a);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
    
    public boolean isSavedImageAvailable() {
		FileInputStream imageRestore = null;
		try {
			imageRestore = openFileInput("image");
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