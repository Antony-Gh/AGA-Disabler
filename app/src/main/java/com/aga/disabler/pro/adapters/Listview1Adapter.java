package com.aga.disabler.pro.adapters;

import static com.aga.disabler.pro.tools.Helper.Bytes2Bitmap;
import static com.aga.disabler.pro.tools.Helper.mType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.fragment.AppList;
import com.aga.disabler.pro.receiver.devicepolicy;
import com.aga.disabler.pro.tools.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Listview1Adapter extends BaseAdapter {
    private final List<AppList> OriginalArray;
    private List<AppList> listStorage;
    public boolean userscroll;

    public Listview1Adapter(List<AppList> list) {
        this.OriginalArray = list;
    }

    public void setData(List<AppList> details) {
        if (details == null) {
            details = Collections.emptyList();
        }
        this.listStorage = details;
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return this.listStorage.size();
    }

    public AppList getItem(int i) {
        return this.listStorage.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(int i, View currentView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (currentView == null) {
            currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.installed_app_list, parent, false);
            viewHolder = new ViewHolder(currentView, parent.getContext());
            currentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) currentView.getTag();
        }
        viewHolder.bind(this.listStorage.get(i));
        if(userscroll) {
            Animation anim = AnimationUtils.loadAnimation(parent.getContext(), R.anim.listloadinganim);
            currentView.startAnimation(anim);
        }
        return currentView;
    }

    public List<AppList> getListStorage() {
        return listStorage;
    }

    public List<AppList> getData() {
        return OriginalArray;
    }


    private class ViewHolder {
        private final ImageView img;
        private final TextView name;
        private final TextView pkg;
        private final Context con;
        private final devicepolicy dp;
        private final AppCompatImageView imgg;



        public ViewHolder(View _view, Context c) {
            this.con = c;
			img =  _view.findViewById(R.id.img);
			name = _view.findViewById(R.id.name);
			pkg = _view.findViewById(R.id.pkg);
			imgg = _view.findViewById(R.id.imagebutton);
			dp = new devicepolicy(con);
        }

        public void bind(AppList city) {
            String pkgname = city.packages;
            this.name.setText(city.name);
            this.pkg.setText(pkgname);
            img.setImageBitmap(Bytes2Bitmap(city.icon));
            int typ =  mType(pkgname, con);

            switch (typ) {
                case Helper.appenbaled :
                        imgg.setImageLevel(1);
                    break;
                case Helper.appdisabled :
                        imgg.setImageLevel(2);
                    break;
            }

            imgg.setOnClickListener(v -> {
                switch (typ) {
                    case Helper.appenbaled :
                        if(dp.disableapps(con, pkgname)){
                            imgg.setImageLevel(2);
                        }else{
                            imgg.setImageLevel(1);
                        }
                        break;
                    case Helper.appdisabled :
                        if(dp.enableapps(con, pkgname)){
                            imgg.setImageLevel(1);
                        }else{
                            imgg.setImageLevel(2);
                        }
                        break;
                }
                notifyDataSetChanged();
            });
            }
            }




            }
