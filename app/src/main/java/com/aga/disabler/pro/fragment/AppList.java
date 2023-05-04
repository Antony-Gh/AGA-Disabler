package com.aga.disabler.pro.fragment;

import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import java.util.List;

public class AppList {
    public String name;
    public String packages;
    public byte[] icon;
    public PackageInfo pkginf;
    public boolean sys;
    private ComponentName comp;

    public void setname(String str) {
        this.name = str;
    }

    public void setpackages(String str) {
        this.packages = str;
    }

    public void seticon(byte[] str) {
        this.icon = str;
    }

    public void setsystem(boolean str) {
        this.sys = str;
    }


    public AppList() {
    }



    public static void sortArrayList(List<AppList> list) {
        list.sort((o1, o2) -> o1.name.compareTo(o2.name));
    }
    
        }