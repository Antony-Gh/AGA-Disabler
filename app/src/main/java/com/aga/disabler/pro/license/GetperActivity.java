package com.aga.disabler.pro.license;

import static com.aga.disabler.pro.license.SharedPreferenceManager.isKnoxLicenseActivated;
import static com.aga.disabler.pro.tools.Helper.emmtoast;
import static com.aga.disabler.pro.tools.Helper.isadminactiva;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.activity.LoginActivity;
import com.aga.disabler.pro.activity.SplashActivity;
import com.aga.disabler.pro.receiver.devicepolicy;
import com.aga.disabler.pro.tools.CustomDialog;
import com.aga.disabler.pro.tools.Helper;

public class GetperActivity extends AppCompatActivity {

	private Button button4;
	private ScrollView linear6;
	private LinearLayout load;
	private Context c;
	private final ActivationCallback activationCallback = new ActivationCallback() {
		@Override
		public void onDeviceAdminActivated() {
			setDeviceAdminActivated();
		}

		@Override
		public void onDeviceAdminActivationCancelled() {
			showDeviceAdminActivationProblem();
		}

		@Override
		public void onKnoxLicenseActivated() {
			saveKnoxLicenseActivationStateToSharedPreference();
			if (KnoxActivationManager.getInstance().isLegacySdk()) {
				activateBackwardLicense();
			} else {
				setLicenseActivationSuccess();
			}
		}

		@Override
		public void onBackwardLicenseActivated() {
			saveBackwardLicenseActivationStateToSharedPreference();
			setLicenseActivationSuccess();
		}

		@Override
		public void onLicenseActivateFailed(int errorType, String errorMessage) {
			hidepass(true);
			showLicenseActivationProblem(errorType, errorMessage);
		}
	};

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.getper);
		c = GetperActivity.this.getApplicationContext();
		KnoxActivationManager.getInstance().register(this, activationCallback);
		initialize();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		KnoxActivationManager.getInstance().onActivityResult(requestCode, resultCode);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 123) {
			String msg;
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				msg = permissions[0] + "Granted Successfully";
			} else {
				msg = permissions[0] + "Denied";
			}
			emmtoast(msg, c);
		}
	}
	
	private void initialize() {
		load = findViewById(R.id.load);
		button4 = findViewById(R.id.button4);
		linear6 = findViewById(R.id.linear6);
		CheckBox check = findViewById(R.id.checkBox_agreement);
		button4.setEnabled(false);
		check.setOnCheckedChangeListener((buttonView, isChecked) -> button4.setEnabled(isChecked));
		button4.setOnClickListener(_view -> Async());
		hidepass(true);
		if (isKnoxLicenseActivated(c)) {
			startActivity(new Intent(this, SplashActivity.class));
			finish();
		}
	}
	
	private void Async() {
		hidepass(false);
		if(isKnoxLicenseActivated(c)){
			startActivity(new Intent(this, SplashActivity.class));
			finish();
		}else{
			if(!isadminactiva(c)){
				checkKnoxSdkSupported();
			}else{
				activateKnoxLicense();
			}
		}
	}

	private boolean ActivatePolicy(Context con) {
		try {
			devicepolicy dp = new devicepolicy(con);
			dp.myApppolicy(true);
			return true;
		}catch (Exception e){
			return false;
		}
	}

	private void checkKnoxSdkSupported() {
		if (KnoxActivationManager.getInstance().isKnoxSdkSupported()) {
			activateDeviceAdmin();
		} else {
			showDeviceUnsupportedProblem();
		}
	}

	private void activateDeviceAdmin() {
		if (KnoxActivationManager.getInstance().isDeviceAdminActivated(this)) {
			setDeviceAdminActivated();
		} else {
			KnoxActivationManager.getInstance().activateDeviceAdmin(this, null);
		}
	}

	private void activateKnoxLicense() {
		hidepass(false);
		if (isKnoxLicenseActivated(this)) {
			activationCallback.onKnoxLicenseActivated();
		} else {
			KnoxActivationManager.getInstance().activateKnoxLicense(this, KnoxActivationManager.KPE_KEY);
		}
	}

	private void activateBackwardLicense() {
		if (SharedPreferenceManager.isBackwardLicenseActivated(this)) {
			activationCallback.onBackwardLicenseActivated();
		} else {
			KnoxActivationManager.getInstance().activateBackwardLicense(this, KnoxActivationManager.LICENSE_KNOX);
		}
	}

	private void setDeviceAdminActivated() {
		showDeviceAdminActivationSuccess();
		activateKnoxLicense();
	}

	private void setLicenseActivationSuccess() {
		load.setVisibility(View.GONE);
		showLicenseActivationSuccess();
		goToDoSomethingActivity();
	}

	private void saveKnoxLicenseActivationStateToSharedPreference() {
		SharedPreferenceManager.setKnoxLicenseActivated(this, true);
	}

	private void saveBackwardLicenseActivationStateToSharedPreference() {
		SharedPreferenceManager.setBackwardLicenseActivated(this);
	}

	private void goToDoSomethingActivity() {
		if(ActivatePolicy(this)) {
			startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
			finish();
		}else{
			hidepass(true);
			emmtoast("Failed to activate app policies", this);
		}
	}

	private void showLicenseActivationSuccess() {
		emmtoast(getString(R.string.license_activation_success), this);
	}

	private void showDeviceAdminActivationSuccess() {
		emmtoast(getString(R.string.device_admin_activation_success), this);
	}

	private void showDeviceUnsupportedProblem() {
		Helper.CreateKnoxAlert(this, getString(R.string.device_unsupported_title), getString(R.string.device_unsupported_content), new CustomDialog.onClick() {
			@Override
			public void onOkClick() {
				finish();
			}
			@Override
			public void onCancelClick() {

			}
		});
	}

	private void showDeviceAdminActivationProblem() {
		Helper.CreateKnoxAlert(this, getString(R.string.device_admin_cancelled_title), getString(R.string.device_admin_cancelled_content), new CustomDialog.onClick() {
			@Override
			public void onOkClick() {
				activateDeviceAdmin();
			}
			@Override
			public void onCancelClick() {
				finish();
			}
		});
	}

	private void showLicenseActivationProblem(int errorType, String errorMessage) {
		Helper.CreateKnoxAlert(this, getString(R.string.license_failed_title), getString(R.string.license_failed_content, errorMessage, errorType), new CustomDialog.onClick() {
			@Override
			public void onOkClick() {
				activateKnoxLicense();
			}
			@Override
			public void onCancelClick() {
				finish();
			}
		});
	}


	public void hidepass(boolean z){
		if(z){
			linear6.setVisibility(View.VISIBLE);
			load.setVisibility(View.GONE);
		}else{
			load.setVisibility(View.VISIBLE);
			linear6.setVisibility(View.GONE);
		}
	}




}
