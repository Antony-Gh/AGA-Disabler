package com.aga.disabler.pro.license;

/**
 * Created by Akexorcist on 4/20/2016 AD.
 */
public interface ActivationCallback {
    void onDeviceAdminActivated();

    void onDeviceAdminActivationCancelled();

    void onKnoxLicenseActivated();

    void onBackwardLicenseActivated();

    void onLicenseActivateFailed(int errorCode, String errorMessage);
}
