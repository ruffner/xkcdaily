package com.bluntllama.xkcdaily;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class NoConnectionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_no_connection_no_image);
	}
	
	public void closeWindow(View v) {
		finish();
	}

}
