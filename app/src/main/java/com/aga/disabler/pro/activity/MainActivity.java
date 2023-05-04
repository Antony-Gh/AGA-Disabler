package com.aga.disabler.pro.activity;

import static com.aga.disabler.pro.receiver.JobsService.startAction;
import static com.aga.disabler.pro.tools.FileUtil.getExternalStorageDir;
import static com.aga.disabler.pro.tools.FileUtil.isExistFile;
import static com.aga.disabler.pro.tools.Helper.getpattern;
import static com.aga.disabler.pro.tools.MyAppinfo.isedited;

import android.app.admin.DevicePolicyManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.license.GetperActivity;
import com.aga.disabler.pro.receiver.DeviceAdmin;
import com.aga.disabler.pro.receiver.NetworkSchedulerService;
import com.aga.disabler.pro.tools.Helper;
import com.aga.disabler.pro.tools.MyAppinfo;
import com.samsung.android.knox.AppIdentity;
import com.samsung.android.knox.EnterpriseDeviceManager;
import com.samsung.android.knox.application.ApplicationPolicy;


public class MainActivity extends  AppCompatActivity  {
	private Context c;

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.main);
        initialize();
		jobservice();
        initializeLogic();
    }

	private void initialize() {
		c = MainActivity.this.getApplicationContext();
		MyAppinfo.CreateFolder("/AGA Disabler/Exported Apk");
		MyAppinfo.CreateFolder("/AGA Disabler/Exported Manifest");
		MyAppinfo.CreateFolder("/AGA Disabler/Exported Icons");
		setupWindowAnimations();
	}

	private void setupWindowAnimations() {
		getWindow().setExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.activity_slide));
	}

	private void initializeLogic() {
		DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName mDeviceAdmin = new ComponentName(MainActivity.this, DeviceAdmin.class);
		boolean b = mDPM.isAdminActive(mDeviceAdmin);
		final Intent i = new Intent();
			if (notsamsung()) {
				if (!isedited(c)) {
					if (!b) {
						i.setClass(c, GetperActivity.class);
					} else {
						if (poli()) {
							if(Helper.getregi(c).equals("true")){
							if (Helper.getpass(c)) {
								if (getpattern(c) != null && !getpattern(c).equals("")) {
									i.putExtra("action", "none");
									i.setClass(c, LockActivity.class);
								} else {
									i.setClass(c, SplashActivity.class);
								}
							}else{
								i.setClass(c, ReinstallActivity.class);
								i.putExtra("type", "block");

							}
							} else{
								i.setClass(c, LoginActivity.class);
							}
						} else {
							i.setClass(c, ReinstallActivity.class);
							i.putExtra("type", "policies");
						}
					}
				} else {
					i.setClass(c, ReinstallActivity.class);
					i.putExtra("type", "edit");
				}
			} else {
				i.setClass(c, ReinstallActivity.class);
				i.putExtra("type", "notsamsung");
			}
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		finish();
	}

private boolean poli() {
		try{
			EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(MainActivity.this.getApplicationContext());
			ApplicationPolicy ap = edm.getApplicationPolicy();
			ap.addPackageToBatteryOptimizationWhiteList(new AppIdentity("com.aga.disabler.pro", null));
			return true;
		}catch (Exception e){
			return false;
		}
}

private boolean notsamsung() {
	return Build.MANUFACTURER.equalsIgnoreCase("samsung");
}

	public void jobservice() {
		Log.e("MainActivity", "Job Service");
		Intent ii = new Intent(startAction);
		sendBroadcast(ii);
		ComponentName serviceComponent = new ComponentName(c, NetworkSchedulerService.class);
		JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
		builder.setMinimumLatency(50000);
		builder.setOverrideDeadline(500000);
		JobScheduler jobScheduler = c.getSystemService(JobScheduler.class);
		jobScheduler.schedule(builder.build());
	}

}
