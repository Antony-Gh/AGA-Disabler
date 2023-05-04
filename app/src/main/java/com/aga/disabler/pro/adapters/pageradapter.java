package com.aga.disabler.pro.adapters;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.aga.disabler.pro.fragment.ActionFragment;
import com.aga.disabler.pro.fragment.ActivitiesFragment;
import com.aga.disabler.pro.fragment.DashboardFragment;
import com.aga.disabler.pro.fragment.FragmentHolder;
import com.aga.disabler.pro.fragment.InfoFragment;
import com.aga.disabler.pro.fragment.ManifestFragment;
import com.aga.disabler.pro.fragment.PermissionsFragment;
import com.aga.disabler.pro.fragment.ProviderFragment;
import com.aga.disabler.pro.fragment.ReceiversFragment;
import com.aga.disabler.pro.fragment.ServicesFragment;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class pageradapter extends FragmentPagerAdapter {
    private final Context context;
    private HashMap<Integer, FragmentHolder> tabs;
    private final Activity act;

    public pageradapter(Context context, FragmentManager fm, String pkg, Activity a) {
        super(fm);
        this.context = context;
        this.act = a;
        initTabs(pkg);
    }

    @Override
    public @NotNull Fragment getItem(int position) {
        return Objects.requireNonNull(tabs.get(position));
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Objects.requireNonNull(tabs.get(position)).getTitle();
    }

    private void initTabs(String p) {
        tabs = new HashMap<>();
        tabs.put(0, DashboardFragment.getInstance(context, p, act));
        tabs.put(1, InfoFragment.getInstance(context, p));
        tabs.put(2, ActionFragment.getInstance(context, p));
        tabs.put(3, ActivitiesFragment.getInstance(context, p));
        tabs.put(4, ReceiversFragment.getInstance(context, p));
        tabs.put(5, ServicesFragment.getInstance(context, p));
        tabs.put(6, ProviderFragment.getInstance(context, p));
        tabs.put(7, PermissionsFragment.getInstance(context, p));
        //tabs.put(8, ManifestFragment.getInstance(context, p));
    }
}
