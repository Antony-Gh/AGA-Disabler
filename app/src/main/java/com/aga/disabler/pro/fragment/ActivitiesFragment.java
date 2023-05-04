package com.aga.disabler.pro.fragment;

import static com.aga.disabler.pro.tools.Helper.emmtoast;
import static com.aga.disabler.pro.tools.Helper.getarchiveinfo;
import static com.aga.disabler.pro.tools.Helper.getcompname;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aga.disabler.pro.R;
import com.samsung.android.knox.EnterpriseDeviceManager;
import com.samsung.android.knox.application.ApplicationPolicy;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivitiesFragment extends FragmentHolder{
    private ActivityInfo[] activityInfos;
    private PackageManager pkgmg;
    private ApplicationPolicy ap;

    public static ActivitiesFragment getInstance(Context context, String pkgname) {
        ActivitiesFragment fragment = new ActivitiesFragment();
        fragment.setContext(context);
        fragment.setTitle("Activities");
        fragment.setpkg(pkgname);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.premissions_fragment, container, false);
        init();
        return view;
    }

    public void init() {
        List<ActivityInfo> act = new ArrayList<>();
        pkgmg = c.getPackageManager();
        ap = EnterpriseDeviceManager.getInstance(c).getApplicationPolicy();
        LinearLayout linear2 = view.findViewById(R.id.linear2);
        ListView list = view.findViewById(R.id.listviewall);
        activityInfos = getarchiveinfo(c, pkg, PackageManager.GET_ACTIVITIES).activities;
        if(activityInfos != null && activityInfos.length != 0) {
            act = Arrays.asList(activityInfos);
        }
        if(act.isEmpty()){
            TextView t = view.findViewById(R.id.textview1);
            String o = t.getText().toString();
            String p = o.replace("%d", "Activities");
            t.setText(p);
            list.setVisibility(View.GONE);
            linear2.setVisibility(View.VISIBLE);
        }else{
            ActAdapter actAdapter = new ActAdapter(act);
            list.setOnItemClickListener((parent, view, position, id) -> {
                disableact(activityInfos[position]);
                ((ActAdapter) list.getAdapter()).notifyDataSetChanged();
            });
            list.setAdapter(actAdapter);
        }

    }

    public class ActAdapter extends BaseAdapter {
        private final List<ActivityInfo> Activitylist;

        public ActAdapter(List<ActivityInfo> act) {
            Activitylist = act;
        }

        @Override
        public int getCount() {
            return Activitylist.size();
        }

        @Override
        public ActivityInfo getItem(int position) {
            return Activitylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View currentView, ViewGroup parent) {
            Viewholder viewHolder;
            if (currentView == null) {
                currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.permission_cus, parent, false);
                viewHolder = new Viewholder(currentView);
                currentView.setTag(viewHolder);
            } else {
                viewHolder = (Viewholder) currentView.getTag();
            }
            viewHolder.bind(Activitylist.get(position));
            return currentView;
        }

        public class Viewholder {
            private final TextView title;
            private final TextView info;
            private final TextView state;

            public Viewholder(View v) {
                title = v.findViewById(R.id.pername);
                info = v.findViewById(R.id.perinfo);
                state = v.findViewById(R.id.perstate);
            }

            public void bind(ActivityInfo act) {
                String s = act.name;
                title.setText(getcompname(s));
                info.setText(s);
                ComponentName cp = new ComponentName(act.applicationInfo.packageName, s);
                boolean b = ap.getApplicationComponentState(cp);
                if (b) {
                    state.setText(R.string.ena);
                    state.setTextColor(c.getColor(R.color.green));
                } else {
                    state.setText(R.string.dis);
                    state.setTextColor(c.getColor(R.color.no_red));
                }
            }

        }
    }
    private void disableact(ActivityInfo a) {
        try {
            ComponentName cp = new ComponentName(a.applicationInfo.packageName, a.name);
            boolean b = ap.getApplicationComponentState(cp);
            if (ap.setApplicationComponentState(cp, !b)) {
                if (b) {
                    emmtoast(a.name + " Disabled Successfully", c);
                } else {
                    emmtoast(a.name + " Enabled Successfully", c);
                }
            } else {
                emmtoast("Failed to disable", c);
            }
        }catch (Exception e){
            emmtoast("Failed to disable", c);
        }
    }



}
