package com.aga.disabler.pro.receiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import com.aga.disabler.pro.fragment.AppList;
import com.aga.disabler.pro.tools.Helper;
import com.aga.disabler.pro.tools.appinfo;

import static com.aga.disabler.pro.fragment.AppList.sortArrayList;
import static com.aga.disabler.pro.tools.Helper.createapplist;
import static com.aga.disabler.pro.tools.Helper.getallgamesapps;
import static com.aga.disabler.pro.tools.Helper.isSystemPackage;
import static com.aga.disabler.pro.tools.Helper.mSystem;
import static com.aga.disabler.pro.tools.Helper.mType;
import static com.aga.disabler.pro.tools.Helper.savelist;

public class PackageLoaderService extends Service {
    private List<AppList> Installedlist;
    private List<AppList> Systemlist;
    private List<AppList> Alllist;
    private List<AppList> GamesList;
    private appinfo appinfo;


    public PackageLoaderService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Package Loader Service", "OnCreate");
        init();
    }

    private void init() {
        Installedlist = new ArrayList<>();
        Systemlist = new ArrayList<>();
        Alllist = new ArrayList<>();
        GamesList = new ArrayList<>();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
