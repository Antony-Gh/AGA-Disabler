package com.aga.disabler.pro.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.receiver.devicepolicy;
import com.aga.disabler.pro.tools.Helper;
import com.aga.disabler.pro.tools.appinfo;
import com.samsung.android.knox.EnterpriseDeviceManager;
import com.samsung.android.knox.application.ApplicationPolicy;
import com.samsung.android.knox.net.firewall.Firewall;
import com.samsung.android.knox.net.firewall.FirewallRule;

import org.jetbrains.annotations.NotNull;

import static com.aga.disabler.pro.tools.Helper.createanapk;
import static com.aga.disabler.pro.tools.Helper.shareapp;

public class ActionFragment extends FragmentHolder {
    private Button ex_apk;
    private Button ex_icon;
    private Button share_but;
    private Button prev_start;
    private Button add_icon;
    private Button prev_batt;
    private Button prev_cache;
    private Button prev_data;
    private Button prev_stop;
    private Button prev_uni;
    private Button wipedata;
    private Button stopapp;
    private Button blc_net;
    private Button blc_upd;
    private Button ext_mani;
    private boolean batteryoptimize;
    private boolean clearcache;
    private boolean cleardata;
    private boolean forcestop;
    private boolean preventstart;
    private boolean preventuninstall;
    private boolean prevnet;
    private boolean prevupd;
    private devicepolicy dp;
    private appinfo appinf;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.actions_fragment, container, false);
        init();
        initviews();
        return view;
    }

    public static ActionFragment getInstance(Context context, String pkgname) {
        ActionFragment fragment = new ActionFragment();
        fragment.setContext(context);
        fragment.setTitle("Actions");
        fragment.setpkg(pkgname);
        return fragment;
    }



    private void init() {
        dp = new devicepolicy(c);
        appinf = new appinfo(c, pkg);
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        ApplicationPolicy ap = edm.getApplicationPolicy();
        batteryoptimize = ap.getPackagesFromBatteryOptimizationWhiteList().contains(pkg);
        clearcache = ap.getPackagesFromClearCacheBlackList().contains(pkg);
        cleardata = ap.getPackagesFromClearDataBlackList().contains(pkg);
        forcestop = ap.getPackagesFromForceStopBlackList().contains(pkg);
        preventstart = ap.getPackagesFromPreventStartBlackList().contains(pkg);
        preventuninstall = ap.getApplicationUninstallationEnabled(pkg);
        prevnet = checknet();
        prevupd = ap.getPackagesFromDisableUpdateBlackList().contains(pkg);
        ex_apk = view.findViewById(R.id.ex_apk);
        ex_icon = view.findViewById(R.id.ex_icon);
        share_but = view.findViewById(R.id.share_but);
        prev_start = view.findViewById(R.id.prev_start);
        add_icon = view.findViewById(R.id.add_icon);
       prev_batt = view.findViewById(R.id.prev_batt);
        prev_cache = view.findViewById(R.id.prev_cache);
       prev_data = view.findViewById(R.id.prev_data);
        prev_stop = view.findViewById(R.id.prev_stop);
        prev_uni = view.findViewById(R.id.prev_uninstall);
        wipedata = view.findViewById(R.id.wipe_data);
        stopapp = view.findViewById(R.id.stop_app);
        blc_net = view.findViewById(R.id.blc_net);
        blc_upd = view.findViewById(R.id.blc_upd);
        ext_mani = view.findViewById(R.id.ext_mani);
        if(batteryoptimize){prev_batt.setText(R.string.allow_battery_optimize);}else{prev_batt.setText(R.string.prevent_battery_optimize); }
        if(clearcache){prev_cache.setText(R.string.allow_clear_cache);}else{prev_cache.setText(R.string.prevent_clear_cache); }
        if(cleardata){prev_data.setText(R.string.allow_clear_data);}else{prev_data.setText(R.string.prevent_clear_data); }
        if(forcestop){prev_stop.setText(R.string.allow_force_stop);}else{prev_stop.setText(R.string.prevent_close_or_force_stop); }
        if(preventuninstall){prev_uni.setText(R.string.prevent_uninstall);}else{prev_uni.setText(R.string.allow_uninstall); }
        if(preventstart){prev_start.setText(R.string.allow_start);}else{prev_start.setText(R.string.prevent_start); }
        if(prevnet){blc_net.setText(R.string.allow_network);}else{blc_net.setText(R.string.prevent_network);}
        if(prevupd){blc_upd.setText(R.string.allow_app_updates);}else{blc_upd.setText(R.string.prevent_app_updates);}
    }

    private void initviews() {
        ex_apk.setOnClickListener(v -> createanapk(pkg, c));
        ex_icon.setOnClickListener(v -> appinf.persistImage());
        share_but.setOnClickListener(v -> shareapp(pkg, c));
        ext_mani.setOnClickListener(view -> Helper.extractmanifest(c,pkg));
        prev_start.setOnClickListener(v -> {
            if(preventstart){
                preventstart = false;
                if(dp.allowstart(pkg)) prev_start.setText(R.string.prevent_start);
            }else{
                preventstart = true;
                if(dp.preventstart(pkg)) prev_start.setText(R.string.allow_start);
            }
        });
        add_icon.setOnClickListener(v -> dp.addicon(pkg));
        prev_batt.setOnClickListener(v -> {
            if(batteryoptimize){
                batteryoptimize = false;
                if(dp.allowbatteryoptimize(pkg)) prev_batt.setText(R.string.prevent_battery_optimize);
            }else{
                batteryoptimize = true;
                if(dp.preventbatteryoptimize(pkg)) prev_batt.setText(R.string.allow_battery_optimize);
            }
        });
        prev_cache.setOnClickListener(v -> {
            if(clearcache){
                clearcache = false;
                if(dp.allowclearcache(pkg)) prev_cache.setText(R.string.prevent_clear_cache);
            }else{
                clearcache = true;
                if(dp.preventclearcache(pkg)) prev_cache.setText(R.string.allow_clear_cache);
            }
        });
        prev_data.setOnClickListener(v -> {
            if(cleardata){
                cleardata = false;
                if(dp.allowcleardata(pkg)) prev_data.setText(R.string.prevent_clear_data);
            }else{
                cleardata = true;
                if(dp.preventcleardata(pkg)) prev_data.setText(R.string.allow_clear_data);
            }
        });
        prev_stop.setOnClickListener(v -> {
            if(forcestop){
                forcestop = false;
                if(dp.allowforcestop(pkg)) prev_stop.setText(R.string.prevent_close_or_force_stop);
            }else{
                forcestop = true;
                if(dp.preventforcestop(pkg)) prev_stop.setText(R.string.allow_force_stop);
            }
        });
       prev_uni.setOnClickListener(v -> {
            if(preventuninstall){
                preventuninstall = false;
                if(dp.preventuninstall(pkg)) prev_uni.setText(R.string.allow_uninstall);
            }else{
                preventuninstall = true;
                if(dp.allowuninstall(pkg)) prev_uni.setText(R.string.prevent_uninstall);
            }
        });
       blc_net.setOnClickListener(view -> {
           if(prevnet){
               prevnet = false;
               if(dp.allownetwork(pkg)) blc_net.setText(R.string.prevent_network);
           }else{
               prevnet = true;
               if(dp.preventnetwork(pkg)) blc_net.setText(R.string.allow_network);
           }
       });
        blc_upd.setOnClickListener(view -> {
            if(prevupd){
                prevupd = false;
                if(dp.allowappupdate(pkg)) blc_upd.setText(R.string.prevent_app_updates);
            }else{
                prevupd = true;
                if(dp.preventappupdate(pkg)) blc_upd.setText(R.string.allow_app_updates);
            }
        });
       wipedata.setOnClickListener(v -> dp.wipeappdata(pkg));
       stopapp.setOnClickListener(v -> dp.forcestopapp(pkg));



    }

    private boolean checknet() {
        EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        Firewall fi = edm.getFirewall();
        FirewallRule[] rules = fi.getRules(15, FirewallRule.Status.ENABLED);
        for (FirewallRule f: rules){
            if(f.getApplication().getPackageName().equals(pkg)){
                return true;
            }
        }
        return false;
    }

}
