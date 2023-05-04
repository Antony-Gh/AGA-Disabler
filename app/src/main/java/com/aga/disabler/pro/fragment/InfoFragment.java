package com.aga.disabler.pro.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.tools.appinfo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoFragment extends FragmentHolder {
    private ArrayList<HashMap<Object, Object>> array;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_fragment, container, false);
        appinfo appinfoo = new appinfo(c, pkg);
        array = appinfoo.getallappinfo();
        setContent(R.id.app_size_layout, c.getString(R.string.app_size), a("size"));
        setContent(R.id.app_install_source_layout, c.getString(R.string.install_src), a("install source"));
        setContent(R.id.app_install_date_layout, c.getString(R.string.install_date), a("install date"));
        setContent(R.id.app_last_modify_layout, c.getString(R.string.last_modify), a("last modify"));
        setContent(R.id.app_targetsdk_layout, c.getString(R.string.target_sdk), a("target sdk"));
        setContent(R.id.app_targetsdk_name_layout, c.getString(R.string.target_version), a("target sdk name"));
        setContent(R.id.app_minsdk_layout, c.getString(R.string.min_sdk), a("min sdk"));
        setContent(R.id.app_minsdk_name_layout, c.getString(R.string.min_version), a("min sdk name"));
        setContent(R.id.app_meta_data, "App meta data", (String) array.get(0).get("metadata"));
        setContent(R.id.app_signature, "App Signature", (String) array.get(0).get("signature"));
        setContent(R.id.app_native_lib, "App Native Libraries", (String) array.get(0).get("nativelib"));
        return view;
    }

    private String a(String s) {
        return String.valueOf(array.get(0).get(s));
    }

    private void setContent(int content_layout_title, String content_title, String content_des) {
        LinearLayout con = view.findViewById(content_layout_title);
        LinearLayout con_linear = con.findViewById(R.id.content_linear);
        TextView con_title = con.findViewById(R.id.content_title);
        TextView con_des = con.findViewById(R.id.content_description);
        con_title.setText(content_title);
        con_des.setText(content_des);
        con_linear.setOnClickListener(v -> {
            //TOD
        });
    }

    public static InfoFragment getInstance(Context context, String pkgname) {
        InfoFragment fragment = new InfoFragment();
        fragment.setContext(context);
        fragment.setTitle("Info");
        fragment.setpkg(pkgname);
        return fragment;
    }

}
