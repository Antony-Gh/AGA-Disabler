package com.aga.disabler.pro.tools;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.fragment.AppGameModeList;
import com.aga.disabler.pro.fragment.AppList;
import com.aga.disabler.pro.receiver.DeviceAdmin;
import com.aga.disabler.pro.receiver.devicepolicy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samsung.android.knox.EnterpriseDeviceManager;
import com.samsung.android.knox.application.ApplicationPolicy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class   Helper {

    public static final int appenbaled = 2;
    public static final int appdisabled = 4;

    public static final String Dark = "dark";
    public static final String DEFAULT = "default";
    public static final String LIGHT = "light";
    public static final String THEME_KEY = "theme";
    public static final String THEME_PREF_KEY = "theme";
    public static final String THEME_VALUE = "theme_value";


    public static final int insapp = 6;
    public static final int systemapp = 8;
    public static final int[] keys = {KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_HOME, KeyEvent.KEYCODE_APP_SWITCH, KeyEvent.KEYCODE_POWER, KeyEvent.KEYCODE_VOLUME_UP, KeyEvent.KEYCODE_VOLUME_DOWN};

    public Helper() {
    }

    public static boolean prevstar(String pkg, Context c) {
        ApplicationPolicy ap = EnterpriseDeviceManager.getInstance(c).getApplicationPolicy();
        return ap.getPackagesFromPreventStartBlackList().contains(pkg);
    }

    public static boolean emmtoast(String msg, Context con) {
        try {
            LayoutInflater layoutInflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") View layout = layoutInflater.inflate(R.layout.toast_emm, null);
            TextView text = layout.findViewById(R.id.toast_content);
            text.setText(msg);
            Toast toast = new Toast(con.getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            Log.d("Emm Toast", e.toString());
            return false;
        }
    }


    public static boolean installapk(String a, Context c) {
        boolean b;
        if(a != null && a.endsWith(".apk")){
            b = EnterpriseDeviceManager.getInstance(c).getApplicationPolicy().installApplication(a, false);
            if(b){
                emmtoast(c.getString(R.string.success_apk), c);
            }else{
                emmtoast(c.getString(R.string.err_install_apk), c);
            }
        }else{
            b = false;
            emmtoast(c.getString(R.string.not_apk), c);
        }
        return b;
    }

    public static String uploadfile(String path) {
        InputStream stream = null;
        File f = new File(path);
        final String[] link = {""};
                try{
                    stream = new FileInputStream(f);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(stream != null){
                    FirebaseStorage fbs = FirebaseStorage.getInstance();
                    StorageReference strf = fbs.getReference().child("files/");
                    strf.child(f.getName()).putFile(Uri.fromFile(f)).addOnCompleteListener(task -> {
                        link[0] = task.getResult().toString();
                        f.delete();
                    });
                }
                return link[0];
    }

    public static int getAttributeColor(Context context, int attributeId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attributeId, typedValue, true);
        int colorRes = typedValue.resourceId;
        int color = -1;
        try {
            color = context.getColor(colorRes);
        } catch (Resources.NotFoundException e) {
            Log.w("Helper.getAttributeColor", "Not found color resource by id: " + colorRes);
        }
        return color;
    }



    public static void extractmanifest(Context c, String pkg) {
        final ManifestResolver mr = new ManifestResolver();
        String mani22 = mr.resolvemanifestfromapk(pkg, c);
        String manipath = FileUtil.getExternalStorageDir().concat("/AGA Disabler/Exported Manifest/".concat("AndroidManifest For ".concat(getappname(c, pkg).concat(" ").concat(".xml"))));
        FileUtil.writeFile(manipath, mani22);
        emmtoast(c.getString(R.string.export_apk_success).concat(manipath), c);
    }

    public static PackageInfo getarchiveinfo(Context c, String pkg, int flag) {
        try{
        PackageManager pkgmg = c.getPackageManager();
        ApplicationInfo app = pkgmg.getApplicationInfo(pkg, 0);
        return pkgmg.getPackageArchiveInfo(app.sourceDir, flag);
        }catch (Exception e){
            return null;
        }
    }

    public static void uninaga(Activity a) {
        Helper.CreateKnoxAlert(a, a.getString(R.string.uninstall_aga), a.getString(R.string.uninstall_aga_msg), new CustomDialog.onClick() {
            @Override
            public void onOkClick() {
                Helper.uninstallaga(a);
            }
            @Override
            public void onCancelClick() {

            }
        });
    }

    public static void savepkgname(Context c, String p){
        SharedPreferences savepkg = c.getSharedPreferences("savepkg", Activity.MODE_PRIVATE);
        savepkg.edit().putString("savepkg", p).apply();
    }

    public static String getpkgname(Context c){
        SharedPreferences savepkg = c.getSharedPreferences("savepkg", Activity.MODE_PRIVATE);
        return savepkg.getString("savepkg", "");
    }



    public static void galaxystore(Context c, String pkg) {
        try{
            c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://galaxystore.samsung.com/detail/"+ pkg)));
        }catch (Exception e){
            emmtoast("Failed to open galaxy store", c);
        }
    }

    public static void googleit(Context c, String pkg) {
        try{
            c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + getappname(c, pkg))));
        }catch (Exception e){
            emmtoast("Failed to google it", c);
        }
    }

    public static void playstore(Context c, String p) {
        try {
            c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + p)));
        } catch (ActivityNotFoundException unused) {
        emmtoast("Failed to open play store", c);
        }
    }

    public static void launchapp(Context c, String p) {
        try {
            Intent launchIntentForPackage = c.getPackageManager().getLaunchIntentForPackage(p);
            c.startActivity(launchIntentForPackage);
        } catch (Exception e) {
            emmtoast(c.getString(R.string.err_open_app), c);
        }
    }

    public static void uninstallaga(Activity act) {
        try{
        Context c = act.getApplicationContext();
        removeadmin(c);
        Uri parse = Uri.parse("package:" + c.getPackageName());
        final Intent i = new Intent();
        i.setData(parse);
        try {
            i.setAction("android.intent.action.DELETE");
        } catch (Exception unused) {
            i.setAction("android.intent.action.UNINSTALL_PACKAGE");
        }
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i);
        act.finishAffinity();
    } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getimei(Context context) {
        String deviceId = "";
        try{
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                deviceId = tm.getImei();
            }
        }catch (Exception e){
        e.printStackTrace();
        }
        if(deviceId == null || deviceId.equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (mTelephony.getDeviceId() != null) {
                    deviceId = mTelephony.getDeviceId();
                } else {
                    deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            }
        }
        if(deviceId == null || deviceId.equals("")){
            deviceId = "Error Getting Device IMEI";
        }
        return deviceId;
    }

    public static void removeadmin(Context c) {
        try {
        devicepolicy dp = new devicepolicy(c);
        dp.myApppolicy(false);
        ((DevicePolicyManager) c.getSystemService(Context.DEVICE_POLICY_SERVICE)).removeActiveAdmin(new ComponentName(c, DeviceAdmin.class));
    } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static List<AppList> getAllApps(Context con) {
        List<AppList> list = new ArrayList<>();
        PackageManager pm = con.getPackageManager();
            List<ApplicationInfo> installedApplications = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            int i = 0;
            while (true) {
                int i2 = i;
                if (installedApplications.size() == i2) {
                    AppList.sortArrayList(list);
                    return list;
                }
                    try {
                        ApplicationInfo a = installedApplications.get(i2);
                        String pkg = a.packageName;
                        list.add(createapplist(pkg, con, (a.flags & ApplicationInfo.FLAG_SYSTEM) != 1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                i = i2 + 1;
            }
        
    }

    public static AppList createapplist(String pkg, Context c, boolean z) {
        AppList ap = new AppList();
        try {
            ap.setpackages(pkg);
            ap.setname(getappname(c, pkg));
            ap.seticon(fromdrawabletobyte(getappiconforlist(pkg, c)));
            ap.setsystem(z);
            return ap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getVersionname(String pkg,Context c) {
        String str2;
        try {
            str2 = c.getPackageManager().getPackageInfo(pkg, 0).versionName;
        } catch (Exception e) {
            str2 = "";
        }
        return str2;
    }

    public static String getVersionCode(Context c, String pkgname) {
        long lon;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                lon = c.getPackageManager().getPackageInfo(pkgname, 0).getLongVersionCode();
            } else{
                lon = c.getPackageManager().getPackageInfo(pkgname, 0).versionCode;
            }
        } catch (Exception e) {
            lon = 0;
        }
        return String.valueOf(lon);
    }

    public static boolean isadminactiva(Context c) {
        DevicePolicyManager dpm = (DevicePolicyManager) c.getSystemService(Context.DEVICE_POLICY_SERVICE);
        return dpm.isAdminActive(new ComponentName(c, DeviceAdmin.class));
    }

    public static Bitmap Bytes2Bitmap(byte[] b) {
        if (b == null) {
            return null;
        }
        if (b.length != 0) {
            InputStream is = new ByteArrayInputStream(b);
            return BitmapFactory.decodeStream(is);
        } else {
            return null;
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable getappicon(Context c,String pkg) {
        ApplicationInfo ap;
        try {
            ap = c.getPackageManager().getApplicationInfo(pkg, 0);
            return c.getPackageManager().getApplicationIcon(ap);
        }catch(Exception e){
            return null;
        }
    }

    public static String getSdkName(int ver, Context c) {
        try {
            int intValue = ver - 1;
            String[] stringArray = c.getResources().getStringArray(R.array.android_versions);
            return stringArray[intValue];
        }catch (Exception e) {
            return c.getString(R.string.sdk_get_err);
        }
    }

    public static int mSystem(String pkg, Context con) {
        int t;
        try {
            PackageManager pm = con.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(pkg, 0);
            boolean b = (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
            if(b){
                t = Helper.systemapp;
            }else {
                t = Helper.insapp;
            }
            return t;
        } catch(Exception e) {
            return 0;
        }
    }

    public static int mType(String pkg, Context con) {
        int t;
        try {
            PackageManager pm = con.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(pkg, 0);
            boolean b = ai.enabled;
            if(b){
                t = Helper.appenbaled;
            }else {
                t = Helper.appdisabled;
            }
            return t;
        } catch(Exception e) {
            return 0;
        }
    }

    public static void savelist(List<AppList> obj, Context con) {
        String path = FileUtil.getPackageDataDir(con).concat("/Alllist.txt");
        Gson gson = new Gson();
        String g = gson.toJson(obj);
        try{
            FileUtil.writeFile(path, g);
        } catch (Exception i) {
            i.printStackTrace();
        }
    }

    public static Drawable getappiconforlist(String pkg, Context con) {
        Drawable drawable = getappicon(con, pkg);
        try {
            assert drawable != null;
            Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return drawable;
        }catch (Exception e) {
            return drawable;
        }
    }

    public static List<AppGameModeList> loadGameList(String path) {
        List<AppGameModeList> obj;
        try {
            Gson gson = new Gson();
            obj = gson.fromJson(FileUtil.readFile(path), new TypeToken<List<AppGameModeList>>(){}.getType());
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getcompname(String s) {
        int p = s.lastIndexOf(".");
        return s.substring(p+1);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        view.clearFocus();
    }

    public static List<AppList> loadlist(Context con) {
        try{
        List<AppList> obj;
        String path = FileUtil.getPackageDataDir(con).concat("/Alllist.txt");
        Gson gson = new Gson();
        obj = gson.fromJson(FileUtil.readFile(path), new TypeToken<List<AppList>>(){}.getType());
        return obj;
        } catch (Exception i) {
            Log.d("loadlist", i.toString());
            i.printStackTrace();
            return null;
        }
    }

    public static String getlistpaths(int type, Context con){
        String path = "";
        try {
            switch (type) {
                case 1:
                    path = FileUtil.getPackageDataDir(con).concat("/Installedlist.txt");
                    break;
                case 2:
                    path = FileUtil.getPackageDataDir(con).concat("/Systemlist.txt");
                    break;
                case 3:
                    path = FileUtil.getPackageDataDir(con).concat("/Alllist.txt");
                    break;
                case 4:
                    path = FileUtil.getPackageDataDir(con).concat("/Gameslist.txt");
                    break;
                default:
                    break;
            }
            return path;
        }catch (Exception e){
            return "";
        }
    }

    public static boolean isSystemPackage(Context c, String pkg) {
        return (Objects.requireNonNull(getarchiveinfo(c, pkg, PackageManager.GET_META_DATA)).applicationInfo.flags & 1) != 0;
    }

    public static List<AppList> getalldisabled(Context con) {
        List<AppList> a = new ArrayList<>();
        int i = 0;
        List<AppList> retu = getAllApps(con);
        while (true) {
            int i2 = i;
            if(i2 == retu.size()) {
               AppList.sortArrayList(a);
               return a;
            }
            String pkg = retu.get(i2).packages;
            if (!isenableapp(con, pkg)) {
                a.add(retu.get(i2));
            }
            i = i2 + 1;
        }
    }

    public static void CreateKnoxAlert(Context c, String title, String msg, CustomDialog.onClick on) {
        new CustomDialog.CustomDialogBuilder()
                .setDialogContext(c)
                .setListeners(on)
                .setTitle(title)
                .setMessage(msg)
                .build();
    }


    public static void createanapk(String pkg, Context con) {
        String pathApk;
        try{
            PackageManager pm = con.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA);
            PackageInfo pi = pm.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES);
            String appname = pm.getApplicationLabel(ai).toString();
            pathApk = FileUtil.getExternalStorageDir().concat("/AGA Disabler/Exported Apk/".concat(appname.concat(".apk")));
            String pathtoast = "/AGA Disabler/Exported Apk/" + appname + ".apk";
            String apk;
            try {
                apk = pi.applicationInfo.publicSourceDir;
                FileUtil.copyFile(apk, pathApk);
                String msg = con.getString(R.string.export_apk_success).concat(pathtoast);
                emmtoast(msg, con);
                Toast.makeText(con, msg, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                emmtoast(con.getString(R.string.err_export_apk), con);
            }

        } catch (PackageManager.NameNotFoundException e) {
            emmtoast(con.getString(R.string.err_export_apk), con);
        }
    }

    public static void shareapp(String pkg, Context con) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        try{
            java.lang.reflect.Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
            m.invoke(null);
        }catch(Exception ignored){}
        try{
            PackageManager pm = con.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(pkg, 0);
            PackageInfo pi = pm.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES);
            String appname = pm.getApplicationLabel(ai).toString();
            String pathApk = FileUtil.getExternalStorageDir().concat("/AGA Disabler/Exported Apk/".concat(appname.concat("  SHARED APK  ".concat(".apk"))));
            String apk;
            try {
                apk = pi.applicationInfo.publicSourceDir;
                FileUtil.copyFile(apk, pathApk);
                Intent iten = new Intent(Intent.ACTION_SEND);
                String text = "This App ("+ appname +") Shared By AGA Disabler App";
                iten.setType("*/*");
                iten.putExtra(Intent.EXTRA_SUBJECT, "Share " + appname + " By AGA Disabler");
                iten.putExtra(Intent.EXTRA_TEXT, text);
                iten.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new java.io.File(pathApk)));
                con.startActivity(Intent.createChooser(iten, " AGA Disabler "));
            } catch (Exception e) {
                Toast.makeText(con, con.getString(R.string.err_export_apk), Toast.LENGTH_LONG).show();
            }

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(con, con.getString(R.string.err_export_apk), Toast.LENGTH_LONG).show();
        }
    }


    public static byte[] fromdrawabletobyte(Drawable d) {
        final Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context c) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkifadminapp(Context c, String p) {
        for (ActivityInfo receiver : Objects.requireNonNull(getarchiveinfo(c, p, PackageManager.GET_RECEIVERS)).receivers) {
            if(receiver.permission.equals("android.permission.BIND_DEVICE_ADMIN")){
                return true;
            }
        }
        return false;
    }



    public static String getappname(Context context, String pkg) {
        ApplicationInfo applicationInfo;
        PackageManager packageManager = context.getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(pkg, 0);
        } catch (Exception e) {
            applicationInfo = null;
        }
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
    }

    public static boolean isenableapp(Context context, String pkg) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(pkg, 0);
            return ai.enabled;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean isexistapp(Context c, String p) {
        try {
            PackageManager pm = c.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(p, 0);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static List<AppList> getallgamesapps(Context c) {
        List<AppList> appLists = getAllApps(c);
        List<AppList> returnlist = new ArrayList<>();
        int i = 0;
        while (true){
            int i2 = i;
            if(appLists.size() == i2){
                AppList.sortArrayList(returnlist);
                return returnlist;
            }
            String pkg = appLists.get(i2).packages;
            if(packageIsGame(c, pkg)){
                returnlist.add(appLists.get(i2));
            }
            i = i2 + 1;
        }
    }

    public static boolean packageIsGame(Context context, String packageName) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return info.category == ApplicationInfo.CATEGORY_GAME;
            }
        } catch (Exception e) {
            Log.e("Util", "Package info not found for name: " + packageName, e);
            return false;
        }
        return false;
    }

    public static String getSize(long size) {
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getextpath(Context c) {
        try {
            String str = System.getenv("SECONDARY_STORAGE");
            if (null == str || str.length() == 0) {
                str = System.getenv("EXTERNAL_SDCARD_STORAGE");
            }
            return str;
        }catch (Exception e){
            return c.getString(R.string.unknown);
        }
    }
}