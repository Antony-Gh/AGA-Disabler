package com.aga.disabler.pro.tools;

import static android.content.Context.WIFI_SERVICE;
import static com.aga.disabler.pro.tools.Helper.getSize;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import com.aga.disabler.pro.R;
import com.samsung.android.knox.EnterpriseDeviceManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AboutPhoneHelper {
    private final Context c;

    public AboutPhoneHelper(Context con) {
        this.c = con;
    }


    public String getlocinfo() {
        String locinfo;
        try {
            LocationManager lm = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
            boolean isgps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isnet = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isgps) {
                if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locinfo = "location permission denied";
                } else {
                    Location gpsloc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    locinfo = getlocmap(gpsloc);
                }
            } else {
                if (isnet) {
                    if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        locinfo = "location permission denied";
                    } else {
                        Location netloc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        locinfo = getlocmap(netloc);
                    }
                } else {
                    locinfo = "No location providers";
                }
            }
        } catch (Exception e) {
            locinfo = c.getString(R.string.unknown);
            e.printStackTrace();
        }
        return locinfo;
    }

    public String getlocmap(Location loc) {
        return "Accuracy : " +
                loc.getAccuracy() +
                "\n" +
                "Altitude : " +
                loc.getAltitude() +
                "\n" +
                "Bearing : " +
                loc.getBearing() +
                "\n" +
                "Latitude : " +
                loc.getLatitude() +
                "\n" +
                "Longitude : " +
                loc.getAccuracy() +
                "\n" +
                "Provider : " +
                loc.getProvider() +
                "\n" +
                "Speed : " +
                loc.getSpeed() +
                "\n" +
                "Time : " +
                loc.getTime() +
                "\n";
    }


    public String getscreeninfo(int type, Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int SW = dm.widthPixels;
        int SH = dm.heightPixels;
        double x = Math.pow(SW / dm.xdpi, 2);
        double y = Math.pow(SH / dm.ydpi, 2);
        double SI = Math.sqrt(x + y);
        double Inches = (double) Math.round(SI * 10) / 10;
        String retun;
        switch (type) {
            case 1:
                retun = String.valueOf(x);
                break;
            case 2:
                retun = String.valueOf(y);
                break;
            case 3:
                retun = String.valueOf(Inches);
                break;
            default:
                retun = "";
                break;
        }
        return retun;
    }

    public String getMacAddress(String str) {
        try {
            for (NetworkInterface t : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (str == null || t.getName().equalsIgnoreCase(str)) {
                    byte[] hardwareAddress = t.getHardwareAddress();
                    if (hardwareAddress == null) {
                        return "";
                    }
                    StringBuilder sb = new StringBuilder();
                    for (byte address : hardwareAddress) {
                        sb.append(String.format("%02X:", address));
                    }
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    return sb.toString();
                }
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    public String screentimeout() {
        try {
            int i = Settings.System.getInt(c.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
            return TimeUnit.MILLISECONDS.toMinutes(i) + " minutes";
        } catch (Exception e) {
            return "0";
        }
    }

    public String proccores() {
        return String.valueOf(Runtime.getRuntime().availableProcessors());
    }

    public String javavm() {
        return String.valueOf(System.getProperty("java.vm.version"));
    }

    public String processorinfo() {
        ProcessBuilder processBuilder;
        StringBuilder Holder = new StringBuilder();
        String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
        InputStream inputStream;
        Process process;
        byte[] byteArry;
        byteArry = new byte[1024];
        try {
            processBuilder = new ProcessBuilder(DATA);
            process = processBuilder.start();
            inputStream = process.getInputStream();
            while (inputStream.read(byteArry) != -1) {
                Holder.append(new String(byteArry));
            }
            inputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return Holder.toString();
    }

    public String screenrefrate() {
        Display display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        float refreshRating = display.getRefreshRate();
        return String.valueOf(refreshRating);
    }

    public String getsecuritypatchlvl() {
        return formatdate(android.os.Build.VERSION.SECURITY_PATCH);
    }

    public String getIPAddress() {
        WifiManager wifiManager = (WifiManager) c.getApplicationContext().getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
    }

    public String getWifiStates() {
        WifiManager wifi = (WifiManager) c.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()) {
            return c.getString(R.string.connect_info) + getCurrentSsid();
        } else {
            return c.getString(R.string.disconnect_info);
        }
    }

    private String getCurrentSsid() {
        String ssid = c.getString(R.string.unknown_ssid);
        ConnectivityManager connManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        assert networkInfo != null;
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) c.getApplicationContext().getSystemService(WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                ssid = connectionInfo.getSSID();
            } else {
                ssid = c.getString(R.string.unknown_ssid);
            }
        }
        return ssid;
    }

    public String getdevicename() {
        String s;
        s = Build.MODEL + " / " + getDeviceNameinsettings(c);
        return s;
    }

    public String allbuildinfo() {
        StringBuilder s = new StringBuilder();
        DeviceName.init(c);
        s.append("Device Name : ");
        s.append(DeviceName.getDeviceName());
        s.append("\n");
        s.append("Board : ");
        s.append(Build.BOARD);
        s.append("\n");
        s.append("Brand : ");
        s.append(Build.BRAND);
        s.append("\n");
        s.append("ID : ");
        s.append(Build.ID);
        s.append("\n");
        s.append("Tags : ");
        s.append(Build.TAGS);
        s.append("\n");
        s.append("Abis : ");
        s.append(Arrays.toString(Build.SUPPORTED_ABIS));
        s.append("\n");
        s.append("Bootloader : ");
        s.append(Build.BOOTLOADER);
        s.append("\n");
        s.append("Device : ");
        s.append(Build.DEVICE);
        s.append("\n");
        s.append("Display : ");
        s.append(Build.DISPLAY);
        s.append("\n");
        s.append("Finger print : ");
        s.append(Build.FINGERPRINT);
        s.append("\n");
        s.append("Hardware : ");
        s.append(Build.HARDWARE);
        s.append("\n");
        s.append("Host : ");
        s.append(Build.HOST);
        s.append("\n");
        s.append("Manufacturer : ");
        s.append(Build.MANUFACTURER);
        s.append("\n");
        s.append("Model : ");
        s.append(Build.MODEL);
        s.append("\n");
        s.append("Product : ");
        s.append(Build.PRODUCT);
        s.append("\n");
        s.append("Time : ");
        s.append(Build.TIME);
        s.append("\n");
        s.append("Type : ");
        s.append(Build.TYPE);
        s.append("\n");
        s.append("User : ");
        s.append(Build.USER);
        s.append("\n");
        s.append("Radio Version : ");
        s.append(Build.getRadioVersion());
        s.append("\n");
        s.append("Base OS : ");
        s.append(Build.VERSION.BASE_OS);
        s.append("\n");
        s.append("Code Name : ");
        s.append(Build.VERSION.CODENAME);
        s.append("\n");
        s.append("Incremental : ");
        s.append(Build.VERSION.INCREMENTAL);
        s.append("\n");
        s.append("Preview sdk : ");
        s.append(Build.VERSION.PREVIEW_SDK_INT);
        s.append("\n");
        s.append("Release : ");
        s.append(Build.VERSION.RELEASE);
        s.append("\n");
        return s.toString();
    }

    public String getManufacture() {
        return Build.MANUFACTURER;
    }

    public String getModel() {
        String s;
        s = Build.MANUFACTURER + " " + Build.MODEL + " " + Build.PRODUCT + " ";
        return s;
    }

    public String getCodeName() {
        String s;
        s = Build.VERSION.CODENAME + " / " + Build.VERSION.SDK_INT + " " + Build.VERSION.RELEASE + " ";
        return s;
    }

    public String getSerialNo() {
        String s;
        s = getSerialNumber();
        return s;
    }

    @SuppressLint("HardwareIds")
    public String getSerialNumber() {
        String serialNumber;
        try {
            serialNumber = String.valueOf(get("gsm.sn1"));
            if (serialNumber.equals(""))
                serialNumber = String.valueOf(get("ril.serialnumber"));
            if (serialNumber.equals(""))
                serialNumber = String.valueOf(get("ro.serialno"));
            if (serialNumber.equals(""))
                serialNumber = String.valueOf(get("sys.serialnumber"));
            if (serialNumber.equals(""))
                serialNumber = String.valueOf(Build.SERIAL);
            if (serialNumber.equals(Build.UNKNOWN))
                serialNumber = EnterpriseDeviceManager.getInstance(c).getDeviceInventory().getSerialNumber();
            if (serialNumber.equals("")) {
                serialNumber = c.getString(R.string.unknown);
            }
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = c.getString(R.string.unknown);
        }
        return serialNumber;
    }

    public String getBuildNo() {
        String s;
        s = Build.DISPLAY + "\n" + c.getString(R.string.bootloader) + Build.BOOTLOADER + "\n" + c.getString(R.string.hardware) + Build.HARDWARE + "\n" + c.getString(R.string.board) + Build.BOARD + " " + bitcount();
        return s;
    }

    public String getAndroidInfo() {
        String s;
        s = c.getString(R.string.target_sdk) + " " + Build.VERSION.SDK_INT + " " + Helper.getSdkName(Build.VERSION.SDK_INT, c);
        return s;
    }

    public String getKernel() {
        String s;
        s = Build.FINGERPRINT + "\n" + Build.HOST + "\n" + Build.USER + "\n" + Build.TYPE + "\n" + Build.TIME + "\n" + System.getProperty("os.arch");
        return s;
    }

    public String isDeviceRooted() {
        String s;
        if (checkRootMethod1() || checkRootMethod2()) {
            s = c.getString(R.string.yes);
        } else {
            s = c.getString(R.string.no);
        }
        return s;
    }

    public String brightness() {
        try {
            String ret;
            String mode;
            int bright = Settings.System.getInt(c.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            int modee = Settings.System.getInt(c.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            switch (modee) {
                case Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC:
                    mode = c.getString(R.string.auto);
                    break;
                case Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL:
                    mode = c.getString(R.string.manual);
                    break;
                default:
                    mode = c.getString(R.string.unknown);
                    break;
            }
            ret = bright + " %" + " Mode: " + mode;
            return ret;
        } catch (Exception e) {
            return "";
        }
    }

    public String bitcount() {
        try {
            String str = "";
            String p = "/proc/cpuinfo";
            if (FileUtil.isExistFile(p)) {
                str = FileUtil.readFile(p);
            }
            String _vvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = get("ro.product.cpu.abilist");
            if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvv6.equals("")) {
                _vvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = get("ro.product.cpu.abi");
            }
            if (str.toLowerCase().contains("aarch64") || _vvvvvvvvvvvvvvvvvvvvvvvvvvvv6.contains("64")) {
                return "[64-bit]";
            }
            return "[32-bit]";
        } catch (Exception e) {
            return "";
        }
    }

    private boolean checkRootMethod1() {
        String str = Build.TAGS;
        return str != null && str.contains("test-keys");
    }

    private boolean checkRootMethod2() {
        String[] strArr = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (int i = 0; i < 10; i++) {
            if (new File(strArr[i]).exists()) {
                return true;
            }
        }
        return false;
    }

    public String getResolution() {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        int i2 = displayMetrics.heightPixels;
        int i3 = displayMetrics.widthPixels;
        return i3 + " x " + i2 + " pixels";
    }

    public String getDensity() {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        return String.valueOf(displayMetrics.density);
    }

    public String getDpi() {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        return String.valueOf(displayMetrics.densityDpi);
    }


    public String getpixelsforx() {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        return String.valueOf(displayMetrics.xdpi);
    }

    public String getpixelsfory() {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        return String.valueOf(displayMetrics.ydpi);
    }

    public String sensordetails() {
        StringBuilder sb = new StringBuilder();
        try {
            List<Sensor> sensorList = ((SensorManager) c.getSystemService(Context.SENSOR_SERVICE)).getSensorList(-1);
            for (int i2 = 0; i2 < sensorList.size(); i2++) {
                sb.append(sensorList.get(i2).getName()).append("\n");
            }
        } catch (Exception ignored) {
        }
        return sb.toString();
    }

    public String getRam() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(memoryInfo);
        return c.getString(R.string.total_ram) +
                getSize(memoryInfo.totalMem) +
                "\n" +
                c.getString(R.string.ava_ram) +
                getSize(memoryInfo.availMem);
    }

    public String Storagesettings() {
        return c.getString(R.string.internal_storage) + eee() + "\n\n" + c.getString(R.string.ext_storage) + a();
    }

    public String getCountryCode() {
        return c.getResources().getConfiguration().locale.getCountry();
    }

    public String getNetwork() {
        StringBuilder stringBuilder = new StringBuilder();
        TelephonyManager phoneMgr = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        stringBuilder.append(c.getString(R.string.net_op));
        stringBuilder.append(" ");
        String s =phoneMgr.getNetworkOperatorName();
        if(s == null || s.equals("")){s=c.getString(R.string.unknown);}
        stringBuilder.append(s).append(" / ").append(phoneMgr.getNetworkOperator());
        stringBuilder.append("\n");
        stringBuilder.append(c.getString(R.string.net_data_type));
        stringBuilder.append(" ");
        stringBuilder.append(getnetworktype());
        stringBuilder.append("\n");
        stringBuilder.append("Network State:");
        stringBuilder.append(isOnlinenow());
        stringBuilder.append("\n");
        stringBuilder.append(c.getString(R.string.net_iso));
        stringBuilder.append(" ");
        stringBuilder.append(phoneMgr.getNetworkCountryIso());
        stringBuilder.append("\n");

        //Sim Info
        stringBuilder.append(c.getString(R.string.sim_state));
        stringBuilder.append(" ");
        stringBuilder.append(getSimstate());
        stringBuilder.append("\n");
        stringBuilder.append(c.getString(R.string.data_state));
        stringBuilder.append(" ");
        stringBuilder.append(getDatastate());
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(c, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stringBuilder.append("\n");
                stringBuilder.append(c.getString(R.string.ser_state));
                stringBuilder.append(" ");
                stringBuilder.append(getserstate(phoneMgr.getServiceState().getState()));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stringBuilder.append("\n");
                stringBuilder.append(c.getString(R.string.rom_state));
                stringBuilder.append(" ");
                stringBuilder.append(phoneMgr.getServiceState().getRoaming());
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        stringBuilder.append("\n");
        stringBuilder.append(c.getString(R.string.sig_streng));
        stringBuilder.append("\n");
        stringBuilder.append(c.getString(R.string.level)).append(" ").append(phoneMgr.getSignalStrength().getLevel()).append("\n");
        stringBuilder.append(c.getString(R.string.strength)).append(" ").append(phoneMgr.getSignalStrength().getCdmaDbm()).append(" dBm");
        }
        stringBuilder.append("\n");
        stringBuilder.append(c.getString(R.string.sim_coun_iso));
        stringBuilder.append(" ");
        String k = phoneMgr.getSimCountryIso();
        if(k == null || k.equals("")){k = c.getString(R.string.unknown);}
        stringBuilder.append(k);
        try {
            stringBuilder.append("\n");
            stringBuilder.append(c.getString(R.string.sim_serial_no));
            stringBuilder.append(" ");
            stringBuilder.append(phoneMgr.getSimSerialNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
        stringBuilder.append("\n");
        if (Build.VERSION.SDK_INT >= 28) {
            stringBuilder.append(c.getString(R.string.sim_carrier));
            stringBuilder.append(" ");
            stringBuilder.append(phoneMgr.getSimCarrierIdName()).append(" / ").append(phoneMgr.getSimCarrierId());
            stringBuilder.append("\n");
        }
        String p;
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            p = "Error get line number";
        } else {
            p = phoneMgr.getLine1Number();
        }

        if (p == null || p.equals("")) {
            p = c.getString(R.string.unknown);
        }
        stringBuilder.append(c.getString(R.string.phone_number));
        stringBuilder.append(" ");
        stringBuilder.append(p);
        return stringBuilder.toString();
    }

    private String getserstate(int i){
        String s;
        switch(i){
            case ServiceState.STATE_IN_SERVICE:
                s="In Service";
            break;
            case ServiceState.STATE_OUT_OF_SERVICE:
                s="Out Of Service";
            break;
            case ServiceState.STATE_EMERGENCY_ONLY:
                s="Emergency Only";
            break;
            case ServiceState.STATE_POWER_OFF:
                s="Power Off";
            break;
            default:
                s = c.getString(R.string.unknown);
            break;
        }
        return s;
    }

    public String isOnlinenow() {
        String retu;
        try {
            ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null) {
                if (netInfo.isConnected()) {
                    String s = netInfo.getTypeName();
                    if (s.toLowerCase(Locale.ROOT).equals("wifi")) {
                        retu = c.getString(R.string.connect_wifi);
                    } else {
                        retu = c.getString(R.string.connect_data);
                    }
                } else {
                    retu = c.getString(R.string.disconnect_from_internet);
                }
            } else {
                retu = c.getString(R.string.no_net_info);
            }
            return retu;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getcolor() {
        try {
            String str;
            String str2;
            String str3;
            String str4;
            String _vvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = get("ril.product_code");
            String _vvvvvvvvvvvvvvvvvvvvvvvvvvvv62 = get("ril.sales_code");
            String _vvvvvvvvvvvvvvvvvvvvvvvvvvvv63 = get("ro.csc.sales_code");
            if ((_vvvvvvvvvvvvvvvvvvvvvvvvvvvv62.equals("") || !_vvvvvvvvvvvvvvvvvvvvvvvvvvvv6.endsWith(_vvvvvvvvvvvvvvvvvvvvvvvvvvvv62)) && (_vvvvvvvvvvvvvvvvvvvvvvvvvvvv63.equals("") || !_vvvvvvvvvvvvvvvvvvvvvvvvvvvv6.endsWith(_vvvvvvvvvvvvvvvvvvvvvvvvvvvv63))) {
                str = "";
            } else {
                _vvvvvvvvvvvvvvvvvvvvvvvvvvvv62 = String.valueOf(_vvvvvvvvvvvvvvvvvvvvvvvvvvvv6.charAt(_vvvvvvvvvvvvvvvvvvvvvvvvvvvv6.length() - 6));
                str = String.valueOf(_vvvvvvvvvvvvvvvvvvvvvvvvvvvv6.charAt(_vvvvvvvvvvvvvvvvvvvvvvvvvvvv6.length() - 5));
            }
            switch (switchObjectToInt(str, "A", "B", "D", "G", "I", "K", "N", "P", "R", "S", "V", "W", "Y")) {
                case 0:
                    str4 = c.getString(R.string.color_gray);
                    break;
                case 1:
                    str4 = c.getString(R.string.color_blue);
                    break;
                case 2:
                    str4 = c.getString(R.string.color_gold);
                    break;
                case 3:
                    str4 = c.getString(R.string.color_green);
                    break;
                case 4:
                    str4 = c.getString(R.string.color_pink);
                    break;
                case 5:
                    str4 = c.getString(R.string.color_black);
                    break;
                case 6:
                    str4 = c.getString(R.string.color_brown);
                    break;
                case 7:
                    str4 = c.getString(R.string.color_purple);
                    break;
                case 8:
                    str4 = c.getString(R.string.color_red);
                    break;
                case 9:
                    str4 = c.getString(R.string.color_bronze);
                    break;
                case 10:
                    str4 = c.getString(R.string.color_orchid_gray);
                    break;
                case 11:
                    str4 = c.getString(R.string.color_white);
                    break;
                case 12:
                    str4 = c.getString(R.string.color_yellow);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + switchObjectToInt(str, "A", "B", "D", "G", "I", "K", "N", "P", "R", "S", "V", "W", "Y"));
            }
            switch (switchObjectToInt(_vvvvvvvvvvvvvvvvvvvvvvvvvvvv62, "B", "D", "E", "G", "H", "M", "N", "T", "W", "Z")) {
                case 0:
                    str2 = c.getString(R.string.color_black);
                    break;
                case 1:
                    str2 = c.getString(R.string.color_deep);
                    break;
                case 2:
                    str2 = c.getString(R.string.color_pink);
                    break;
                case 3:
                    str2 = c.getString(R.string.color_gold);
                    break;
                case 5:
                    str2 = c.getString(R.string.color_metallic);
                    break;
                case 6:
                    str2 = c.getString(R.string.color_brown);
                    break;
                case 7:
                    str2 = c.getString(R.string.color_titanium);
                    break;
                case 8:
                    str2 = c.getString(R.string.color_white);
                    break;
                default:
                    str2 = "";
                    break;
            }
            if (str4.equals("")) {
                str3 = "";
            } else if (str.equals("D")) {
                str3 = str4 + " " + str2;
            } else {
                str3 = str2 + " " + str4;
            }
            return str3.trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getDatastate() {
        try {
            String s;
            TelephonyManager phoneMgr = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
            switch (phoneMgr.getDataState()) {
                case TelephonyManager.DATA_DISCONNECTED:
                    s = c.getString(R.string.disconnect);
                    break;
                case TelephonyManager.DATA_CONNECTING:
                    s = c.getString(R.string.connecting);
                    break;
                case TelephonyManager.DATA_CONNECTED:
                    s = c.getString(R.string.connected);
                    break;
                case TelephonyManager.DATA_SUSPENDED:
                    s = c.getString(R.string.suspended);
                    break;
                case TelephonyManager.DATA_DISCONNECTING:
                    s = c.getString(R.string.disconnecting);
                    break;
                default:
                    s = c.getString(R.string.unknown);
                    break;
            }
            return s;
        } catch (Exception e) {
            return "";
        }
    }

    public String getSimstate() {
        try {
            String s;
            TelephonyManager phoneMgr = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
            switch (phoneMgr.getSimState()) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    s = c.getString(R.string.absent);
                    break;
                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                    s = c.getString(R.string.pin_required);
                    break;
                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                    s = c.getString(R.string.puk_required);
                    break;
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                    s = c.getString(R.string.network_locked);
                    break;
                case TelephonyManager.SIM_STATE_READY:
                    s = c.getString(R.string.ready);
                    break;
                case TelephonyManager.SIM_STATE_NOT_READY:
                    s = c.getString(R.string.not_ready);
                    break;
                case TelephonyManager.SIM_STATE_PERM_DISABLED:
                    s = c.getString(R.string.perm_disabled);
                    break;
                case TelephonyManager.SIM_STATE_CARD_IO_ERROR:
                    s = c.getString(R.string.card_io_error);
                    break;
                case TelephonyManager.SIM_STATE_CARD_RESTRICTED:
                    s = c.getString(R.string.card_restricted);
                    break;
                default:
                    s = c.getString(R.string.unknown);
                    break;
            }
            return s;
        } catch (Exception e) {
            return "";
        }
    }

    public String getnetworktype() {
        try {
            String s;
            TelephonyManager phoneMgr = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
            int i = 5050;
            if (ActivityCompat.checkSelfPermission(c, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                i = phoneMgr.getDataNetworkType();
            }

            switch (i) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    s = "1xRTT";
                    break;
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    s = "CDMA";
                    break;
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    s = "EDGE";
                    break;
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    s = "EHRPD";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    s = "EVDO_0";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    s = "EVDO_A";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    s = "EVDO_B";
                    break;
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    s = "GPRS";
                    break;
                case TelephonyManager.NETWORK_TYPE_GSM:
                    s = "GSM";
                    break;
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    s = "HSDPA";
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    s = "HSPA";
                    break;
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    s = "HSPAP";
                    break;
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    s = "HSUPA";
                    break;
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    s = "IDEN";
                    break;
                case TelephonyManager.NETWORK_TYPE_IWLAN:
                    s = "IWLAN";
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    s = "LTE";
                    break;
                case TelephonyManager.NETWORK_TYPE_NR:
                    s = "NR";
                    break;
                case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    s = "TD_SCDMA";
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    s = "UMTS";
                    break;
                default:
                    s = c.getString(R.string.unknown);
                    break;
            }
        return s;
        }catch (Exception e){
            return "";
        }
    }

    public String a() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long freeBlocksLong = statFs.getFreeBlocksLong() * blockSizeLong;
        return "\n" +
                getSize(statFs.getBlockCountLong() * blockSizeLong) +
                " " +
                c.getString(R.string.total) +
                " " +
                "\n" +
                getSize(freeBlocksLong) +
                " " +
                c.getString(R.string.free) +
                " ";
    }

    public static String formatdate(String str){
        String str2;
        String str3 = "";
        int i = 0;
        boolean z = false;
        String trim = str.trim();
        int length = trim.length() - 1;
        for (int i2 = 0; i2 <= length; i2++) {
            String ObjectToString = String.valueOf(trim.charAt(i2));
            switch (switchObjectToInt(true, "1234567890".contains(ObjectToString), "./- ".contains(ObjectToString))) {
                case 0:
                    break;
                case 1:
                    i++;
                    if (!str3.equals("")) {
                        if (!str3.equals(ObjectToString)) {
                            z = true;
                        }
                    } else {
                        str3 = ObjectToString;
                    }
                    break;
                default:
                    z = true;
                    break;
            }
        }
        if (z) {
            return trim;
        }
        if (str3.equals("")) {
            switch (switchObjectToInt(trim.length(), 7, 6, 5)) {
                case 0:
                    str2 = trim.substring(0, 4) + "0" + trim.substring(4);
                    break;
                case 1:
                    str2 = "20" + trim;
                    break;
                case 2:
                    String str4 = "20" + trim;
                    str2 = str4.substring(0, 4) + "0" + str4.substring(4);
                    break;
                default:
                    str2 = trim;
                    break;
            }
            if (str2.length() == 8) {
                return str2.substring(0, 4) + "/" + str2.substring(4, 6) + "/" + str2.substring(6);
            }
            return str2;
        } else if (i == 2) {
            return trim.replace(str3, "/");
        } else {
            return trim;
        }
    }

    public String a(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b2 : digest) {
                StringBuilder hexString = new StringBuilder(Integer.toHexString(b2 & 255));
                while (hexString.length() < 2) {
                    hexString.insert(0, "0");
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }


    public String eee() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long freeBlocksLong = statFs.getFreeBlocksLong() * blockSizeLong;
        return "\n" +
                getSize(statFs.getBlockCountLong() * blockSizeLong) +
                " " +
                c.getString(R.string.total) +
                " " +
                "\n" +
                getSize(freeBlocksLong) +
                " " +
                c.getString(R.string.free) +
                " ";
    }

    public static String getDevice() {
        return Build.DEVICE;
    }

    public static String getDeviceNameinsettings(Context c) {
        //device name in settings -> about device
        String s;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                s = Settings.Global.getString(c.getContentResolver(), Settings.Global.DEVICE_NAME);
            }else{
                s = c.getString(R.string.unknown);
            }
        }catch (Exception e){
            s = "Error Getting Device Name In Settings : " + e;
        }
        return s;
    }

    public static String getDis() {
        //device bootloader and id info like -> M1AJQ.T585XXU6CTB1
        String s;
        try{
            s = Build.DISPLAY;
        }catch (Exception e){
            s = "Error Getting Device Display Version : " + e.toString();
        }
        return s;
    }

    public String warranty() {
        String warr = getsystemproperties("ro.boot.warranty_bit", "ro.warranty_bit");
        if (!warr.equals("")) {
            if (warr.equals("0") || warr.equals("1")) {
                return "0x" + warr;
            }
            return warr;
        } else if (Build.MANUFACTURER.equals("samsung")) {
            return c.getString(R.string.nowarr);
        } else {
            return warr;
        }
    }

    @SuppressLint("PrivateApi")
    public String get(String key) throws Exception {
        String ret;
        ClassLoader cl = c.getClassLoader();
        @SuppressWarnings("rawtypes")
        Class SystemProperties = cl.loadClass("android.os.SystemProperties");
        @SuppressWarnings("rawtypes")
        Class[] paramTypes= new Class[1];
        paramTypes[0]= String.class;
        Method get = SystemProperties.getMethod("get", paramTypes);
        Object[] params= new Object[1];
        params[0]= key;
        ret=(String) get.invoke(SystemProperties, params);
        return ret;
    }

    public String getsystemproperties(String str, String str2) {
        try {
            String first = get(str);
            String secound = get(str2);
            if (first.equalsIgnoreCase(secound)) {
                return secound;
            }
            if (first.toUpperCase().startsWith(secound.toUpperCase() + ",") || first.toUpperCase().endsWith("," + secound.toUpperCase())) {
                return first;
            }
            if (secound.toUpperCase().startsWith(first.toUpperCase() + ",") || secound.toUpperCase().endsWith("," + first.toUpperCase())) {
                return secound;
            }
            switch (switchObjectToInt("", first, secound)) {
                case 0:
                    return secound;
                case 1:
                    return first;
                default:
                    return first + " / " + secound;
            }
        }catch (Exception e){
            return "";
        }
    }

    public String manudate() {
        try {
        return formatdate(get("ril.rfcal_date"));
        }catch (Exception e){
            return "";
        }
    }


    public String procode() {
        try {
        return get("ril.product_code");
    }catch (Exception e){
        return "";
    }
    }

    public String getfirmwarestate() {
        try {
        String upperCase = get("ro.build.official.release");
        if(upperCase == null || upperCase.equals("")){
            return "";
        }else{
            upperCase = upperCase.toUpperCase();
        }
        if (upperCase.equals("1") || upperCase.equals("Y") || upperCase.equals("YES") || upperCase.equals("TRUE")) {
            upperCase = c.getString(R.string.official);
        } else if (upperCase.equals("0") || upperCase.equals("N") || upperCase.equals("NO") || upperCase.equals("FALSE")) {
            upperCase = c.getString(R.string.custom);
        }
        return upperCase;
        }catch (Exception e){
            return "";
        }
    }




    public String getcountryoforigin(String str) {
        String str2 = "";
        if ((!str.startsWith("R") && !str.startsWith("C")) || str.length() <= 5) {
            return str2;
        }
        switch (switchObjectToInt(str.charAt(1),
                ObjectToChar("1"),
                ObjectToChar("2"),
                ObjectToChar("3"),
                ObjectToChar("4"),
                ObjectToChar("5"),
                ObjectToChar("6"),
                ObjectToChar("7"),
                ObjectToChar("8"),
                ObjectToChar("9"),
                ObjectToChar("A"),
                ObjectToChar("B"),
                ObjectToChar("N"),
                ObjectToChar("P"),
                ObjectToChar("Q"),
                ObjectToChar("R"),
                ObjectToChar("T"),
                ObjectToChar("U"),
                ObjectToChar("V"),
                ObjectToChar("Y"),
                ObjectToChar("D"),
                ObjectToChar("F"),
                ObjectToChar("G"),
                ObjectToChar("J"),
                ObjectToChar("L"),
                ObjectToChar("S"),
                ObjectToChar("Z"))) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                str2 = c.getString(R.string.korea);
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
                str2 = c.getString(R.string.china);
                break;
            case 19:
            case 20:
            case 21:
            case 22:
                str2 = c.getString(R.string.vietnam);
                break;
            case 23:
            case 24:
                str2 = c.getString(R.string.brazil);
                break;
            case 25:
                str2 = c.getString(R.string.india);
                break;
        }
        switch (switchObjectToInt(str.substring(1, 3), "21", "28", "51", "52", "58", "5A")) {
            case 0:
            case 1:
                str2 = c.getString(R.string.china);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                str2 = c.getString(R.string.vietnam);
                break;
        }
        if (str2.equals("")) {
            return str2;
        }
        return c.getString(R.string.made_in) + " " + str2;
    }

    public static int switchObjectToInt(Object test, Object... values) {
        if (test instanceof Number) {
            double t = ((Number) test).doubleValue();
            for (int i = 0; i < values.length; i++) {
                if (values[i].equals(t)) {
                    return i;
                }
            }
            return -1;
        }
        for (int i2 = 0; i2 < values.length; i2++) {
            if (test.equals(values[i2])) {
                return i2;
            }
        }
        return -1;
    }

    public char ObjectToChar(Object o) {
        if (o instanceof Character) {
            return (Character) o;
        }
        return CharFromString(o.toString());
    }

    public char CharFromString(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        return s.charAt(0);
    }

    public static String getbatteryhealth(int h, Context c) {
        String healthLbl;
        switch (h) {
            case BatteryManager.BATTERY_HEALTH_COLD:
                healthLbl = c.getString(R.string.health_cold);
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthLbl = c.getString(R.string.health_dead);
                break;

            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthLbl = c.getString(R.string.health_good);
                break;

            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthLbl = c.getString(R.string.health_overvolt);
                break;

            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthLbl = c.getString(R.string.health_overheat);
                break;

            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                healthLbl = c.getString(R.string.unspec_health);
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
            default:
                healthLbl = c.getString(R.string.unknown);
                break;
        }
        return healthLbl;
    }

    public static String getbatterypercentage(int level, int scale) {
        if (level != -1 && scale != -1) {
            int batteryPct = (int) ((level / (float) scale) * 100f);
            return batteryPct + " %";
        }
        return "0 %";
    }

    public static String getpluggedstate(Context c, int g) {
        String pluggedLbl;
        switch (g) {
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                pluggedLbl = c.getString(R.string.wire);
                break;

            case BatteryManager.BATTERY_PLUGGED_USB:
                pluggedLbl = "USB";
                break;

            case BatteryManager.BATTERY_PLUGGED_AC:
                pluggedLbl = c.getString(R.string.usb_main);
                break;

            default:
                pluggedLbl = c.getString(R.string.none);
                break;
        }
        return pluggedLbl;
    }

    public static String getchargestate(Context c, int s) {
        String statusLbl;
        switch (s) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                statusLbl = c.getString(R.string.charge);
                break;

            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                statusLbl = c.getString(R.string.discharge);
                break;

            case BatteryManager.BATTERY_STATUS_FULL:
                statusLbl = c.getString(R.string.batfull);
                break;

            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                statusLbl = c.getString(R.string.unknown);
                break;

            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
            default:
                statusLbl = "Not Charging";
                break;
        }
        return statusLbl;
    }


    public static long getBatteryCapacity(Context context) {
        long d2;
            BatteryManager mBatteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
            int chargeCounter = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            int capacity = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            d2 = (chargeCounter / capacity) * 100L;

           if(d2 == 0){
               Object mPowerProfile;
               double batteryCapacity = 0;
               final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

               try {
                   mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                           .getConstructor(Context.class)
                           .newInstance(context);

                   batteryCapacity = (double) Class
                           .forName(POWER_PROFILE_CLASS)
                           .getMethod("getBatteryCapacity")
                           .invoke(mPowerProfile);

               } catch (Exception e) {
                   e.printStackTrace();
               }

               d2 = (long) batteryCapacity;
           }
           return d2;
    }



}
