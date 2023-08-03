package com.aga.disabler.pro.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.receiver.devicepolicy;
import com.aga.disabler.pro.tools.MyAppinfo;

import org.jetbrains.annotations.NotNull;

import static com.aga.disabler.pro.tools.Helper.galaxystore;
import static com.aga.disabler.pro.tools.Helper.getAttributeColor;
import static com.aga.disabler.pro.tools.Helper.googleit;
import static com.aga.disabler.pro.tools.Helper.isenableapp;
import static com.aga.disabler.pro.tools.Helper.launchapp;
import static com.aga.disabler.pro.tools.Helper.playstore;

public class DashboardFragment extends FragmentHolder {
    private TextView state;
    private Button launch;
    private Button openset;
    private Button disable_app;
    private Button uni_app;
    private Button play_but;
    private Button galaxy_but;
    private Button google;
    private devicepolicy dp;

    public static DashboardFragment getInstance(Context context, String pkgname, Activity a) {
        DashboardFragment fragment = new DashboardFragment();
        fragment.setContext(context);
        fragment.setTitle("Dashboard");
        fragment.setpkg(pkgname);
        fragment.setact(a);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appinfo_dashboard, container, false);
        init();
        initviews();
        return view;
    }

    public void init() {
        state = view.findViewById(R.id.state);
        launch = view.findViewById(R.id.launch);
        openset = view.findViewById(R.id.open_settings);
        disable_app = view.findViewById(R.id.disable_app);
        uni_app = view.findViewById(R.id.uni_app);
        play_but = view.findViewById(R.id.play_but);
        galaxy_but = view.findViewById(R.id.galaxy_but);
        google = view.findViewById(R.id.google_but);
        dp = new devicepolicy(c);
    }

    public void initviews() {
        if (isenableapp(c, pkg)) {
            state.setText(R.string.ena);
            state.setTextColor(getAttributeColor(c, R.attr.permission_text_color_true));
            disable_app.setText(R.string.disable_app);
        } else {
            state.setText(R.string.dis);
            state.setTextColor(getAttributeColor(c, R.attr.permission_text_color_false));
            disable_app.setText(R.string.enable_app);
        }
        launch.setOnClickListener(v -> launchapp(c, pkg));
        openset.setOnClickListener(v -> MyAppinfo.activeSettingsPer(c, pkg));
        disable_app.setOnClickListener(v -> {
            if (isenableapp(c, pkg)) {
                if (dp.disableapps(c, pkg)) {
                    disable_app.setText(R.string.enable_app);
                    state.setText(R.string.dis);
                    state.setTextColor(getAttributeColor(c, R.attr.permission_text_color_false));
                }
            } else {
                if (dp.enableapps(c, pkg)) {
                    disable_app.setText(R.string.disable_app);
                    state.setText(R.string.ena);
                    state.setTextColor(getAttributeColor(c, R.attr.permission_text_color_true));
                }
            }
        });
        uni_app.setOnClickListener(v -> {
            if (dp.uninstallapps(c, pkg)) {
                act.finish();
            }
        });
        play_but.setOnClickListener(v -> playstore(c, pkg));
        galaxy_but.setOnClickListener(v -> galaxystore(c, pkg));
        google.setOnClickListener(v -> googleit(c, pkg));
    }

}
