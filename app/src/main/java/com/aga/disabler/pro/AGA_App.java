package com.aga.disabler.pro;

import android.app.Application;

import com.aga.disabler.pro.activity.MainActivity;

import cat.ereza.customactivityoncrash.config.CaocConfig;

public class AGA_App extends Application {


	@Override
	public void onCreate() {
		super.onCreate();
		CaocConfig.Builder.create()
				.backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM)
				.trackActivities(true)
				.errorDrawable(R.drawable.logo)
				.restartActivity(MainActivity.class)
				.apply();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
