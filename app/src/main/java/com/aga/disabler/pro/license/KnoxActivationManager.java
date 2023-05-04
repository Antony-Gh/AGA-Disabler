package com.aga.disabler.pro.license;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.receiver.DeviceAdmin;
import com.samsung.android.knox.EnterpriseDeviceManager;
import com.samsung.android.knox.license.EnterpriseLicenseManager;
import com.samsung.android.knox.license.KnoxEnterpriseLicenseManager;


public class KnoxActivationManager {
    public static final int REQUEST_CODE_KNOX = 4545;
    public static final String KPE_KEY = "KLM09-CZBKZ-VR12Z-FEWYC-1BWHV-C99L2";
    public static final String LICENSE_KNOX = "9877E7A41EAD2AEC5F1355B66DB6E057BCB272CEE49A806F8654B1F8C08F320E9D9D94D348B2A97AF8F05291F371D3EB0B2742EEEC172C72086A55368649817B";

    private static KnoxActivationManager knoxActivationManager;

    public static KnoxActivationManager getInstance() {
        if (knoxActivationManager == null) {
            knoxActivationManager = new KnoxActivationManager();
        }
        return knoxActivationManager;
    }

    private ActivationCallback activationCallback;

    public void register(Context context, ActivationCallback callback) {
        this.activationCallback = callback;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EnterpriseLicenseManager.ACTION_LICENSE_STATUS);
        intentFilter.addAction(KnoxEnterpriseLicenseManager.ACTION_LICENSE_STATUS);
        context.registerReceiver(knoxLicenseReceiver, intentFilter);
    }

    public void unregister(Context context) {
        activationCallback = null;
        context.unregisterReceiver(knoxLicenseReceiver);
    }

    public void onActivityResult(int requestCode, final int resultCode) {
        if (requestCode == REQUEST_CODE_KNOX) {
            if (activationCallback != null) {
                if (resultCode == Activity.RESULT_OK) {
                    activationCallback.onDeviceAdminActivated();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    activationCallback.onDeviceAdminActivationCancelled();
                }
            }
        }
    }

    public void activateDeviceAdmin(Activity activity, String description) {
        ComponentName componentName = new ComponentName(activity, DeviceAdmin.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        if (description != null) {
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, description);
        }
        activity.startActivityForResult(intent, REQUEST_CODE_KNOX);
    }

    public void activateKnoxLicense(Context context, String licenseKey) {
        KnoxEnterpriseLicenseManager knoxEnterpriseLicenseManager = KnoxEnterpriseLicenseManager.getInstance(context);
        knoxEnterpriseLicenseManager.activateLicense(licenseKey);
    }

    public void activateBackwardLicense(Context context, String licenseKey) {
        EnterpriseLicenseManager enterpriseLicenseManager = EnterpriseLicenseManager.getInstance(context);
        enterpriseLicenseManager.activateLicense(licenseKey);
    }

    public boolean isDeviceAdminActivated(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context, DeviceAdmin.class);
        return devicePolicyManager != null && devicePolicyManager.isAdminActive(componentName);
    }

    public boolean isKnoxSdkSupported() {
        try {
            EnterpriseDeviceManager.getAPILevel();
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isLegacySdk() {
        try {
            return EnterpriseDeviceManager.getAPILevel() < EnterpriseDeviceManager.KNOX_VERSION_CODES.KNOX_2_8;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

    private final KnoxLicenseReceiver knoxLicenseReceiver = new KnoxLicenseReceiver() {
        @Override
        public void onKnoxLicenseActivated(Context context) {
            if (activationCallback != null) {
                activationCallback.onKnoxLicenseActivated();
            }
        }

        @Override
        public void onBackwardLicenseActivated(Context context) {
            if (activationCallback != null) {
                activationCallback.onBackwardLicenseActivated();
            }
        }

        @Override
        public void onLicenseActivationFailed(Context context, int errorCode) {
            if (activationCallback != null) {
                activationCallback.onLicenseActivateFailed(errorCode, getErrorMessage(errorCode, context));
            }
        }
    };

    private String getErrorMessage(int errorCode, Context c) {
        int str;
        switch (errorCode) {
            case EnterpriseLicenseManager.ERROR_INTERNAL:
                str = R.string.err_elm_internal;
                break;
            case EnterpriseLicenseManager.ERROR_INTERNAL_SERVER:
                str = R.string.err_elm_internal_server;
                break;
            case EnterpriseLicenseManager.ERROR_INVALID_LICENSE:
                str = R.string.err_licence_invalid_license;
                break;
            case EnterpriseLicenseManager.ERROR_INVALID_PACKAGE_NAME:
                str = R.string.err_license_invalid_package_name;
                break;
            case EnterpriseLicenseManager.ERROR_LICENSE_TERMINATED:
                str = R.string.err_license_licence_terminated;
                break;
            case EnterpriseLicenseManager.ERROR_NETWORK_DISCONNECTED:
                str = R.string.err_license_network_disconnected;
                break;
            case EnterpriseLicenseManager.ERROR_NETWORK_GENERAL:
                str = R.string.err_license_network_general;
                break;
            case EnterpriseLicenseManager.ERROR_NOT_CURRENT_DATE:
                str = R.string.err_license_not_current_date;
                break;
            case EnterpriseLicenseManager.ERROR_NULL_PARAMS:
                str = R.string.err_license_null_params;
                break;
            case EnterpriseLicenseManager.ERROR_SIGNATURE_MISMATCH:
                str = R.string.err_license_sig_mismatch;
                break;
            case EnterpriseLicenseManager.ERROR_USER_DISAGREES_LICENSE_AGREEMENT:
                str = R.string.err_license_user_disagrees_license_agreement;
                break;
            case EnterpriseLicenseManager.ERROR_VERSION_CODE_MISMATCH:
                str = R.string.err_license_ver_code_mismatch;
                break;
            case EnterpriseLicenseManager.ERROR_UNKNOWN:
            default:
                str = R.string.err_license_unknown;
                break;
        }
        return c.getString(str);
    }
}
