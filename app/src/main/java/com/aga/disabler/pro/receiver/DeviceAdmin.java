package com.aga.disabler.pro.receiver;

import static com.aga.disabler.pro.tools.Helper.emmtoast;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.license.SharedPreferenceManager;

import org.jetbrains.annotations.NotNull;

public class DeviceAdmin extends DeviceAdminReceiver {

    public CharSequence onDisableRequested(@NotNull Context context, @NotNull Intent intent) {
        return context.getString(R.string.disable_admin);
    }

    public void onDisabled(@NotNull Context context, @NotNull Intent intent) {
        updateState(context, 2);
        super.onDisabled(context, intent);
    }

    public void onEnabled(@NotNull Context context, @NotNull Intent intent) {
        updateState(context, 1);
        super.onEnabled(context, intent);
    }

    public void updateState(Context context, int i) {
        String str = "";
        if(i == 1) {
            str = context.getString(R.string.admin_ena);
        } else {
            if(i == 2) {
                SharedPreferenceManager.setKnoxLicenseActivated(context, false);
                str = context.getString(R.string.admin_dis);
            }
        }
        emmtoast(str, context);
    }


}
