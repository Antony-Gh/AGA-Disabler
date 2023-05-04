package com.aga.disabler.pro.adapters;

import static com.aga.disabler.pro.tools.Helper.Bytes2Bitmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.fragment.AppGameModeList;

import java.util.Collections;
import java.util.List;

public class GameModeAdapter extends BaseAdapter{
    private List<AppGameModeList> listStorage;
    public boolean userscroll;

    public GameModeAdapter() {
    }

    public void setData(List<AppGameModeList> details) {
        if (details == null) {
            details = Collections.emptyList();
        }
        this.listStorage = details;
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return this.listStorage.size();
    }

    public AppGameModeList getItem(int i) {
        return this.listStorage.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(int i, View currentView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (currentView == null) {
            currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gamemodeitem, parent, false);
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

    public List<AppGameModeList> getListStorage() {return listStorage;}


    private static class ViewHolder {
        private final ImageView img;
        private final TextView name;
        private final TextView pkg;
        private final LinearLayout layout;
        private final Context con;



        public ViewHolder(View _view, Context c) {
            this.con = c;
			img =  _view.findViewById(R.id.img);
			name = _view.findViewById(R.id.name);
			pkg = _view.findViewById(R.id.pkg);
            layout = _view.findViewById(R.id.layout);
        }

        public void bind(AppGameModeList city) {
            String pkgname = city.packages;
            this.name.setText(city.name);
            this.pkg.setText(pkgname);
            img.setImageBitmap(Bytes2Bitmap(city.icon));
            int typ = city.type;
            switch (typ){
                case 0:
                    layout.setBackgroundColor(con.getResources().getColor(R.color.white));
                break;
                case 1:
                    layout.setBackgroundColor(con.getResources().getColor(R.color.yes_green));
                break;
                case 2:
                    layout.setBackgroundColor(con.getResources().getColor(R.color.no_red));
                break;
            }

            }


            }




            }
