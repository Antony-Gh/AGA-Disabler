package com.aga.disabler.pro.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.license.ExecutorServiceII;
import com.aga.disabler.pro.tools.ManifestResolver;

import org.jetbrains.annotations.NotNull;

import br.tiagohm.codeview.CodeView;
import br.tiagohm.codeview.Language;
import br.tiagohm.codeview.Theme;

public class ManifestFragment extends FragmentHolder {

    public static ManifestFragment getInstance(Context context, String pkgname) {
        ManifestFragment fragment = new ManifestFragment();
        fragment.setContext(context);
        fragment.setTitle("Manifest");
        fragment.setpkg(pkgname);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.manifest_fragment, container, false);
        init();
        return view;
    }

    public void init() {
        CodeView code = view.findViewById(R.id.codeview1);
        final ManifestResolver mr = new ManifestResolver();
        String mani = mr.resolvemanifestfromapk(pkg, c);
        code.setTheme(Theme.ATOM_ONE_DARK)
                .setCode(mani)
                .setLanguage(Language.XML)
                .setWrapLine(true)
                .setFontSize(16)
                .setZoomEnabled(true)
                .setShowLineNumber(true)
                .setStartLineNumber(1)
                .apply();
    }


}
