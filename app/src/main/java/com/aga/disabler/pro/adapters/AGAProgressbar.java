package com.aga.disabler.pro.adapters;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

import com.aga.disabler.pro.R;

import org.jetbrains.annotations.NotNull;

public class AGAProgressbar extends AppCompatImageView {

    public AGAProgressbar(@NonNull @NotNull Context context) {
        super(context);
        init();
    }

    public AGAProgressbar(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AGAProgressbar(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_load, null));
        setScaleType(ScaleType.FIT_XY);
        ObjectAnimator o = new ObjectAnimator();
        o.setTarget(this);
        o.setPropertyName("rotation");
        o.setFloatValues((float)(0.0d), (float)(360.0d));
        o.setDuration(650);
        o.setAutoCancel(false);
        o.setInterpolator(new LinearInterpolator());
        o.setRepeatCount(Animation.INFINITE);
        o.setRepeatMode(ValueAnimator.RESTART);
        o.start();
    }

    public void setcolour(int col) {
        setColorFilter(R.color.colorAccent, PorterDuff.Mode.SRC_IN);
    }

}
