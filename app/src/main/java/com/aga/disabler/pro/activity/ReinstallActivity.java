package com.aga.disabler.pro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.license.GetperActivity;
import com.aga.disabler.pro.receiver.devicepolicy;

import static com.aga.disabler.pro.tools.Helper.uninaga;

public class ReinstallActivity extends  AppCompatActivity  {


	private TextView textview1;
	private Button button2;
	private Context con;
	private Button button3;

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.reinstall);
		initialize();
		initializeLogic();
	}
	
	private void initialize() {
		textview1 = findViewById(R.id.textview1);
		Button button1 = findViewById(R.id.button1);
		button2 = findViewById(R.id.button2);
		button1.setOnClickListener(_view -> finishAffinity());
		button3 = findViewById(R.id.button3);

		try{
			devicepolicy dp = new devicepolicy(this);
			dp.myApppolicy(false);
		}catch(Exception e){

		}

		button3.setOnClickListener(view -> uninaga(ReinstallActivity.this));

		button2.setOnClickListener(v -> {
			Intent i = new Intent();
			con = ReinstallActivity.this.getApplicationContext();
			i.setClass(con, GetperActivity.class);
			startActivity(i);
		});
	}
	
	private void initializeLogic() {
		String Type = getIntent().getStringExtra("type");
		assert Type != null;
		switch (Type){
			case "update" :
				textview1.setText(R.string.update_app_msg);
				break;
			case "edit" :
				textview1.setText(R.string.modify_ver_msg);
				break;
			case "policies" :
				textview1.setText(R.string.err_klm_license_expired);
				button2.setVisibility(View.VISIBLE);
				break;
			case "notsamsung":
				textview1.setText(R.string.notsam);
				break;
			case "block":
				textview1.setText(R.string.app_block);
				break;
		}
	}
}
