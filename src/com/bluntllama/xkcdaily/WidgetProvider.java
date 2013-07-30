package com.bluntllama.xkcdaily;

import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.RemoteViews;
 
public class WidgetProvider extends AppWidgetProvider {	
	private static final String TAG = "xkcd";
	
	Bitmap image = null;
	ImageData imaged = null;
	boolean isConnected;
	private SharedPreferences prefs;
	private SharedPreferences.Editor prefEdit;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.v(TAG, "in on update in widget.");
		
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefEdit = prefs.edit();
		
		update(context, appWidgetManager, appWidgetIds, null);
	}
	
	//This is where we do the actual updating
	public void update(Context context, AppWidgetManager manager, int[] ids, Object data) {
		final int N = ids.length;

		// Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = ids[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            
            // check if were online
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni == null || !ni.isConnected()) {
			    // no connection
			    isConnected = false;
			    
			    // set image to no wifi
			    image = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_wifi);
			    
			    // put the image into the widget
				views.setImageViewBitmap(R.id.imageView, image);

				// show the no connection dialog on click
				Intent intent = new Intent(context, NoConnectionActivity.class);
	            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
	            views.setOnClickPendingIntent(R.id.imageView, pendingIntent);
			    
			} else {
				isConnected = ni.isConnected();
				// get an image
	            try {
	            	imaged = new ImageData(new JSONGetter().execute("http://xkcd.com/info.0.json").get());
					image = new GetImage().execute(imaged.url).get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
	            
	            // save the image
	            if(saveImage(context))
	            	Log.v(TAG, "successfully saved image");
	            else
	            	Log.v(TAG, "failed to saved image");
	            
	            saveDetails(context);
	            Log.v(TAG, "just saved image details");
	            
	            // put the image into the widget
				views.setImageViewBitmap(R.id.imageView, image);
				Log.v(TAG, "just put image into widget");
				
				// launch our app when clicked
	            Intent intent = new Intent(context, ShowFragmentActivity.class);
	            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
	            views.setOnClickPendingIntent(R.id.imageView, pendingIntent);
			}
			
			
            // update the widget for real
            manager.updateAppWidget(appWidgetId, views);
        }
	}
	
	public static class OfflineDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage("you're offline")
	               .setPositiveButton("ok", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // FIRE ZE MISSILES!
	                   }
	               })
	               .setNegativeButton("oh well", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
	
	public boolean saveImage(Context c) {
		FileOutputStream imageStore = null;
        try {
			imageStore = c.openFileOutput("image", Context.MODE_PRIVATE);
			image.compress(Bitmap.CompressFormat.PNG, 90, imageStore);
	        imageStore.flush();
	        imageStore.close();
		} catch (Exception e) {
			return false;
		}
        return true;
	}
	
	public void saveDetails(Context c) {
		prefEdit.putLong("num", imaged.num);
		prefEdit.putString("title", imaged.title);
		prefEdit.putString("alt", imaged.alt);
		prefEdit.putString("date", imaged.date);
		prefEdit.putString("url", imaged.url);		
		
		prefEdit.commit();
	}
	
	public void showOfflineDialog(Context c) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new OfflineDialogFragment();
        dialog.show(((FragmentActivity) c).getSupportFragmentManager(), "NoticeDialogFragment");
    }
}