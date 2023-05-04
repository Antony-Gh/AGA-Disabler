package com.aga.disabler.pro;

import android.app.Application;
import android.content.Intent;

import com.aga.disabler.pro.activity.MainActivity;
import com.aga.disabler.pro.receiver.PackageLoaderService;
import cat.ereza.customactivityoncrash.config.CaocConfig;

public class AGA_App extends Application {


	@Override
	public void onCreate() {
		super.onCreate();
		CaocConfig.Builder.create()
				.backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM)
				.trackActivities(true)
				.errorDrawable(R.mipmap.ic_launcher)
				.restartActivity(MainActivity.class)
				.apply();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
