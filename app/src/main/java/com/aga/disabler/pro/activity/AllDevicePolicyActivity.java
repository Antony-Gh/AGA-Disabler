package com.aga.disabler.pro.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.receiver.devicepolicy;
import com.aga.disabler.pro.tools.FileUtil;

import static com.aga.disabler.pro.receiver.devicepolicy.*;
import static com.aga.disabler.pro.tools.Helper.emmtoast;
import static com.aga.disabler.pro.tools.Helper.installapk;

public class AllDevicePolicyActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Context c;
    private LinearLayout apps_set_but;
    private LinearLayout install_apk;
    private final int PICK = 1111;

    private devicepolicy dev;
    private SwitchCompat android_browser;
    private SwitchCompat voice_dialer;
    private SwitchCompat youtube;
    private SwitchCompat android_market;
    private SwitchCompat non_market;
    private SwitchCompat wifi_settings_switch;
    private SwitchCompat lockscreen_settings_switch;
    private SwitchCompat statusbar_mode;
    private SwitchCompat noti_mode;
    private SwitchCompat multi_window;
    private SwitchCompat task_manager;
    private SwitchCompat multi_user;
    private SwitchCompat outcoming_calls;
    private SwitchCompat ota;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_policy);
        initviews();
        initclick();
    }

    private void initviews() {
        c = AllDevicePolicyActivity.this.getApplicationContext();
        dev = new devicepolicy(c);
        apps_set_but = findViewById(R.id.apps_set_but);
        install_apk = findViewById(R.id.install_apk);

        //Switch Compat

        android_browser = findViewById(R.id.android_browser);
        voice_dialer = findViewById(R.id.voice_dialer);
        youtube = findViewById(R.id.youtube);
        android_market = findViewById(R.id.android_market);
        non_market = findViewById(R.id.non_market);
        wifi_settings_switch = findViewById(R.id.wifi_settings_switch);
        lockscreen_settings_switch = findViewById(R.id.lockscreen_settings_switch);
        statusbar_mode = findViewById(R.id.statusbar_mode);
        noti_mode = findViewById(R.id.noti_mode);
        multi_window = findViewById(R.id.multi_window);
        task_manager = findViewById(R.id.task_manager);
        multi_user = findViewById(R.id.multi_user);
        outcoming_calls = findViewById(R.id.outcoming_calls);
        ota = findViewById(R.id.ota);

    }

    private void initclick() {
        apps_set_but.setOnClickListener(this);
        install_apk.setOnClickListener(this);

        //Switch Compat

        android_browser.setOnCheckedChangeListener(this);
        voice_dialer.setOnCheckedChangeListener(this);
        youtube.setOnCheckedChangeListener(this);
        android_market.setOnCheckedChangeListener(this);
        non_market.setOnCheckedChangeListener(this);
        wifi_settings_switch.setOnCheckedChangeListener(this);
        lockscreen_settings_switch.setOnCheckedChangeListener(this);
        statusbar_mode.setOnCheckedChangeListener(this);
        noti_mode.setOnCheckedChangeListener(this);
        multi_window.setOnCheckedChangeListener(this);
        task_manager.setOnCheckedChangeListener(this);
        multi_user.setOnCheckedChangeListener(this);
        outcoming_calls.setOnCheckedChangeListener(this);
        ota.setOnCheckedChangeListener(this);



    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/vnd.android.package-archive");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Choose APK"), PICK);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.install_apk:
                showFileChooser();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK:
                if (resultCode == RESULT_OK) {
                    String filepath;
                    if(data != null){
                        Log.d("Install Apk", "Data != null");
                        ClipData cd = data.getClipData();
                        if(cd != null){
                            Log.d("Install Apk", "ClipData != null");
                            ClipData.Item item = cd.getItemAt(0);
                            Log.d("Install Apk", "Clip Data : " + cd);
                            filepath = FileUtil.convertUriToFilePath(c, item.getUri());
                            Log.d("Install Apk", "Path : " + filepath);
                        }else{
                            Log.d("Install Apk", "ClipData == null");
                            filepath = FileUtil.convertUriToFilePath(c, data.getData());
                        }
                    }else{
                        Log.d("Install Apk", "Data == null");
                        emmtoast("data = null",c);
                        filepath = "";
                    }
                    Log.d("Install Apk", "filepath == " + filepath);
                    installapk(filepath, c);
                }
                break;

        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.android_browser:
                androidbrowse(b,c);
                break;
            case R.id.voice_dialer:
                voice_dialer(b,c);
                break;
            case R.id.youtube:
                youtube(b,c);
                break;
            case R.id.android_market:
                market(b,c);
                break;
            case R.id.non_market:
                nonmarket(b,c);
                break;
            case R.id.wifi_settings_switch:
                wifisettings(b,c);
                break;
            case R.id.lockscreen_settings_switch:
                lock(b,c);
                break;
            case R.id.statusbar_mode:
                statusbar_mode(b,c);
                break;
            case R.id.noti_mode:
                noti_mode(b,c);
                break;
            case R.id.multi_window:
                multiwindow(b,c);
                break;
            case R.id.task_manager:
                taskmanager(b,c);
                break;
            case R.id.multi_user:
                multi_user(b,c);
                break;
            case R.id.outcoming_calls:
                outgoing_calls(b,c);
                break;
            case R.id.ota:
                otaup(b,c);
                break;

        }
    }
}
