package com.aga.disabler.pro.fragment;

import static com.aga.disabler.pro.tools.Helper.emmtoast;
import static com.aga.disabler.pro.tools.Helper.getAttributeColor;
import static com.aga.disabler.pro.tools.Helper.getarchiveinfo;
import static com.aga.disabler.pro.tools.PermInfo.getpermissioninfo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
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
import com.samsung.android.knox.AppIdentity;
import com.samsung.android.knox.EnterpriseDeviceManager;
import com.samsung.android.knox.application.ApplicationPolicy;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PermissionsFragment extends FragmentHolder{
    private PackageManager pkgmg;
    private ApplicationPolicy ap;

    public static PermissionsFragment getInstance(Context context, String pkgname) {
        PermissionsFragment fragment = new PermissionsFragment();
        fragment.setContext(context);
        fragment.setTitle("Permissions");
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
        pkgmg = c.getPackageManager();
        ap = EnterpriseDeviceManager.getInstance(c).getApplicationPolicy();
        LinearLayout linear2 = view.findViewById(R.id.linear2);
        ListView list = view.findViewById(R.id.listviewall);
        List<PermissionInfo> activityInfos = peri();
        if(activityInfos == null || activityInfos.isEmpty()){
            TextView t = view.findViewById(R.id.textview1);
            t.setText(t.getText().toString().replace("%d", "Permissions"));
            list.setVisibility(View.GONE);
            linear2.setVisibility(View.VISIBLE);
        }else{
            ActAdapter actAdapter = new ActAdapter(activityInfos);
            list.setOnItemClickListener((parent, view, position, id) -> {
                disableact(activityInfos.get(position));
                ((ActAdapter) list.getAdapter()).notifyDataSetChanged();
            });
            list.setAdapter(actAdapter);
        }

    }

    private List<PermissionInfo> peri() {
        List<PermissionInfo> list = new ArrayList<>();
        String[] ap = Objects.requireNonNull(getarchiveinfo(c, pkg, PackageManager.GET_PERMISSIONS)).requestedPermissions;
        if(ap == null || ap.length <= 0){
            return list;
        }
        for (String st : ap) {
            try {
                PermissionInfo p = pkgmg.getPermissionInfo(st, PackageManager.GET_META_DATA);
                list.add(p);
            }catch (Exception ignored){}
        }
        return list;
    }

    public class ActAdapter extends BaseAdapter {
        private final List<PermissionInfo> Activitylist;

        public ActAdapter(List<PermissionInfo> act) {
            Activitylist = act;
        }

        @Override
        public int getCount() {
            return Activitylist.size();
        }

        @Override
        public PermissionInfo getItem(int position) {
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

            public void bind(PermissionInfo act) {
                title.setText(act.name);
                info.setText(getpermissioninfo(act.name, c));
                if(getperstate(act.name, pkg)){
                    state.setText(R.string.ena);
                    state.setTextColor(getAttributeColor(c, R.attr.permission_text_color_true));
                }else{
                    state.setText(R.string.dis);
                    state.setTextColor(getAttributeColor(c, R.attr.permission_text_color_false));
                }
            }

        }
    }

    private void disableact(PermissionInfo a) {
        List<String> l = new ArrayList<>();
        l.add(a.name);
        try {
            boolean b = getperstate(a.name, pkg);
            if(b){
              if(ap.applyRuntimePermissions(new AppIdentity(pkg, null), l, ApplicationPolicy.PERMISSION_POLICY_STATE_DENY) == ApplicationPolicy.ERROR_NONE){
                  emmtoast("Permission Denied Successfully", c);
              }else{
                  emmtoast("Failed to disable", c);
              }
            }else{
                if(ap.applyRuntimePermissions(new AppIdentity(pkg, null), l, ApplicationPolicy.PERMISSION_POLICY_STATE_GRANT) == ApplicationPolicy.ERROR_NONE){
                    emmtoast("Permission Granted Successfully", c);
                }else{
                    emmtoast("Failed to enable", c);
                }
            }
        }catch (Exception e){
            emmtoast("Failed to disable", c);
        }
    }

    public boolean getperstate(String per, String pkg) {
        return pkgmg.checkPermission(per, pkg) == PackageManager.PERMISSION_GRANTED;
    }

}
