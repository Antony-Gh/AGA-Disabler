package com.aga.disabler.pro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.receiver.devicepolicy;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends  AppCompatActivity  { 

	private final Intent inte = new Intent();

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.splash);
		initialize();
		setupWindowAnimations();
	}
	
	private void initialize() {
		LinearLayout linearTop = findViewById(R.id.linearTop);
		TextView txt = findViewById(R.id.textview2);
		Animation frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
		txt.setAnimation(frombottom);
		Animation fromtop= AnimationUtils.loadAnimation(this, R.anim.fromtop);
		linearTop.setAnimation(fromtop);
		try{
			devicepolicy dp = new devicepolicy(this);
			dp.myApppolicy(true);
		}catch(Exception e){
			e.printStackTrace();
		}

Timer tt = new Timer();
		TimerTask t1 = new TimerTask() {
			@Override
			public void run() {
				final Context con = getApplicationContext();
				inte.setClass(con, AdminActivity.class);
				inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				con.startActivity(inte);
				SplashActivity.this.finish();
			}
		};
		tt.schedule(t1, (1050));
	}

	private void setupWindowAnimations() {
		getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.activity_fade));
		getWindow().setExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.activity_slide));
	}

}
