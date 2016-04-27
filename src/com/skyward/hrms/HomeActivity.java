package com.skyward.hrms;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ImageView ivLogout = (ImageView) findViewById(R.id.ivLogout);
		ivLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this, MainActivity.class));
				finish();
			}
		});
		LinearLayout laycustomer = (LinearLayout) findViewById(R.id.laycustomer);
		laycustomer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this,
						CustomerListActivity.class));
			}
		});
		LinearLayout layAttendancce = (LinearLayout) findViewById(R.id.layAttendancce);
		layAttendancce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this,
						DailyAttendanceActivity.class));

				finish();
			}
		});
	/*	
		LinearLayout layleave = (LinearLayout) findViewById(R.id.leave);
		layAttendancce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this,
						Leave.class));

				finish();
			}
		});*/
	}
}
