package com.aga.disabler.pro.receiver;

import static com.aga.disabler.pro.tools.Helper.emmtoast;
import static com.aga.disabler.pro.tools.Helper.getappname;
import static com.aga.disabler.pro.tools.Helper.keys;
import static com.samsung.android.knox.application.ApplicationPolicy.ERROR_NONE;
import static com.samsung.android.knox.custom.CustomDeviceManager.HIDE;
import static com.samsung.android.knox.custom.CustomDeviceManager.SETTINGS_WIFI;
import static com.samsung.android.knox.custom.CustomDeviceManager.SHOW;
import static com.samsung.android.knox.custom.CustomDeviceManager.SUCCESS;
import static com.samsung.android.knox.custom.CustomDeviceManager.USB_CONNECTION_TYPE_CHARGING;
import static com.samsung.android.knox.custom.CustomDeviceManager.USB_CONNECTION_TYPE_DEFAULT;
import static com.samsung.android.knox.custom.CustomDeviceManager.getInstance;
import static com.samsung.android.knox.net.firewall.DomainFilterRule.CLEAR_ALL;

import android.content.Context;
import android.util.Log;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.tools.PermInfo;
import com.samsung.android.knox.AppIdentity;
import com.samsung.android.knox.EnterpriseDeviceManager;
import com.samsung.android.knox.EnterpriseKnoxManager;
import com.samsung.android.knox.application.ApplicationPolicy;
import com.samsung.android.knox.custom.CustomDeviceManager;
import com.samsung.android.knox.lockscreen.BootBanner;
import com.samsung.android.knox.net.firewall.DomainFilterRule;
import com.samsung.android.knox.net.firewall.Firewall;
import com.samsung.android.knox.net.firewall.FirewallResponse;
import com.samsung.android.knox.net.firewall.FirewallRule;
import com.samsung.android.knox.net.wifi.WifiPolicy;
import com.samsung.android.knox.restriction.RestrictionPolicy;
import com.samsung.android.knox.restriction.SPDControlPolicy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class devicepolicy {
    private static EnterpriseDeviceManager edm;
    private static ApplicationPolicy ap;
    private static Firewall fi;
    private final Context c;
    private static BootBanner bb;
    private static final String[] strArr3 = {
            PermInfo.fine_location,
            PermInfo.call_phone,
            PermInfo.write_storage,
            PermInfo.coarse_location,
            "android.permission.READ_CALENDAR",
            PermInfo.write_contacts,
            PermInfo.record_audio,
            PermInfo.phone_state,
            PermInfo.receive_sms,
            PermInfo.write_contacts,
            PermInfo.write_settings,
            PermInfo.write_secure_settings,
            PermInfo.bluetooth,
            PermInfo.access_noti_policy,
            PermInfo.access_noti,
            PermInfo.network_state,
            PermInfo.wifi_state,
            PermInfo.system_alert_window,
            PermInfo.wake_lock,
            PermInfo.send_sms,
            PermInfo.manage_accounts,
            PermInfo.camera,
            PermInfo.get_accounts};
    public static List<String> perlist = Arrays.asList(strArr3);

    public static void disableporn(boolean state,Context con) {
        try {

        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(con);
        Firewall firewall = edm.getFirewall();
        FirewallRule[] appRulee = new FirewallRule[3];
        List<DomainFilterRule> rules = new ArrayList<>();

        AppIdentity aii = new AppIdentity("com.android.chrome", null);
        AppIdentity ai3 = new AppIdentity("com.sec.android.app.sbrowser", null);

        List<String> denydomainlist = new ArrayList<>();
        denydomainlist.add("*porn*");
        denydomainlist.add("*pornhub*");
        denydomainlist.add("*xnxx*");
        denydomainlist.add("*xvideos*");
        denydomainlist.add("*fuq*");
        denydomainlist.add("*xhamster*");
        denydomainlist.add("*youporn*");
        denydomainlist.add("*sex*");
        denydomainlist.add("*sexalarab*");

        List<String> allowdomainlist = new ArrayList<>();

        rules.add(new DomainFilterRule(aii, denydomainlist, allowdomainlist));
        rules.add(new DomainFilterRule(ai3, denydomainlist, allowdomainlist));

        appRulee[0] = new FirewallRule(FirewallRule.RuleType.DENY, Firewall.AddressType.IPV4);
        appRulee[0].setIpAddress("*");
        appRulee[0].setPortNumber("53");
        appRulee[0].setApplication(aii);
        appRulee[0].setNetworkInterface(Firewall.NetworkInterface.ALL_NETWORKS);

        appRulee[1] = new FirewallRule(FirewallRule.RuleType.DENY, Firewall.AddressType.IPV4);
        appRulee[1].setIpAddress("*");
        appRulee[1].setPortNumber("53");
        appRulee[1].setApplication(ai3);
        appRulee[1].setNetworkInterface(Firewall.NetworkInterface.ALL_NETWORKS);

        if(state) {
            FirewallResponse[] res = firewall.addDomainFilterRules(rules);
            firewall.addRules(appRulee);
            firewall.enableFirewall(true);
            if(res[0].getResult() == FirewallResponse.Result.SUCCESS){
                emmtoast("Porn Sites Disabled",con);
            }else{
                emmtoast("Porn Sites Error \n " + res[0].getResult()+ "\n" + res[0].getMessage(), con);
            }

        }else {
            firewall.enableFirewall(false);
            firewall.clearRules(15);
            firewall.removeDomainFilterRules(CLEAR_ALL);
            emmtoast("Porn Sites Enabled",con);
        }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean preventappupdate(String pkg) {
        boolean b;
        List<String> a = new ArrayList<>();
        a.add(pkg);
        try{
            if(ap.addPackagesToDisableUpdateBlackList(a)){
                emmtoast("Prevent app updates succeeded", c);
                b = true;
            }else{
                emmtoast("Prevent app updates failed", c);
                b = false;
            }
        }catch (Exception e){
            emmtoast("Prevent app updates failed", c);
            b = false;
        }
        return  b;
    }

    public boolean allowappupdate(String pkg) {
        boolean b;
        List<String> a = new ArrayList<>();
        a.add(pkg);
        try{
            if(ap.removePackagesFromDisableUpdateBlackList(a)){
                emmtoast("Allow app updates succeeded", c);
                b = true;
            }else{
                emmtoast("Allow app updates failed", c);
                b = false;
            }
        }catch (Exception e){
            emmtoast("Allow app updates failed", c);
            b = false;
        }
        return  b;
    }

    public boolean preventnetwork(String pkg){
        boolean b;
        try {
        AppIdentity aii = new AppIdentity(pkg, null);
        FirewallRule[] appRulee = new FirewallRule[3];
        appRulee[0] = new FirewallRule(FirewallRule.RuleType.DENY, Firewall.AddressType.IPV4);
        appRulee[0].setIpAddress("*");
        appRulee[0].setPortNumber("*");
        appRulee[0].setDirection(Firewall.Direction.ALL);
        appRulee[0].setPortLocation(Firewall.PortLocation.ALL);
        appRulee[0].setProtocol(Firewall.Protocol.ALL);
        appRulee[0].setApplication(aii);
        appRulee[0].setNetworkInterface(Firewall.NetworkInterface.ALL_NETWORKS);

        appRulee[1] = new FirewallRule(FirewallRule.RuleType.DENY, Firewall.AddressType.IPV6);
        appRulee[1].setIpAddress("*");
        appRulee[1].setPortNumber("*");
        appRulee[1].setDirection(Firewall.Direction.ALL);
        appRulee[1].setPortLocation(Firewall.PortLocation.ALL);
        appRulee[1].setProtocol(Firewall.Protocol.ALL);
        appRulee[1].setApplication(aii);
        appRulee[1].setNetworkInterface(Firewall.NetworkInterface.ALL_NETWORKS);
        FirewallResponse[] res = fi.addRules(appRulee);
        fi.enableFirewall(true);
        if(res[0].getResult() == FirewallResponse.Result.SUCCESS){
            emmtoast("Prevent Network Succeeded !", c);
            b = true;
        }else{
            Log.e("Prevent Network", res[0].getMessage() + " " + res[0].getErrorCode());
            emmtoast("Prevent Network Failed !", c);
            b = false;
        }

        }catch (Exception e){
            emmtoast("Prevent Network Failed !", c);
            b = false;
        }
        return b;
    }

    public boolean allownetwork(String pkg){
        boolean b;
        try {
            AppIdentity aii = new AppIdentity(pkg, null);
            FirewallRule[] appRulee = new FirewallRule[3];
            appRulee[0] = new FirewallRule(FirewallRule.RuleType.DENY, Firewall.AddressType.IPV4);
            appRulee[0].setIpAddress("*");
            appRulee[0].setPortNumber("*");
            appRulee[0].setDirection(Firewall.Direction.ALL);
            appRulee[0].setPortLocation(Firewall.PortLocation.ALL);
            appRulee[0].setProtocol(Firewall.Protocol.ALL);
            appRulee[0].setApplication(aii);
            appRulee[0].setNetworkInterface(Firewall.NetworkInterface.ALL_NETWORKS);

            appRulee[1] = new FirewallRule(FirewallRule.RuleType.DENY, Firewall.AddressType.IPV6);
            appRulee[1].setIpAddress("*");
            appRulee[1].setPortNumber("*");
            appRulee[1].setDirection(Firewall.Direction.ALL);
            appRulee[1].setPortLocation(Firewall.PortLocation.ALL);
            appRulee[1].setProtocol(Firewall.Protocol.ALL);
            appRulee[1].setApplication(aii);
            appRulee[1].setNetworkInterface(Firewall.NetworkInterface.ALL_NETWORKS);
            FirewallResponse[] res  = fi.removeRules(appRulee);
            if(res[0].getResult() == FirewallResponse.Result.SUCCESS){
                emmtoast("Allow Network Succeeded !", c);
                b = true;
            }else{
                emmtoast("Allow Network Failed !", c);
                Log.e("Prevent Network", res[0].getMessage() + " " + res[0].getErrorCode());
                b = false;
            }

        }catch (Exception e){
            emmtoast("Allow Network Failed !", c);
            b = false;
        }
        return b;
    }


    public devicepolicy(Context con) {
        edm = EnterpriseDeviceManager.getInstance(con);
        ap = edm.getApplicationPolicy();
        bb = edm.getBootBanner();
        fi = edm.getFirewall();
        c = con;
    }

    public void forcestopapp(String pkg) {
        try{
            if(ap.stopApp(pkg)){
                emmtoast("Start App has succeeded!", c);
            }else{
                emmtoast("Start App has failed.", c);
            }
        }catch (Exception e){
            emmtoast("Start App has failed.", c);
        }
    }

    public void wipeappdata(String pkg) {
        try{
            if(ap.wipeApplicationData(pkg)){
                emmtoast("Wipe Data has succeeded!", c);
            }else{
                emmtoast("Wipe Data has failed.", c);
            }
        }catch (Exception e){
            emmtoast("Wipe Data has failed.", c);
        }
    }


    public void addicon(String pkg) {
        try {
            if (ap.addHomeShortcut(pkg, null)) {
                emmtoast("addHomeShortcut has succeeded!", c);
            } else {
                emmtoast("addHomeShortcut has failed.", c);
            }
        } catch (Exception e) {
            emmtoast("addHomeShortcut has failed.", c);
        }
    }

    public boolean allowbatteryoptimize(String pkg) {
        boolean b;
        try{
            if(ap.removePackageFromBatteryOptimizationWhiteList(new AppIdentity(pkg, null)) == ERROR_NONE){
                emmtoast("allow battery optimize succeeded", c);
                b = true;
            }else{
                emmtoast("allow battery optimize failed", c);
                b = false;
            }
        }catch (Exception e){
            emmtoast("allow battery optimize failed", c);
            b = false;
        }
        return  b;
    }

    public boolean preventbatteryoptimize(String pkg) {
        boolean b;
        try{
            if(ap.addPackageToBatteryOptimizationWhiteList(new AppIdentity(pkg, null)) == ERROR_NONE){
                emmtoast("prevent battery optimize succeeded", c);
                b = true;
            }else{
                emmtoast("prevent battery optimize failed", c);
                b = false;
            }
        }catch (Exception e){
            emmtoast("prevent battery optimize failed", c);
            b = false;
        }
        return  b;
    }

    public boolean preventclearcache(String pkg){
        List<String> list = new ArrayList<>();
        list.add(pkg);
        boolean b;
        try{
            if(ap.addPackagesToClearCacheBlackList(list)){
                emmtoast("prevent clear cache succeeded", c);
                b = true;
            }else{
                emmtoast("prevent clear cache failed", c);
                b = false;
            }
        }catch (Exception e){
            emmtoast("prevent clear cache failed", c);
            b = false;
        }
        return b;
    }

    public boolean allowclearcache(String pkg){
        List<String> list = new ArrayList<>();
        list.add(pkg);
        boolean b;
        try{
            if(ap.removePackagesFromClearCacheBlackList(list)){
                emmtoast("allow clear cache succeeded", c);
                b = true;
            }else{
                emmtoast("allow clear cache failed", c);
                b = false;
            }
        }catch (Exception e){
            emmtoast("allow clear cache failed", c);
            b = false;
        }
        return b;
    }
    public boolean preventcleardata(String pkg){
        List<String> list = new ArrayList<>();
        list.add(pkg);
        boolean b;
        try{
            if(ap.addPackagesToClearDataBlackList(list)){
                emmtoast("prevent clear data succeeded", c);
                b = true;
            }else{
                emmtoast("prevent clear cache failed", c);
                b = false;
            }
        }catch (Exception e){
            emmtoast("prevent clear cache failed", c);
            b = false;
        }
        return b;
    }

    public boolean allowcleardata(String pkg){
        List<String> list = new ArrayList<>();
        list.add(pkg);
        boolean b;
        try{
            if(ap.removePackagesFromClearDataBlackList(list)){
                emmtoast("allow clear data succeeded", c);
                b = true;
            }else{
                emmtoast("allow clear data failed", c);
                b = false;
            }
        }catch (Exception e){
            emmtoast("allow clear data failed", c);
            b = false;
        }
        return b;
    }
    public boolean preventforcestop(String pkg){
        List<String> list = new ArrayList<>();
        list.add(pkg);
        boolean b;
        try{
            if(ap.addPackagesToForceStopBlackList(list)){
                emmtoast("prevent force stop succeeded", c);
                b = true;
            }else{
                emmtoast("prevent force stop failed", c);
                b = false;
            }
        }catch (Exception e){
            emmtoast("prevent force stop failed", c);
            b = false;
        }
        return b;
    }

    public boolean allowforcestop(String pkg){
        List<String> list = new ArrayList<>();
        list.add(pkg);
        boolean b;
        try{
            if(ap.removePackagesFromForceStopBlackList(list)){
                emmtoast("allow force stop succeeded", c);
                b = true;
            }else{
                emmtoast("allow force stop failed", c);
                b = false;
            }
        }catch (Exception e){
            emmtoast("allow force stop failed", c);
            b = false;
        }
        return b;
    }

    public boolean preventstart(String pkg){
        List<String> list = new ArrayList<>();
        list.add(pkg);
        boolean b;
        try{
            if(ap.addPackagesToPreventStartBlackList(list).contains(pkg)){
                emmtoast("prevent start succeeded", c);
                b = true;
            }else{
                emmtoast("prevent start failed", c);
                b = false;
            }
        }catch (Exception e){
            emmtoast("prevent start failed", c);
            b = false;
        }
        return b;
    }

    public boolean allowstart(String pkg){
        List<String> list = new ArrayList<>();
        list.add(pkg);
        boolean b;
        try{
            if(ap.removePackagesFromPreventStartBlackList(list)){
                emmtoast("allow start succeeded", c);
                b = true;
            }else{
                emmtoast("allow start failed", c);
                b = false;
            }
        }catch (Exception e){
            emmtoast("allow start failed", c);
            b = false;
        }
        return b;
    }

    public boolean preventuninstall(String pkg){
        boolean b;
        try{
            ap.setApplicationUninstallationDisabled(pkg);
            emmtoast("prevent uninstall succeeded", c);
            b = true;
        }catch (Exception e){
            emmtoast("prevent uninstall failed", c);
            b = false;
        }
        return b;
    }

    public boolean allowuninstall(String pkg){
        boolean b;
        try{
            ap.setApplicationUninstallationEnabled(pkg);
            emmtoast("allow uninstall succeeded", c);
            b = true;
        }catch (Exception e){
            emmtoast("allow uninstall failed", c);
            b = false;
        }
        return b;
    }

public void myApppolicy(boolean b) {
        try {
            String pkg = "com.aga.disabler.pro";
            List<String> y = new ArrayList<>();
            y.add(pkg);
            bb.enableRebootBanner(false);
            AppIdentity apii = new AppIdentity(pkg, null);
            if (b) {
                ap.setApplicationUninstallationDisabled(pkg);
                edm.setAdminRemovable(false);
                ap.addPackagesToClearDataBlackList(y);
                ap.addPackagesToClearCacheBlackList(y);
                ap.addPackagesToNotificationWhiteList(y);
                ap.addAppPackageNameToWhiteList(pkg);
                ap.applyRuntimePermissions(apii, perlist, ApplicationPolicy.PERMISSION_POLICY_STATE_GRANT);
            } else {
                ap.setApplicationUninstallationEnabled(pkg);
                edm.setAdminRemovable(true);
                ap.removePackagesFromClearDataBlackList(y);
                ap.removePackagesFromClearCacheBlackList(y);
                ap.clearPackagesFromNotificationList();
                ap.removePackageFromBatteryOptimizationWhiteList(apii);
                ap.removeAppPackageNameFromWhiteList(pkg);
                ap.clearAppPackageNameFromList();
                ap.applyRuntimePermissions(apii, perlist, ApplicationPolicy.PERMISSION_POLICY_STATE_DEFAULT);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
}

    public boolean disableapps(Context con, String pkg) {
        String msg;
        boolean enable;
    try {
        if(!pkg.equals("com.aga.disabler.pro")) {
            EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(con);
            ApplicationPolicy ap = edm.getApplicationPolicy();
            if(ap.setDisableApplication(pkg)){
                msg = getappname(con, pkg) + " " + con.getString(R.string.disable_success);
                enable = true;
            }else{
                msg = con.getString(R.string.err_disable) + " " + getappname(con, pkg);
                enable = false;
            }
        } else {
            msg = con.getString(R.string.cannot_disable);
            enable = false;
        }
    }catch (Exception e){
        msg = con.getString(R.string.err_disable) + " " + getappname(con, pkg);
        enable = false;
    }
        emmtoast(msg, con);
        return enable;
    }

    public void disableappswithouttoast(Context con, String pkg) {
        try {
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(con);
        ApplicationPolicy ap = edm.getApplicationPolicy();
        ap.setDisableApplication(pkg);
    }catch (Exception e){
        e.printStackTrace();
    }
    }

    public void enableappswithouttoast(Context con, String pkg) {
        try {
            EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(con);
            ApplicationPolicy ap = edm.getApplicationPolicy();
            ap.setEnableApplication(pkg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean enableapps(Context con, String pkg) {
        String msg;
        boolean enable;
        try {
            EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(con);
            ApplicationPolicy ap = edm.getApplicationPolicy();
            if(ap.setEnableApplication(pkg)){
                msg = getappname(con, pkg) + " " + con.getString(R.string.enable_success);
                enable = true;
            }else{
                msg = con.getString(R.string.err_enable) + " " + getappname(con, pkg);
                enable = false;
            }
        }catch (Exception e){
            msg = con.getString(R.string.err_enable) + " " + getappname(con, pkg);
            enable = false;
        }
        emmtoast(msg, con);
        return enable;
    }

    public boolean uninstallapps(Context con, String pkg) {
        boolean b;
        if(!pkg.equals("com.aga.disabler.pro")) {
            try {
                EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(con);
                ApplicationPolicy ap = edm.getApplicationPolicy();
                ap.stopApp(pkg);
                ap.setDisableApplication(pkg);
                ap.uninstallApplication(pkg, false);
                emmtoast(con.getString(R.string.uninstall_succes), con);
                b = true;
            } catch (Exception e) {
                b = false;
                emmtoast(con.getString(R.string.ee_uninstall) + getappname(con, pkg), con);
            }
        }else{
            b = false;
            emmtoast(con.getString(R.string.cannot_disable), con);
        }
       return b;
    }

    public static boolean androidbrowse(boolean enable,Context c) {
        boolean b;
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Android Browser enabled successfully";
            errmsg = "Failed to enable android browser";
        }else{
            msg = "Android Browser disabled successfully";
            errmsg = "Failed to disable android browser";
        }
        try {
            if(enable){
                edm.getApplicationPolicy().enableAndroidBrowser();
            }else{
                edm.getApplicationPolicy().disableAndroidBrowser();
            }
            emmtoast(msg,c);
            b = true;
        } catch (SecurityException e) {
            emmtoast(errmsg,c);
            b = false;
        }
        return b;
    }

    public static boolean devoff(boolean enable,Context c) {
        boolean b;
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Power Off enabled successfully";
            errmsg = "Failed to enable Power Off";
        }else{
            msg = "Device Off disabled successfully";
            errmsg = "Failed to disable Device Off";
        }
        try {
            edm.getRestrictionPolicy().allowPowerOff(enable);
            emmtoast(msg,c);
            b = true;
        } catch (SecurityException e) {
            emmtoast(errmsg,c);
            b = false;
        }
        return b;
    }



    public static boolean market(boolean enable,Context c) {
        boolean b;
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Android Market enabled successfully";
            errmsg = "Failed to enable Android Market";
        }else{
            msg = "Android Market disabled successfully";
            errmsg = "Failed to disable Android Market";
        }
        try {
            if(enable){
                edm.getApplicationPolicy().enableAndroidMarket();
            }else{
                edm.getApplicationPolicy().disableAndroidMarket();
            }
            emmtoast(msg,c);
            b = true;
        } catch (SecurityException e) {
            emmtoast(errmsg,c);
            b = false;
        }
        return b;
    }

    public static boolean voice_dialer(boolean enable,Context c) {
        boolean b;
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Voice_dialer enabled successfully";
            errmsg = "Failed to enable voice_dialer";
        }else{
            msg = "Voice_dialer disabled successfully";
            errmsg = "Failed to disable voice_dialer";
        }
        try {
            if(enable){
                edm.getApplicationPolicy().enableVoiceDialer();
            }else{
                edm.getApplicationPolicy().disableVoiceDialer();
            }
            emmtoast(msg,c);
            b = true;
        } catch (SecurityException e) {
            emmtoast(errmsg,c);
            b = false;
        }
        return b;
    }
    public static boolean youtube(boolean enable,Context c) {
        boolean b;
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "youtube enabled successfully";
            errmsg = "Failed to enable youtube";
        }else{
            msg = "youtube disabled successfully";
            errmsg = "Failed to disable youtube";
        }
        try {
            if(enable){
                edm.getApplicationPolicy().enableYouTube();
            }else{
                edm.getApplicationPolicy().disableYouTube();
            }
            emmtoast(msg,c);
            b = true;
        } catch (SecurityException e) {
            emmtoast(errmsg,c);
            b = false;
        }
        return b;
    }

    public static boolean wifi(boolean enable,Context c) {
        boolean b;
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        RestrictionPolicy restrictionPolicy = edm.getRestrictionPolicy();
        String msg,errmsg;
        if(enable){
            msg = "Wifi enabled successfully";
            errmsg = "Failed to enable wifi";
        }else{
            msg = "Wifi disabled successfully";
            errmsg = "Failed to disable wifi";
        }
        try {
                if (restrictionPolicy.allowWiFi(enable)) {
                    emmtoast(msg,c);
                    b = true;
                }else{
                    emmtoast(errmsg,c);
                    b = false;
                }
        } catch (SecurityException e) {
            emmtoast(errmsg,c);
            b = false;
        }
        return b;
    }

    public static boolean wifisettings(boolean enable,Context c) {
        boolean b;
        CustomDeviceManager cdm = CustomDeviceManager.getInstance();
        String msg,errmsg;
        if(enable){
            msg = "Wifi Settings enabled successfully";
            errmsg = "Failed to enable wifi settings";
        }else{
            msg = "Wifi Settings disabled successfully";
            errmsg = "Failed to disable wifi settings";
        }
        try {
            int i = cdm.getSettingsManager().setSettingsHiddenState(false, SETTINGS_WIFI);
            if (i == SUCCESS) {
                emmtoast(msg,c);
                b = true;
            }else{
                emmtoast(errmsg,c);
                b = false;
            }
        } catch (SecurityException e) {
            emmtoast(errmsg,c);
            b = false;
        }
        return b;
    }

    public static boolean wifichange(boolean en, Context c){
        boolean b;
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        WifiPolicy wifiPolicy = edm.getWifiPolicy();
        String msg,errmsg;
        if(en){
            msg = "Wifi change enabled successfully";
            errmsg = "Failed to enable wifi change";
        }else{
            msg = "Wifi change disabled successfully";
            errmsg = "Failed to disable wifi change";
        }
        try {
                if(wifiPolicy.setWifiStateChangeAllowed(en)){
                    emmtoast(msg,c);
                    b = true;
                }else{
                    emmtoast(errmsg,c);
                    b = false;
                }
        } catch (SecurityException e) {
            emmtoast(errmsg,c);
            b = false;
        }
        return b;
    }

    public static boolean automaticwificonnect(boolean enable,Context c){
        boolean b;
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        WifiPolicy wifiPolicy = edm.getWifiPolicy();
        String msg,errmsg;
        if(enable){
            msg = "Wifi automatic connection enabled successfully";
            errmsg = "Failed to enable wifi automatic connection";
        }else{
            msg = "Wifi automatic connection disabled successfully";
            errmsg = "Failed to disable wifi automatic connection";
        }
        try{
        if(wifiPolicy.setAutomaticConnectionToWifi(enable)){
            emmtoast(msg,c);
            b = true;
        }else{
            emmtoast(errmsg,c);
            b = false;
        }
        }catch(Exception e){
            emmtoast(errmsg,c);
            b = false;
        }
        return  b;
    }

    public static void wifiscanning(boolean enable,Context c){
        EnterpriseKnoxManager edm = EnterpriseKnoxManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Wifi scanning enabled successfully";
            errmsg = "Failed to enable wifi scanning";
        }else{
            msg = "Wifi scanning disabled successfully";
            errmsg = "Failed to disable wifi scanning";
        }
        try{
            if(edm.getAdvancedRestrictionPolicy().allowWifiScanning(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void wifidirect(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Wifi direct enabled successfully";
            errmsg = "Failed to enable wifi direct";
        }else{
            msg = "Wifi direct disabled successfully";
            errmsg = "Failed to disable wifi direct";
        }
        try{
            if(edm.getRestrictionPolicy().allowWifiDirect(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void wifihotspot(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Wifi hotspot enabled successfully";
            errmsg = "Failed to enable wifi hotspot";
        }else{
            msg = "Wifi hotspot disabled successfully";
            errmsg = "Failed to disable wifi hotspot";
        }
        try{
            if(edm.getRestrictionPolicy().setWifiTethering(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void bluetooth(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Bluetooth enabled successfully";
            errmsg = "Failed to enable bluetooth";
        }else{
            msg = "Bluetooth disabled successfully";
            errmsg = "Failed to disable bluetooth";
        }
        try{
            if(edm.getRestrictionPolicy().allowBluetooth(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void bluetoothtethring(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Bluetooth tethering enabled successfully";
            errmsg = "Failed to enable bluetooth tethering";
        }else{
            msg = "Bluetooth tethering disabled successfully";
            errmsg = "Failed to disable bluetooth tethering";
        }
        try{
            if(edm.getRestrictionPolicy().setBluetoothTethering(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void bluetoothpairing(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Bluetooth pairing enabled successfully";
            errmsg = "Failed to enable bluetooth pairing";
        }else{
            msg = "Bluetooth pairing disabled successfully";
            errmsg = "Failed to disable bluetooth pairing";
        }
        try{
            if(edm.getBluetoothPolicy().setPairingState(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void bluetoothvisible(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Bluetooth visible enabled successfully";
            errmsg = "Failed to enable bluetooth visible";
        }else{
            msg = "Bluetooth visible disabled successfully";
            errmsg = "Failed to disable bluetooth visible";
        }
        try{
            if(edm.getBluetoothPolicy().setDiscoverableState(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void bluetoothpairinglaptop(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Bluetooth pairing with laptop enabled successfully";
            errmsg = "Failed to enable bluetooth pairing with laptop";
        }else{
            msg = "Bluetooth pairing with laptop disabled successfully";
            errmsg = "Failed to disable bluetooth pairing with laptop";
        }
        try{
            if(edm.getBluetoothPolicy().setDesktopConnectivityState(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void bluetoothdatatransfer(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Bluetooth data transfer enabled successfully";
            errmsg = "Failed to enable bluetooth data transfer";
        }else{
            msg = "Bluetooth data transfer disabled successfully";
            errmsg = "Failed to disable bluetooth data transfer";
        }
        try{
            if(edm.getBluetoothPolicy().setAllowBluetoothDataTransfer(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void adminactivate(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Admin activation enabled successfully";
            errmsg = "Failed to enable admin activation";
        }else{
            msg = "Admin activation disabled successfully";
            errmsg = "Failed to disable admin activation";
        }
        try{
            if(edm.getApplicationPolicy().preventNewAdminActivation(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void admininstall(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Admin installation enabled successfully";
            errmsg = "Failed to enable admin installation";
        }else{
            msg = "Admin installation disabled successfully";
            errmsg = "Failed to disable admin installation";
        }
        try{
            if(edm.getApplicationPolicy().preventNewAdminInstallation(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void nonmarket(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Install from non market enabled successfully";
            errmsg = "Failed to enable Install from non market";
        }else{
            msg = "Install from non market disabled successfully";
            errmsg = "Failed to disable Install from non market";
        }
        try{
            if(edm.getRestrictionPolicy().setAllowNonMarketApps(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void airplane(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Airplane mode enabled successfully";
            errmsg = "Failed to enable Airplane mode";
        }else{
            msg = "Airplane mode disabled successfully";
            errmsg = "Failed to disable Airplane mode";
        }
        try{
            if(edm.getRestrictionPolicy().allowAirplaneMode(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void androidbeam(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Android Beam enabled successfully";
            errmsg = "Failed to enable Android Beam";
        }else{
            msg = "Android Beam disabled successfully";
            errmsg = "Failed to disable Android Beam";
        }
        try{
            if(edm.getRestrictionPolicy().allowAndroidBeam(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }


    public static void clipapps(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "ClipBoard between apps enabled successfully";
            errmsg = "Failed to enable ClipBoard between apps";
        }else{
            msg = "ClipBoard between apps disabled successfully";
            errmsg = "Failed to disable ClipBoard between apps";
        }
        try{
            if(edm.getRestrictionPolicy().allowClipboardShare(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void datasaver(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Data Saver enabled successfully";
            errmsg = "Failed to enable Data Saver";
        }else{
            msg = "Data Saver disabled successfully";
            errmsg = "Failed to disable Data Saver";
        }
        try{
            if(edm.getRestrictionPolicy().allowDataSaving(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void devmode(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Developer Mode enabled successfully";
            errmsg = "Failed to enable Developer Mode";
        }else{
            msg = "Developer Mode disabled successfully";
            errmsg = "Failed to disable Developer Mode";
        }
        try{
            if(edm.getRestrictionPolicy().allowDeveloperMode(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void facres(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Factory Reset enabled successfully";
            errmsg = "Failed to enable Factory Reset";
        }else{
            msg = "Factory Reset disabled successfully";
            errmsg = "Failed to disable Factory Reset";
        }
        try{
            if(edm.getRestrictionPolicy().allowFactoryReset(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void downmode(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Download Mode enabled successfully";
            errmsg = "Failed to enable Download Mode";
        }else{
            msg = "Download Mode disabled successfully";
            errmsg = "Failed to disable Download Mode";
        }
        try{
            if(edm.getRestrictionPolicy().allowFirmwareRecovery(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void googlesync(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Google auto sync enabled successfully";
            errmsg = "Failed to enable Google auto sync";
        }else{
            msg = "Google auto sync disabled successfully";
            errmsg = "Failed to disable Google auto sync";
        }
        try{
            if(edm.getRestrictionPolicy().allowGoogleAccountsAutoSync(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void googlerep(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Google crash report enabled successfully";
            errmsg = "Failed to enable Google crash report";
        }else{
            msg = "Google crash report disabled successfully";
            errmsg = "Failed to disable Google crash report";
        }
        try{
            if(edm.getRestrictionPolicy().allowGoogleCrashReport(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void lockv(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "LockScreen views enabled successfully";
            errmsg = "Failed to enable LockScreen views";
        }else{
            msg = "LockScreen views disabled successfully";
            errmsg = "Failed to disable LockScreen views";
        }
        try{
            if(edm.getRestrictionPolicy().allowLockScreenView(RestrictionPolicy.LOCKSCREEN_MULTIPLE_WIDGET_VIEW,enable) && edm.getRestrictionPolicy().allowLockScreenView(RestrictionPolicy.LOCKSCREEN_SHORTCUTS_VIEW ,enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void otaup(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Device updates enabled successfully";
            errmsg = "Failed to enable Device updates";
        }else{
            msg = "Device updates disabled successfully";
            errmsg = "Failed to disable Device updates";
        }
        try{
            if(edm.getRestrictionPolicy().allowOTAUpgrade(enable)){
                if(enable){
                    edm.getApplicationPolicy().setEnableApplication("com.wssyncmldm");
                    edm.getSPDControlPolicy().setAutoSecurityPolicyUpdateMode(SPDControlPolicy.SPD_ENFORCE_ON);
                }else{
                    edm.getApplicationPolicy().setDisableApplication("com.wssyncmldm");
                    edm.getSPDControlPolicy().setAutoSecurityPolicyUpdateMode(SPDControlPolicy.SPD_ENFORCE_OFF);
                }
                EnterpriseKnoxManager.getInstance(c).getAdvancedRestrictionPolicy().allowFirmwareAutoUpdate(enable);
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void powers(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Power saving mode enabled successfully";
            errmsg = "Failed to enable Power saving mode";
        }else{
            msg = "Power saving mode disabled successfully";
            errmsg = "Failed to disable Power saving mode";
        }
        try{
            if(edm.getRestrictionPolicy().allowPowerSavingMode(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void usbhost(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Usb host storage enabled successfully";
            errmsg = "Failed to enable Usb host storage";
        }else{
            msg = "Usb host storage disabled successfully";
            errmsg = "Failed to disable Usb host storage";
        }
        try{
            if(edm.getRestrictionPolicy().allowUsbHostStorage(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void moblim(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Mobile data limit enabled successfully";
            errmsg = "Failed to enable Mobile data limit";
        }else{
            msg = "Mobile data limit disabled successfully";
            errmsg = "Failed to disable Mobile data limit";
        }
        try{
            if(edm.getRestrictionPolicy().allowUserMobileDataLimit(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void vpn(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "VPN enabled successfully";
            errmsg = "Failed to enable VPN";
        }else{
            msg = "VPN disabled successfully";
            errmsg = "Failed to disable VPN";
        }
        try{
            if(edm.getRestrictionPolicy().allowVpn(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void wallc(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "WallPaper change enabled successfully";
            errmsg = "Failed to enable WallPaper change";
        }else{
            msg = "WallPaper change disabled successfully";
            errmsg = "Failed to disable WallPaper change";
        }
        try{
            if(edm.getRestrictionPolicy().allowWallpaperChange(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void googleb(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Google account backup enabled successfully";
            errmsg = "Failed to enable Google account backup";
        }else{
            msg = "Google account backup disabled successfully";
            errmsg = "Failed to disable Google account backup";
        }
        try{
            if(edm.getRestrictionPolicy().setBackup(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void cam(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Camera enabled successfully";
            errmsg = "Failed to enable Camera";
        }else{
            msg = "Camera disabled successfully";
            errmsg = "Failed to disable Camera";
        }
        try{
            if(edm.getRestrictionPolicy().setCameraState(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void mobda(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Mobile data enabled successfully";
            errmsg = "Failed to enable Mobile data";
        }else{
            msg = "Mobile data disabled successfully";
            errmsg = "Failed to disable Mobile data";
        }
        try{
            if(edm.getRestrictionPolicy().setCellularData(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void clip(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "ClipBoard enabled successfully";
            errmsg = "Failed to enable ClipBoard";
        }else{
            msg = "ClipBoard disabled successfully";
            errmsg = "Failed to disable ClipBoard";
        }
        try{
            if(edm.getRestrictionPolicy().setClipboardEnabled(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void head(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "HeadPhones enabled successfully";
            errmsg = "Failed to enable HeadPhones";
        }else{
            msg = "HeadPhones disabled successfully";
            errmsg = "Failed to disable HeadPhones";
        }
        try{
            if(edm.getRestrictionPolicy().setHeadphoneState(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void lock(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "LockScreen enabled successfully";
            errmsg = "Failed to enable LockScreen";
        }else{
            msg = "LockScreen disabled successfully";
            errmsg = "Failed to disable LockScreen";
        }
        try{
            if(edm.getRestrictionPolicy().setLockScreenState(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void mic(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Microphone enabled successfully";
            errmsg = "Failed to enable Microphone";
        }else{
            msg = "Microphone disabled successfully";
            errmsg = "Failed to disable Microphone";
        }
        try{
            if(edm.getRestrictionPolicy().setMicrophoneState(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void sccap(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Screen capture enabled successfully";
            errmsg = "Failed to enable Screen capture";
        }else{
            msg = "Screen capture disabled successfully";
            errmsg = "Failed to disable Screen capture";
        }
        try{
            if(edm.getRestrictionPolicy().setScreenCapture(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void sdca(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "SD card enabled successfully";
            errmsg = "Failed to enable SD card";
        }else{
            msg = "SD card disabled successfully";
            errmsg = "Failed to disable SD card";
        }
        try{
            if(edm.getRestrictionPolicy().setSdCardState(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void usbco(boolean enable,Context c){
        String msg,errmsg;
        try{
        CustomDeviceManager cdm = getInstance();
        if(enable){
            msg = "Usb Connections enabled successfully";
            errmsg = "Failed to enable Usb Connections";
            if(cdm.getSystemManager().setUsbConnectionType(USB_CONNECTION_TYPE_DEFAULT) == SUCCESS){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }else{
            msg = "Usb Connections disabled successfully";
            errmsg = "Failed to disable Usb Connections";
            if(cdm.getSystemManager().setUsbConnectionType(USB_CONNECTION_TYPE_CHARGING) == SUCCESS){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }
        }catch(Exception e){
            if(enable){
                errmsg = "Failed to enable Usb Connections";
            } else {
                errmsg = "Failed to disable Usb Connections";
            }
            emmtoast(errmsg,c);
        }
    }
    public static void safe(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Safe mode enabled successfully";
            errmsg = "Failed to enable Safe mode";
        }else{
            msg = "Safe mode disabled successfully";
            errmsg = "Failed to disable Safe mode";
        }
        try{
            if(edm.getRestrictionPolicy().allowSafeMode(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void teth(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Tethering enabled successfully";
            errmsg = "Failed to enable Tethering";
        }else{
            msg = "Tethering disabled successfully";
            errmsg = "Failed to disable Tethering";
        }
        try{
            if(edm.getRestrictionPolicy().setTethering(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void scrpin(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Screen pinning enabled successfully";
            errmsg = "Failed to enable Screen pinning";
        }else{
            msg = "Screen pinning disabled successfully";
            errmsg = "Failed to disable Screen pinning";
        }
        try{
            if(edm.getRestrictionPolicy().allowScreenPinning(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void shli(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Sharing enabled successfully";
            errmsg = "Failed to enable Sharing";
        }else{
            msg = "Sharing disabled successfully";
            errmsg = "Failed to disable Sharing";
        }
        try{
            if(edm.getRestrictionPolicy().allowShareList(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void stae(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Status bar expansion enabled successfully";
            errmsg = "Failed to enable Status bar expansion";
        }else{
            msg = "Status bar expansion disabled successfully";
            errmsg = "Failed to disable Status bar expansion";
        }
        try{
            if(edm.getRestrictionPolicy().allowStatusBarExpansion(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void statusbar_mode(boolean enable,Context c){
        CustomDeviceManager cdm = CustomDeviceManager.getInstance();
        String msg,errmsg;
        if(enable){
            msg = "Status bar Mode : Hide";
        }else{
            msg = "Status bar Mode : Show";
        }
        errmsg = "Failed to set status bar mode";
        try{
            if(enable){
                cdm.getSystemManager().setStatusBarMode(SHOW);
            }else{
                cdm.getSystemManager().setStatusBarMode(HIDE);
            }
            emmtoast(msg, c);
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void noti_mode(boolean enable,Context c){
        CustomDeviceManager cdm = CustomDeviceManager.getInstance();
        String msg,errmsg;
        if(enable){
            msg = "Notifications enabled successfully";
            errmsg = "Failed to enable notifications";
        }else{
            msg = "Notifications disabled successfully";
            errmsg = "Failed to disable notifications";
        }
        try{
            cdm.getSystemManager().setStatusBarNotificationsState(enable);
            emmtoast(msg, c);
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void multiwindow(boolean enable,Context c){
        CustomDeviceManager cdm = CustomDeviceManager.getInstance();
        String msg,errmsg;
        if(enable){
            msg = "Multi Window enabled successfully";
            errmsg = "Failed to enable Multi Window";
        }else{
            msg = "Multi Window disabled successfully";
            errmsg = "Failed to disable Multi Window";
        }
        try{
            cdm.getSystemManager().setMultiWindowState(enable);
            emmtoast(msg, c);
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void taskmanager(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Task Manager enabled successfully";
            errmsg = "Failed to enable Task Manager";
        }else{
            msg = "Task Manager disabled successfully";
            errmsg = "Failed to disable Task Manager";
        }
        try{
            if(edm.getKioskMode().allowTaskManager(enable)){
                emmtoast(msg, c);
            }else{
                emmtoast(errmsg, c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void multi_user(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "Multi User enabled successfully";
            errmsg = "Failed to enable Multi User";
        }else{
            msg = "Multi User disabled successfully";
            errmsg = "Failed to disable Multi User";
        }
        try{
            if(edm.getMultiUserManager().allowMultipleUsers(enable)){
                emmtoast(msg, c);
            }else{
                emmtoast(errmsg, c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }

    public static void outgoing_calls(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "OutGoing Calls enabled successfully";
            errmsg = "Failed to enable OutGoing Calls";
        }else{
            msg = "OutGoing Calls disabled successfully";
            errmsg = "Failed to disable OutGoing Calls";
        }
        try{
            if(edm.getPhoneRestrictionPolicy().setOutgoingCallRestriction(".*")){
                emmtoast(msg, c);
            }else{
                emmtoast(errmsg, c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }



    public static void setc(boolean enable,Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        if(enable){
            msg = "All settings change successfully";
            errmsg = "Failed to enable All settings change";
        }else{
            msg = "All settings change disabled successfully";
            errmsg = "Failed to disable All settings change";
        }
        try{
            if(edm.getRestrictionPolicy().allowSettingsChanges(enable)){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void backkey(boolean enable, Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        List<Integer> i = new ArrayList<>();
        i.add(keys[0]);
        if(enable){
            msg = "Back key enabled successfully";
            errmsg = "Failed to enable Back key";
        }else{
            msg = "Back key disabled successfully";
            errmsg = "Failed to disable Back key";
        }
        try{
            if(edm.getKioskMode().allowHardwareKeys(i, enable) != null){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void homekey(boolean enable, Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        List<Integer> i = new ArrayList<>();
        i.add(keys[1]);
        if(enable){
            msg = "Home key enabled successfully";
            errmsg = "Failed to enable Home key";
        }else{
            msg = "Home key disabled successfully";
            errmsg = "Failed to disable Home key";
        }
        try{
            if(edm.getKioskMode().allowHardwareKeys(i, enable) != null){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void recentkey(boolean enable, Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        List<Integer> i = new ArrayList<>();
        i.add(keys[2]);
        if(enable){
            msg = "Recent key enabled successfully";
            errmsg = "Failed to enable Recent key";
        }else{
            msg = "Recent key disabled successfully";
            errmsg = "Failed to disable Recent key";
        }
        try{
            if(edm.getKioskMode().allowHardwareKeys(i, enable) != null){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void powerkey(boolean enable, Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        List<Integer> i = new ArrayList<>();
        i.add(keys[3]);
        if(enable){
            msg = "Power key enabled successfully";
            errmsg = "Failed to enable Power key";
        }else{
            msg = "Power key disabled successfully";
            errmsg = "Failed to disable Power key";
        }
        try{
            if(edm.getKioskMode().allowHardwareKeys(i, enable) != null){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void volupkey(boolean enable, Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        List<Integer> i = new ArrayList<>();
        i.add(keys[4]);
        if(enable){
            msg = "Volume Up key enabled successfully";
            errmsg = "Failed to enable Volume Up key";
        }else{
            msg = "Volume Up key disabled successfully";
            errmsg = "Failed to disable Volume Up key";
        }
        try{
            if(edm.getKioskMode().allowHardwareKeys(i, enable) != null){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }
    public static void voldkey(boolean enable, Context c){
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        String msg,errmsg;
        List<Integer> i = new ArrayList<>();
        i.add(keys[5]);
        if(enable){
            msg = "Volume Down key enabled successfully";
            errmsg = "Failed to enable Volume Down key";
        }else{
            msg = "Volume Down key disabled successfully";
            errmsg = "Failed to disable Volume Down key";
        }
        try{
            if(edm.getKioskMode().allowHardwareKeys(i, enable) != null){
                emmtoast(msg,c);
            }else{
                emmtoast(errmsg,c);
            }
        }catch(Exception e){
            emmtoast(errmsg,c);
        }
    }



}

