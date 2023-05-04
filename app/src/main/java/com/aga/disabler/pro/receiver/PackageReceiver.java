package com.aga.disabler.pro.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.tools.Helper;

import java.util.Arrays;

import static com.aga.disabler.pro.tools.Helper.emmtoast;

public class PackageReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String pkgname = handleintent(intent,context);
        assert action != null;
        switch (action) {
            case "android.intent.action.PACKAGE_INSTALL":
                createtoast(context,pkgname,1);
                break;
            case "android.intent.action.PACKAGE_ADDED":
                createtoast(context,pkgname,2);
                break;
            case "android.intent.action.MY_PACKAGE_REPLACED":
                createtoast(context,pkgname,3);
                break;
            case "android.intent.action.MY_PACKAGE_SUSPENDED":
                createtoast(context,pkgname,4);
                break;
            case "android.intent.action.MY_PACKAGE_UNSUSPENDED":
                createtoast(context,pkgname,5);
                break;
            case "android.intent.action.PACKAGES_SUSPENDED":
                createtoast(context,pkgname,6);
                break;
            case "android.intent.action.PACKAGES_UNSUSPENDED":
                createtoast(context,pkgname,7);
                break;
            case "android.intent.action.PACKAGE_DATA_CLEARED":
                createtoast(context,pkgname,9);
                break;
            case "android.intent.action.PACKAGE_FIRST_LAUNCH":
                createtoast(context,pkgname,10);
                break;
            case "android.intent.action.PACKAGE_FULLY_LOADED":
                createtoast(context,pkgname,11);
                break;
            case "android.intent.action.PACKAGE_FULLY_REMOVED":
                createtoast(context,pkgname,12);
                break;
            case "android.intent.action.PACKAGE_REMOVED":
                createtoast(context,pkgname,13);
                break;
            case "android.intent.action.PACKAGE_REPLACED":
                createtoast(context,pkgname,14);
                break;
            case "android.intent.action.PACKAGE_RESTARTED" :
                createtoast(context,pkgname,15);
                break;
        }
    }

    private void createtoast(Context con, String pkg,int type) {
        String msg;
        switch(type) {
            case 1:
                msg = "Android App : (" + Helper.getappname(con, pkg) + ") Has been Installed with package name : (" + pkg + ") ";
            break;
            case 2:
                msg = "Android App : (" + Helper.getappname(con, pkg) + ") Has been Added with package name : (" + pkg + ") ";
            break;
            case 3:
               msg = "AGA Disabler has been updated to version " + Helper.getVersionname(pkg,con) + " with code " + Helper.getVersionCode(con,pkg);
            break;
            case 4:
                msg = "AGA Disabler has been suspended";
            break;
            case 5:
                msg = "AGA Disabler has been unsuspended";
            break;
            case 6:
                msg = "Android App : (" + Helper.getappname(con, pkg) + ") Has been suspended with package name : (" + pkg + ") ";
             break;
            case 7:
                msg = "Android App : (" + Helper.getappname(con, pkg) + ") Has been unsuspended with package name : (" + pkg + ") ";
                break;
            case 8:
                msg = "Android App : (" + Helper.getappname(con, pkg) + ") Has been changed with package name : (" + pkg + ") ";
                break;
            case 9:
                msg = "Android App : (" + Helper.getappname(con, pkg) + ") Has been cleared its data with package name : (" + pkg + ") ";
                break;
            case 10:
                msg = "Android App : (" + Helper.getappname(con, pkg) + ") Has been first launch with package name : (" + pkg + ") ";
                break;
            case 11:
                msg = "Android App : (" + Helper.getappname(con, pkg) + ") Has been fully loaded with package name : (" + pkg + ") ";
                break;
            case 12:
                msg = "Android App : (" + Helper.getappname(con, pkg) + ") Has been fully removed with package name : (" + pkg + ") ";
                break;
            case 13:
                msg = "Android App : (" + Helper.getappname(con, pkg) + ") Has been just removed with package name : (" + pkg + ") ";
                break;
            case 14:
                msg = "Android App : (" + Helper.getappname(con, pkg) + ") Has been replaced with package name : (" + pkg + ") ";
                break;
            case 15:
                msg = "Android App : (" + Helper.getappname(con, pkg) + ") Has been restarted with package name : (" + pkg + ") ";
                break;
            default:
                msg = con.getString(R.string.unknown);
            break;
        }
        emmtoast(msg,con);
    }

    public static String handleintent(Intent intt,Context con){
        try{
            String pkg;
            pkg = String.valueOf(intt.getData().getSchemeSpecificPart());
            if(pkg.equals("")) {
                Bundle b = intt.getExtras();
                int uid = b.getInt(Intent.EXTRA_UID);
                pkg = Arrays.toString(con.getPackageManager().getPackagesForUid(uid));
            }
            if(pkg.equals("")) {
                pkg =  intt.getStringExtra(Intent.EXTRA_PACKAGE_NAME);
            }
            return pkg;
        }catch(Exception e){
            return "";
        }
    }

}
