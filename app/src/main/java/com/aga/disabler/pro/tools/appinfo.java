package com.aga.disabler.pro.tools;

import static com.aga.disabler.pro.tools.Helper.getarchiveinfo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aga.disabler.pro.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import kotlin.jvm.internal.Intrinsics;

public class appinfo {

   private final Context c;
    private final String pkgname;
    private final PackageManager pkgmg;
    private ApplicationInfo applicationInfo;

    public appinfo(Context con, String pkg) {
     this.c = con;
     this.pkgname = pkg;
     pkgmg = c.getPackageManager();
     try {
     applicationInfo = pkgmg.getApplicationInfo(pkgname, 0);
     }catch (Exception e){
     applicationInfo = null;
     }
    }

    public HashMap<Object, Object> getsomeappinfo() {
        HashMap<Object, Object> a = new HashMap<>();
        a.put("name", getappname());//
        a.put("package", pkgname);//
        a.put("icon", getappiconforlist());//
        a.put("ver code", String.valueOf(getVersionCode()));//
        a.put("ver name", String.valueOf(getVersionname()));//
        return a;
    }

    public ArrayList<HashMap<Object, Object>> getallappinfo() {
     ArrayList<HashMap<Object, Object>> appdata = new ArrayList<>();
     HashMap<Object, Object> a = new HashMap<>();
     a.put("name", getappname());//
     a.put("package", pkgname);//
     a.put("icon", getappiconforlist());//
     a.put("ver code", String.valueOf(getVersionCode()));//
     a.put("ver name", String.valueOf(getVersionname()));//
     assert applicationInfo != null;
     a.put("target sdk", String.valueOf(applicationInfo.targetSdkVersion));//
     a.put("target sdk name", Helper.getSdkName(applicationInfo.targetSdkVersion, c));//
     a.put("min sdk", String.valueOf(applicationInfo.minSdkVersion));//
     a.put("min sdk name", Helper.getSdkName(applicationInfo.minSdkVersion, c));//
     a.put("data dir", applicationInfo.dataDir);//
     a.put("source dir", applicationInfo.sourceDir);//
     a.put("install source", getinstallsource());//
     a.put("install date", getdate(1));//
     a.put("last modify", getdate(2));//
     a.put("category", category());//
     a.put("app status", applicationInfo.enabled);//
     a.put("process name", applicationInfo.processName);//
     a.put("activities", getActivites());//
     a.put("receivers", getReceivers());//
     a.put("services", getServices());//
     a.put("providers", getProviders());//
     a.put("permissions list", getPermissions());
     a.put("metadata", getmetadata());
     a.put("signature", getSignature());
     a.put("nativelib", getnativelib());
     a.put("size", getApkSize());//
     appdata.add(a);
     return appdata;
    }

    public String getnativelib() {
        try {
        StringBuilder sb2 = new StringBuilder();
        PackageInfo packageInfo = c.getPackageManager().getPackageInfo(pkgname, PackageManager.GET_SHARED_LIBRARY_FILES);
        String str3 = packageInfo.applicationInfo.nativeLibraryDir;
        File[] listFiles;
        int i = 1;
        if (str3 != null) {
            if ((str3.length() > 0) && (listFiles = new File(packageInfo.applicationInfo.nativeLibraryDir).listFiles()) != null) {
                for (File file : listFiles) {
                    sb2.append(i).append("- ");
                    Intrinsics.checkExpressionValueIsNotNull(file, "itf");
                    sb2.append(file.getAbsolutePath());
                    sb2.append(" [ ");
                    sb2.append(Helper.getSize(file.length()));
                    sb2.append(" ] ");
                    sb2.append("\n");
                    i++;
                }
            }
        }

        if(sb2.toString().equals("")){
            return c.getString(R.string.unknown);
        }
        return sb2.toString();
        }catch (Exception e){
            return c.getString(R.string.unknown);
        }
    }

    public String getApkSize() {
        String Apksize;
        try {
            Apksize = Helper.getSize((long) FileUtil.getFileLength(c.getPackageManager().getApplicationInfo(pkgname, 0).publicSourceDir));
        } catch (Exception e) {
            Apksize = c.getString(R.string.err_get_appsize);
        }
        return Apksize;
    }

