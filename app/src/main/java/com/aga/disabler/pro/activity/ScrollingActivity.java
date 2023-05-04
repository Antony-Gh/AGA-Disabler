package com.aga.disabler.pro.activity;

import static com.aga.disabler.pro.tools.Helper.getimei;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.adapters.Listview1Adapter;
import com.aga.disabler.pro.license.ExecutorServiceII;
import com.aga.disabler.pro.tools.AboutPhoneHelper;
import com.aga.disabler.pro.tools.Helper;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ScrollingActivity extends AppCompatActivity implements ExecutorServiceII.Tasks {
    private AboutPhoneHelper aboutPhoneHelper;
    private ScrollView scrollView;
    private LinearLayout linloadall;
    private HashMap<String, Object> values;
    private ExecutorServiceII exe;

    @Override
    public void doinbackground() {
        init();
        initviews();
    }

    @Override
    public void onpreexecute() {
        linloadall.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
    }

    @Override
    public void onpostexecute() {
        linloadall.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);

    }

    private void initviews() {
        //Wifi
        setheader(R.id.wifiheader, getString(R.string.wifi_title));
        setContent(R.id.macaddress, getString(R.string.mac_address), values.get("mac"));
        setContent(R.id.ipaddress, getString(R.string.ip_address), values.get("ip"));
        setContent(R.id.wifistates, getString(R.string.wifi_status), values.get("wifistate"));
        setContent(R.id.wificonfig, getString(R.string.network_config), values.get("netconfig"));

        //Device Details
        setheader(R.id.devicedetailsheader, getString(R.string.device_details));
        setContent(R.id.devicename, getString(R.string.device_nam), values.get("devname"));
        setContent(R.id.manufacturerlayout, getString(R.string.manufacturer), values.get("manu"));
        setContent(R.id.devicemodel, getString(R.string.model), values.get("devmodel"));
        setContent(R.id.codename, getString(R.string.code_name), values.get("codename"));
        setContent(R.id.serialnoo, getString(R.string.serial_no), values.get("serno"));
        setContent(R.id.imei, getString(R.string.imei_tit), values.get("imei"));
        setContent(R.id.androidver, getString(R.string.android_info), values.get("androidinfo"));
        setContent(R.id.bootloaderbuild, getString(R.string.build_info), values.get("buildinfo"));
        setContent(R.id.kernalinfo, getString(R.string.kernal_info), values.get("kernalinfo"));
        setContent(R.id.rootinfo, getString(R.string.rooted_info), values.get("root"));
        setContent(R.id.firestate, getString(R.string.firmstate), values.get("firestate"));
        setContent(R.id.warranty_info, getString(R.string.warr_void), values.get("war"));
        setContent(R.id.country_of_origin, getString(R.string.countryoforigin), values.get("origin"));
        setContent(R.id.build_date, getString(R.string.manu_date), values.get("builddate"));
        setContent(R.id.dev_color, getString(R.string.colo), values.get("devcolor"));
        setContent(R.id.all_details, "All Device Details", values.get("alldet"));

        //Display info
        setheader(R.id.Displayinfo, getString(R.string.display_info));
        setContent(R.id.resolution, getString(R.string.resolution), values.get("res"));
        setContent(R.id.density, getString(R.string.density_scaling), values.get("den"));
        setContent(R.id.screenrefreshrate, "Screen Refresh Rate : ", values.get("refrate"));
        setContent(R.id.dpi, getString(R.string.dpi_info), aboutPhoneHelper.getDpi());
        setContent(R.id.screentimeout, "Screen Time Out : ", aboutPhoneHelper.screentimeout());
        setContent(R.id.pixforx, getString(R.string.pixels_x), aboutPhoneHelper.getpixelsforx());
        setContent(R.id.pixfory, getString(R.string.pixels_y), aboutPhoneHelper.getpixelsfory());
        setContent(R.id.bright, getString(R.string.brightness), aboutPhoneHelper.brightness());
        setContent(R.id.screenwidth, "Screen Width : ", aboutPhoneHelper.getscreeninfo(1, ScrollingActivity.this));
        setContent(R.id.screenheight, "Screen Height : ", aboutPhoneHelper.getscreeninfo(2, ScrollingActivity.this));
        setContent(R.id.screeninches, "Screen Inches : ", aboutPhoneHelper.getscreeninfo(3, ScrollingActivity.this));

        //Sensor info
        setheader(R.id.Sensorinfo, getString(R.string.sensor_info));
        setContent(R.id.sensordetails, getString(R.string.sensor_details), aboutPhoneHelper.sensordetails());

        //Proc
        setheader(R.id.processor_info, "Processor-Info");
        setContent(R.id.procinfo, "Processor : ", aboutPhoneHelper.processorinfo());
        setContent(R.id.proccores, "Processor Cores : ", aboutPhoneHelper.proccores());

        //Loc
        setheader(R.id.locinfo, "Location-Info");
        setContent(R.id.loc, "Location : ", aboutPhoneHelper.getlocinfo());


        //Other
        setheader(R.id.Otherinfo, getString(R.string.other_info));
        setContent(R.id.ramdetails, getString(R.string.ram_info), aboutPhoneHelper.getRam());
        setContent(R.id.storagedetails, getString(R.string.storage_info), aboutPhoneHelper.Storagesettings());
        setContent(R.id.countrycode, getString(R.string.dev_country_code), aboutPhoneHelper.getCountryCode());
        setContent(R.id.productcode, getString(R.string.pro_code), aboutPhoneHelper.procode());
        setContent(R.id.securitypatchlvl, "Security Patch Level : ", aboutPhoneHelper.getsecuritypatchlvl());
        setContent(R.id.devid, "Device ID : ", getimei(this));
        setContent(R.id.javavmversion, "Java VM Version : ", aboutPhoneHelper.javavm());


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        exe = new ExecutorServiceII.ExecutorBuilder().setTasks(this).build();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        scrollView = findViewById(R.id.scrollall);
        linloadall = findViewById(R.id.load_linear);
        registerReceiver(batteryInfoReceiver, intentFilter);
        values = new HashMap<>();
        exe.execute();
    }

    private final BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            HandleIntent(intent);
        }
    };


    private void init() {
        aboutPhoneHelper = new AboutPhoneHelper(ScrollingActivity.this.getApplicationContext());
        //Wifi
        values.put("mac", aboutPhoneHelper.getMacAddress("wlan0"));
        values.put("ip", aboutPhoneHelper.getIPAddress());
        values.put("wifistate", aboutPhoneHelper.getWifiStates());
        values.put("netconfig", aboutPhoneHelper.getNetwork());

        //Device Details
        values.put("devname", aboutPhoneHelper.getdevicename());
        values.put("manu", aboutPhoneHelper.getManufacture());
        values.put("devmodel", aboutPhoneHelper.getModel());
        values.put("codename", aboutPhoneHelper.getCodeName());
        values.put("serno", aboutPhoneHelper.getSerialNo());
        values.put("imei", Helper.getimei(this));
        values.put("androidinfo", aboutPhoneHelper.getAndroidInfo());
        values.put("buildinfo", aboutPhoneHelper.getBuildNo());
        values.put("kernalinfo", aboutPhoneHelper.getKernel());
        values.put("root", aboutPhoneHelper.isDeviceRooted());
        values.put("firestate", aboutPhoneHelper.getfirmwarestate());
        values.put("war", aboutPhoneHelper.warranty());
        values.put("origin", aboutPhoneHelper.getcountryoforigin(aboutPhoneHelper.getSerialNumber()));
        values.put("builddate", aboutPhoneHelper.manudate());
        values.put("devcolor", aboutPhoneHelper.getcolor());
        values.put("alldet", aboutPhoneHelper.allbuildinfo());

        //Display info
        values.put("res", aboutPhoneHelper.getResolution());
        values.put("den", aboutPhoneHelper.getDensity());
        values.put("refrate", aboutPhoneHelper.screenrefrate());
        values.put("dpi", aboutPhoneHelper.getDpi());
        values.put("timeout", aboutPhoneHelper.screentimeout());
        values.put("pixx", aboutPhoneHelper.getpixelsforx());
        values.put("pixy", aboutPhoneHelper.getpixelsfory());
        values.put("bright", aboutPhoneHelper.brightness());
        values.put("width", aboutPhoneHelper.getscreeninfo(1, ScrollingActivity.this));
        values.put("height", aboutPhoneHelper.getscreeninfo(2, ScrollingActivity.this));
        values.put("inch", aboutPhoneHelper.getscreeninfo(3, ScrollingActivity.this));

        //Sensor
        values.put("sensor", aboutPhoneHelper.sensordetails());

        //Processor
        values.put("procinfo", aboutPhoneHelper.processorinfo());
        values.put("proccores", aboutPhoneHelper.proccores());

        //GPU




        //Other
        values.put("ram", aboutPhoneHelper.getRam());
        values.put("storage", aboutPhoneHelper.Storagesettings());
        values.put("coucode", aboutPhoneHelper.getCountryCode());
        values.put("procode", aboutPhoneHelper.procode());
        values.put("patchlevel", aboutPhoneHelper.getsecuritypatchlvl());
        values.put("devid", getimei(this));
        values.put("vmver", aboutPhoneHelper.javavm());






    }


    private void setheader(int headerlayoutid, String headertitle) {
        LinearLayout devicedetail = findViewById(headerlayoutid);
        TextView devicetitle = devicedetail.findViewById(R.id.header_title);
        devicetitle.setText(headertitle);
    }

    private void setContent(int contentlayouttitle, String contentitle, Object contentdes) {
        String contet = String.valueOf(contentdes);
        LinearLayout wifimac = findViewById(contentlayouttitle);
        LinearLayout contentlinear = wifimac.findViewById(R.id.content_layout);
        TextView mactitle = wifimac.findViewById(R.id.contenttitle);
        TextView macadd = wifimac.findViewById(R.id.contentdes);
        mactitle.setText(contentitle);
        macadd.setText(contet);
        contentlinear.setOnClickListener(v -> {
            //TODO
        });
    }


    public void HandleIntent(Intent intent) {
        Context c = ScrollingActivity.this.getApplicationContext();
        String batteryhealth;
        String batterylevel;
        String usbcablestatus;
        String batterychargingstate;
        String batterytech;
        String batterytemp;
        String batteryvolt;
        String batterycap;
        if (intent.getBooleanExtra("present", false)) {
            batteryhealth = AboutPhoneHelper.getbatteryhealth(intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0), c);
            batterylevel = AboutPhoneHelper.getbatterypercentage(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1), intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1));
            usbcablestatus = AboutPhoneHelper.getpluggedstate(c, intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0));
            batterychargingstate = AboutPhoneHelper.getchargestate(c, intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1));
            if (intent.getExtras() != null) {
                String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
                if (!"".equals(technology)) {
                    batterytech = technology;
                } else {
                    batterytech = c.getString(R.string.unknown);
                }
            }else{
                batterytech = c.getString(R.string.unknown);
            }
            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            if (temperature > 0) {
                float temp = ((float) temperature) / 10f;
                batterytemp = temp + " °C";
            }else{
                batterytemp = c.getString(R.string.unknown);
            }
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            if (voltage > 0) {
                batteryvolt = voltage + " mV";
            }else{
                batteryvolt = c.getString(R.string.unknown);
            }
            long capacity = AboutPhoneHelper.getBatteryCapacity(c);
            if (capacity > 0) {
                batterycap = capacity + " mAh";
            }else{
                batterycap = c.getString(R.string.unknown);
            }
        }else{
            batteryhealth = c.getString(R.string.unknown);
            batterylevel = c.getString(R.string.unknown);
            usbcablestatus = c.getString(R.string.unknown);
            batterychargingstate = c.getString(R.string.unknown);
            batterytech = c.getString(R.string.unknown);
            batterytemp = c.getString(R.string.unknown);
            batteryvolt = c.getString(R.string.unknown);
            batterycap = c.getString(R.string.unknown);
        }
        setheader(R.id.Batteryinfo, getString(R.string.battery_info));
        setContent(R.id.batteryhealth, getString(R.string.battery_health), batteryhealth);
        setContent(R.id.batterylevel, getString(R.string.battery_level), batterylevel);
        setContent(R.id.batterychargingstate, getString(R.string.battery_charge), batterychargingstate);
        setContent(R.id.batterytech, getString(R.string.battery_tech), batterytech);
        setContent(R.id.batterytemp, getString(R.string.battery_temp), batterytemp);
        setContent(R.id.batteryvolt, getString(R.string.battery_volt), batteryvolt);
        setContent(R.id.batterycap, getString(R.string.battery_cap), batterycap);
        setContent(R.id.usbcable, getString(R.string.usb_state), usbcablestatus);
    }
@Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryInfoReceiver);
    }
}