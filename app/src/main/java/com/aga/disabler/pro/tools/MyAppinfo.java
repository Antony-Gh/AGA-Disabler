package com.aga.disabler.pro.tools;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class MyAppinfo {
    public static final String APP_NAME = "AGA Disabler";
    public static final String APP_PKG = "com.aga.disabler.pro";


    public static String getapppkgname(Context context) {
        return context.getPackageName();
    }

public static boolean isedited(Context context) {
    return !getapppkgname(context).equals(APP_PKG) || ! com.aga.disabler.pro.tools.Helper.getappname(context, APP_PKG).equals(APP_NAME);
}

    public static void activeSettingsPer(Context context, String str) {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + str));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            context.startActivity(new Intent("android.settings.MANAGE_APPLICATIONS_SETTINGS"));
        }
    }

    public static void CreateFolder(String str) {
        String concat = FileUtil.getExternalStorageDir() + str;
        if (!FileUtil.isExistFile(concat)) {
            FileUtil.makeDir(concat);
        }
    }

}