    public String getmetadata() {
        try {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = c.getPackageManager().getPackageInfo(pkgname, PackageManager.GET_META_DATA).applicationInfo.metaData;
        if (bundle != null) {
            for (String str4 : bundle.keySet()) {
                sb.append(str4);
                sb.append(" [ ");
                sb.append(bundle.get(str4));
                sb.append(" ] ");
                sb.append("\n");
            }
        }
        String s = sb.toString();
        if(s.equals("")){
            return c.getString(R.string.unknown);
        }else{
            return s;
        }
        }catch (Exception e){
           return c.getString(R.string.unknown);
        }
    }



    public String getSignature(){
        PackageInfo pkgg;
        try {
            if(Build.VERSION.SDK_INT >= 28) {
                pkgg = c.getPackageManager().getPackageInfo(pkgname, PackageManager.GET_SIGNING_CERTIFICATES);
            }else{
                pkgg = c.getPackageManager().getPackageInfo(pkgname, PackageManager.GET_SIGNATURES);
            }
            return "MD5 Signature : " +
                    "\n" +
                    getMD5(pkgg) +
                    "\n\n" +
                    "SHA1 Signature : " +
                    "\n" +
                    getSHA1(pkgg) +
                    "\n\n" +
                    "SHA256 Signature : " +
                    "\n" +
                    getSHA(pkgg) +
                    "\n\n";
        }catch (Exception e){
            return c.getString(R.string.unknown);
        }
    }

    public String getdate(int t) {
        String str2;
        try {
            String fory = "dd/MM/yyyy EEEE/MMMM";
            switch (t) {
                case 1:
                    str2 = new SimpleDateFormat(fory, Locale.US).format(new Date(c.getPackageManager().getPackageInfo(pkgname, 0).firstInstallTime));
                    break;
                case 2:
                    str2 = new SimpleDateFormat(fory, Locale.US).format(new Date(new File(applicationInfo.sourceDir).lastModified()));
                    break;
                default:
                    str2 = "";
                    break;
            }
        }catch (Exception e){
           str2 = "";
        }
        return str2;
    }

    public String getinstallsource() {
        PackageManager pm = c.getPackageManager();
        String soupkg;
        String souname;
        soupkg = pm.getInstallerPackageName(pkgname);
        if (soupkg == null || soupkg.equals("")) {
            souname = c.getString(R.string.install_src_unknown);
        } else {
            souname = Helper.getappname(c, soupkg);
        }
        return souname;
    }

