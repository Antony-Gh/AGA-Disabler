package com.aga.disabler.pro.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.aga.disabler.pro.R;
import com.aga.disabler.pro.tools.appinfo;

import java.util.ArrayList;
import java.util.HashMap;


public class AboutActivity extends AppCompatActivity {
    private ArrayList<HashMap<Object, Object>> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context c = this.getApplicationContext();
        setContentView(R.layout.activity_about);
        appinfo appinf = new appinfo(c, "com.aga.disabler.pro");
        array = appinf.getallappinfo();

        TextView a= findViewById(R.id.appname1);
        TextView vern = findViewById(R.id.version_name);
        TextView verc = findViewById(R.id.version_code);

        String appname = c.getResources().getString(R.string.app_name_title) + " " + array.get(0).get("name");
        String vername = c.getResources().getString(R.string.version_name) + " " + array.get(0).get("ver name");
        String vercode = c.getResources().getString(R.string.version_code) + " " + array.get(0).get("ver code");

        a.setText(appname);
        vern.setText(vername);
        verc.setText(vercode);

    }

}