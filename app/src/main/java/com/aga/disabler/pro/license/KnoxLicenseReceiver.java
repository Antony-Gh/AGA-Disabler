package com.aga.disabler.pro.license;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.aga.disabler.pro.BuildConfig;
import com.samsung.android.knox.license.EnterpriseLicenseManager;
import com.samsung.android.knox.license.KnoxEnterpriseLicenseManager;

public abstract class KnoxLicenseReceiver extends BroadcastReceiver {
    private static final String TAG = KnoxLicenseReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            debugLicenseStatus(intent);
            String action = intent.getAction();
            if (action.equals(KnoxEnterpriseLicenseManager.ACTION_LICENSE_STATUS)) {
                int errorCode = intent.getIntExtra(KnoxEnterpriseLicenseManager.EXTRA_LICENSE_ERROR_CODE, -1);
                checkKnoxResultCode(context, errorCode);
                return;
            }else{
                if (action.equals(EnterpriseLicenseManager.ACTION_LICENSE_STATUS)) {
                    int errorCode = intent.getIntExtra(EnterpriseLicenseManager.EXTRA_LICENSE_ERROR_CODE, -1);
                    checkElmResultCode(context,errorCode);
                    return;
                }
            }
        }
        onLicenseActivationFailed(context, EnterpriseLicenseManager.ERROR_UNKNOWN);
    }

    private void checkKnoxResultCode(Context context, int errcode) {
        if (errcode == EnterpriseLicenseManager.ERROR_NONE) {
            onKnoxLicenseActivated(context);
        } else {
            onLicenseActivationFailed(context, errcode);
        }
    }

    private void checkElmResultCode(Context context, int errcode) {
        if (errcode == EnterpriseLicenseManager.ERROR_NONE) {
            onBackwardLicenseActivated(context);
        } else {
            onLicenseActivationFailed(context, errcode);
        }
    }

    private void debugLicenseStatus(Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("Action : %s", intent.getAction()));
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Object value = bundle.get(key);
                    Log.d(TAG, String.format("%s %s (%s)", key,
                            value != null ? value.toString() : "null",
                            value != null ? value.getClass().getName() : ""));
                }
            }
        }
    }

    public abstract void onKnoxLicenseActivated(Context context);

    public abstract void onBackwardLicenseActivated(Context context);

    public abstract void onLicenseActivationFailed(Context context, int errorCode);
}
