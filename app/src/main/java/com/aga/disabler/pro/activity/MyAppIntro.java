package com.aga.disabler.pro.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.aga.disabler.pro.R;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

import org.jetbrains.annotations.Nullable;

public class MyAppIntro extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setColorTransitionsEnabled(true);

        setIndicatorEnabled(true);

        setVibrate(true);

        setVibrateDuration(50L);

        setIndicatorColor(getColor(R.color.no_red), getColor(R.color.darkblue));

        setProgressIndicator();

        setImmersiveMode();

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.appintro_first_title),
                getString(R.string.appintro_first_des),
                R.mipmap.ic_launcher,
                Color.parseColor("#FFFF9800"),
                Color.WHITE,
                Color.WHITE));
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.appintro_sec_title),
                getString(R.string.appintro_sec_des),
                R.mipmap.ic_launcher,
                Color.parseColor("#FF00be84"),
                Color.WHITE,
                Color.WHITE));
        addSlide(AppIntroFragment.newInstance(
                getString(R.string.appintro_sec_title),
                getString(R.string.appintro_sec_des),
                R.mipmap.ic_launcher,
                Color.parseColor("#FF00be84"),
                Color.WHITE,
                Color.WHITE));
    }
}