    public String category() {
        String s;
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                switch (applicationInfo.category) {
                    case ApplicationInfo.CATEGORY_AUDIO:
                        s = "Audio";
                        break;
                    case ApplicationInfo.CATEGORY_GAME:
                        s = "Game";
                        break;
                    case ApplicationInfo.CATEGORY_IMAGE:
                        s = "Image";
                        break;
                    case ApplicationInfo.CATEGORY_MAPS:
                        s = "Map";
                        break;
                    case ApplicationInfo.CATEGORY_NEWS:
                        s = "News";
                        break;
                    case ApplicationInfo.CATEGORY_PRODUCTIVITY:
                        s = "Productivity";
                        break;
                    case ApplicationInfo.CATEGORY_SOCIAL:
                        s = "Social";
                        break;
                    case ApplicationInfo.CATEGORY_UNDEFINED:
                        s = "Undefined";
                        break;
                    case ApplicationInfo.CATEGORY_VIDEO:
                        s = "Video";
                        break;
                    default:
                        s = c.getString(R.string.unknown);
                        break;
                    case ApplicationInfo.CATEGORY_ACCESSIBILITY:
                        s = "Accessibility";
                        break;
                }
            }else{
                s = c.getString(R.string.unknown);
            }
            return s;
        }catch (Exception e){
            return null;
        }
    }


    public String getVersionname() {
        String str2;
        try {
            str2 = pkgmg.getPackageInfo(pkgname, 0).versionName;
        } catch (Exception c) {
            str2 = "";
        }
        return str2;
    }

    public String getappname() {
        String str2;
        try {
            str2 = (String) (applicationInfo != null ? pkgmg.getApplicationLabel(applicationInfo) : c.getString(R.string.unknown));
        } catch (Exception c) {
            str2 = "";
        }
        return str2;
    }

    public String getVersionCode() {
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

    public String[] getPermissions() {
        try {
            PackageInfo pkginfo = getarchiveinfo(c, pkgname, PackageManager.GET_PERMISSIONS);
            assert pkginfo != null;
            return pkginfo.requestedPermissions;
        }catch (Exception e){
            return null;
        }
    }

    public ActivityInfo[] getActivites() {
        try {
            PackageInfo pkginfo = getarchiveinfo(c, pkgname, PackageManager.GET_RECEIVERS);
            assert pkginfo != null;
            return pkginfo.activities;
        }catch (Exception e){
            return null;
        }
    }
    public ActivityInfo[] getReceivers() {
        try {
            PackageInfo pkginfo = getarchiveinfo(c, pkgname, PackageManager.GET_RECEIVERS);
            assert pkginfo != null;
            return pkginfo.receivers;
        }catch (Exception e){
            return null;
        }
    }
    public ServiceInfo[] getServices() {
        try {
            PackageInfo pkginfo = getarchiveinfo(c, pkgname, PackageManager.GET_SERVICES);
            assert pkginfo != null;
            return pkginfo.services;
        }catch (Exception e){
            return null;
        }
    }
    public ProviderInfo[] getProviders() {
        try {
            PackageInfo pkginfo = getarchiveinfo(c, pkgname, PackageManager.GET_PROVIDERS);
            assert pkginfo != null;
            return pkginfo.providers;
        }catch (Exception e){
            return null;
        }
    }

 public Drawable getappiconforlist() {
     try {
      Drawable drawable = getappicon();
      Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(createBitmap);
      drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      drawable.draw(canvas);
         return drawable;
     }catch (Exception e) {
         return getappicon();
     }
    }

 public Drawable getappicon() {
  return pkgmg.getApplicationIcon(applicationInfo);
 }

 @NonNull
 private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
  final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
  final Canvas canvas = new Canvas(bmp);
  drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
  drawable.draw(canvas);
  return bmp;
 }

 public void persistImage() {
        String msg;
  String path = Environment.getExternalStorageDirectory() + "/AGA Disabler/Exported Icons/" + getappname() + " icon.png";
  File imageFile = new File(path);
  OutputStream os;
  try {
   Drawable d = getappicon();
   Bitmap bitmap = getBitmapFromDrawable(d);
   os = new FileOutputStream(imageFile);
   bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
   os.flush();
   os.close();
   msg = c.getString(R.string.export_apk_success).concat(path);
  } catch (Exception e) {
   msg = c.getString(R.string.err_export_apk);
   Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
  }
 Helper.emmtoast(msg,c);
  }

    public String getSHA(PackageInfo packageInfo) {
        Signature[] signatureArr = packageInfo.signatures;
        if (signatureArr == null || signatureArr.length == 0) {
            return c.getString(R.string.unknown);
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(signatureArr[0].toByteArray());
            StringBuilder stringBuffer = new StringBuilder();
            for (byte b2 : digest) {
                String upperCase = Integer.toHexString(b2 & 255).toUpperCase(Locale.US);
                if (upperCase.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(upperCase);
                stringBuffer.append(":");
            }
            String stringBuffer2 = stringBuffer.toString();
            return stringBuffer2.substring(0, stringBuffer2.length() - 1);
        } catch (Exception e2) {
            e2.printStackTrace();
            return c.getString(R.string.unknown);
        }
    }

    public String getMD5(PackageInfo packageInfo) {
        Signature[] signatureArr = packageInfo.signatures;
        if (signatureArr == null || signatureArr.length == 0) {
            return c.getString(R.string.unknown);
        }
        byte[] byteArray = signatureArr[0].toByteArray();
        StringBuilder stringBuffer = new StringBuilder();
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(byteArray);
            byte[] digest = instance.digest();
            for (byte b : digest) {
                if (Integer.toHexString(b & 255).length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(b & 255));
            }
            return stringBuffer.toString();
        } catch (Exception e2) {
            return c.getString(R.string.unknown);
        }
    }

    public String getSHA1(PackageInfo packageInfo) {
        try {
        Signature[] signatureArr = packageInfo.signatures;
        if (signatureArr == null || signatureArr.length == 0) {
            return c.getString(R.string.unknown);
        }
            byte[] digest = MessageDigest.getInstance("SHA1").digest(signatureArr[0].toByteArray());
            StringBuilder stringBuffer = new StringBuilder();
            for (byte b2 : digest) {
                String upperCase = Integer.toHexString(b2 & 255).toUpperCase(Locale.US);
                if (upperCase.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(upperCase);
                stringBuffer.append(":");
            }
            String stringBuffer2 = stringBuffer.toString();
            return stringBuffer2.substring(0, stringBuffer2.length() - 1);
        } catch (Exception e2) {
            return c.getString(R.string.unknown);
        }
    }



}
