package com.bluntllama.xkcdaily;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LastUpdateFailedActivity extends Activity {
	public static final String ACTION_REFRESH = "com.bluntllama.xkcdaily.ACTION_REFRESH";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_last_update_failed);
	}
	
	public void showInfo(View v) {
		Intent intent = new Intent(getApplicationContext(), ShowFragmentActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void updateNow(View v) {
		Intent intent = new Intent();
		intent.setAction(ACTION_REFRESH);
		sendBroadcast(intent);
		finish();
		
	}

}
