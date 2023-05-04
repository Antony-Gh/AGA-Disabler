package com.aga.disabler.pro.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

public class FragmentHolder extends Fragment {
    private String title;
    protected Context c;
    protected View view;
    protected String pkg;
    protected Activity act;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContext(Context context) {
        this.c = context;
    }

    public void setpkg(String s){
        this.pkg = s;
    }

    public String getTitle() {
        return title;
    }

    public void setact(Activity s){
        this.act = s;
    }

}
