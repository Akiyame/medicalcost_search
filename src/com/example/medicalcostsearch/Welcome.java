package com.example.medicalcostsearch;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class Welcome extends Activity {
	private final int SPLASH_DISPLAY_LENGHT = 600; // ÑÓ³Ù¶þÃë

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		PackageManager pm = getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo("com.lyt.android", 0);
			TextView versionNumber = (TextView) findViewById(R.id.versionNumber);
			versionNumber.setText("Version " + pi.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent mainIntent = new Intent(Welcome.this,
						Login.class);
				Welcome.this.startActivity(mainIntent);
				Welcome.this.finish();
			}

		}, SPLASH_DISPLAY_LENGHT);
	}
}
