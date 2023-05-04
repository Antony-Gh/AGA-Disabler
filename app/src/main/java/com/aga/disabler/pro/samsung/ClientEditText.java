package com.aga.disabler.pro.samsung;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatEditText;
import com.aga.disabler.pro.R;

public class ClientEditText extends AppCompatEditText {
    /* access modifiers changed from: private */
    public Drawable i;
    public boolean m = false;
    /* access modifiers changed from: private */
    public d o;

    class a implements View.OnTouchListener {
        a() {
        }

        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (ClientEditText.this.getCompoundDrawables()[2] != null && motionEvent.getAction() == 1 && motionEvent.getX() > ((float) ((ClientEditText.this.getWidth() - ClientEditText.this.getPaddingRight()) - ClientEditText.this.i.getIntrinsicWidth()))) {
                ClientEditText.this.setText("");
                ClientEditText.this.d(0);
            }
            return false;
        }
    }

    class b implements TextWatcher {
        b() {
        }

        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            ClientEditText clientEditText = ClientEditText.this;
            clientEditText.d(clientEditText.length());
        }
    }

    class c implements View.OnFocusChangeListener {
        c() {
        }

        public void onFocusChange(View view, boolean z) {
            if (ClientEditText.this.o != null) {
                ClientEditText.this.o.onFocusChanged(z);
            }
            ClientEditText clientEditText = ClientEditText.this;
            clientEditText.d(!z ? 0 : clientEditText.length());
        }
    }

    public interface d {
        void onFocusChanged(boolean z);
    }

    public ClientEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        e();
    }

    public ClientEditText(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        e();
    }

    /* access modifiers changed from: private */
    public void d(int i2) {
        Drawable drawable;
        Drawable drawable2;
        Drawable drawable3;
        Drawable drawable4;
        if (i2 != 0) {
            drawable = getCompoundDrawables()[0];
            drawable3 = getCompoundDrawables()[1];
            drawable2 = this.i;
            drawable4 = getCompoundDrawables()[3];
        } else {
            drawable = getCompoundDrawables()[0];
            drawable3 = getCompoundDrawables()[1];
            drawable2 = null;
            drawable4 = getCompoundDrawables()[3];
        }
        setCompoundDrawables(drawable, drawable3, drawable2, drawable4);
    }

    private void e() {
        setPrivateImeOptions("defaultInputmode=english");
        h(false);
        Drawable drawable = getResources().getDrawable(R.drawable.btn_text_delete);
        this.i = drawable;
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.i.getIntrinsicHeight());
        d(0);
        setOnTouchListener(new a());
        addTextChangedListener(new b());
        setOnFocusChangeListener(new c());
    }

    public void f(boolean z) {
        setPrivateImeOptions("disableToolbar=" + z + ";disableCMKey=" + z + ";");
    }

    public void g(d dVar) {
        this.o = dVar;
    }

    public void h(boolean z) {
        Resources resources;
        int i2;
        this.m = z;
        if (z) {
            resources = getContext().getResources();
            i2 = R.drawable.input_error;
        } else {
            resources = getContext().getResources();
            i2 = R.drawable.client_edittext_bg;
        }
        setBackground(resources.getDrawable(i2));
    }

    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        super.setText(charSequence, bufferType);
        if (!hasFocus()) {
            d(0);
        }
    }
}
