package com.aga.disabler.pro.fragment;

import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.Comparator;
import java.util.List;

public class AppGameModeList {
    public String name;
    public String packages;
    public byte[] icon;
    public int type;

    public static final int App_Enable = 1;
    public static final int App_Disable = 2;
    public static final int App_None = 0;

    public void setname(String str) {
        this.name = str;
    }

    public void setpackages(String str) {
        this.packages = str;
    }

    public void seticon(byte[] str) {
        this.icon = str;
    }

    public void settype(int s){this.type = s;}


    public AppGameModeList() {
    }



    public static void sortArrayList(List<AppGameModeList> list) {
        list.sort(Comparator.comparing(o -> o.name));
    }
    
        }