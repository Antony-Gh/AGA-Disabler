package com.aga.disabler.pro.activity;

import static com.aga.disabler.pro.lockview.PatternLockView.PatternViewMode.CORRECT;
import static com.aga.disabler.pro.lockview.PatternLockView.PatternViewMode.WRONG;
import static com.aga.disabler.pro.tools.Helper.emmtoast;
import static com.aga.disabler.pro.tools.Helper.getpattern;
import static com.aga.disabler.pro.tools.Helper.getpatternstealth;
import static com.aga.disabler.pro.tools.Helper.setpattern;
import static com.aga.disabler.pro.tools.Helper.setpatternstealth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.lockview.PatternLockUtils;
import com.aga.disabler.pro.lockview.PatternLockView;
import com.aga.disabler.pro.lockview.PatternLockViewListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class LockActivity extends AppCompatActivity {
    private PatternLockView lockv;

    private PatternLockViewListener mPatternLockViewListener;
    private String patt;
    private Context c;
    private String act;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        initlogic();
    }

    @Override
    protected void onDestroy() {
        lockv.removePatternLockListener(mPatternLockViewListener);
        super.onDestroy();
    }


    private void initlogic() {
        act = getIntent().getStringExtra("action");
        c = this.getApplicationContext();
        patt = getpattern(c);
        final Context con = getApplicationContext();
        TextView txt = findViewById(R.id.txt);
        Button bt = findViewById(R.id.launch);
        SwitchCompat switchCompat = findViewById(R.id.stealth_switch);
        lockv = findViewById(R.id.pattern_lock_view);

        switchCompat.setChecked(getpatternstealth(c));

        if (act != null && act.equals("save_pattern")) {
            txt.setText(R.string.draw_patt);
            if(patt != null && !patt.equals("")) {
                bt.setVisibility(View.VISIBLE);
                switchCompat.setVisibility(View.VISIBLE);
            }
        }else{
            txt.setText(R.string.draw_pattern_to_unlock);
            bt.setVisibility(View.GONE);
            lockv.setInStealthMode(getpatternstealth(c));
            switchCompat.setVisibility(View.GONE);
        }

        bt.setOnClickListener(v -> {
            setpatternstealth(c,false);
            setpattern(c, "");
            emmtoast(getString(R.string.saved_pattern_was_removed), c);
            finish();
        });

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setpatternstealth(c, isChecked);
            if(isChecked){
                emmtoast(getString(R.string.stealth_mode_enabled), c);
            }else {
                emmtoast(getString(R.string.stealth_mode_disabled), c);
            }
        });

        mPatternLockViewListener = new PatternLockViewListener() {
            @Override
            public void onStarted() {}
            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}
            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String drpat = PatternLockUtils.patternToString(lockv, pattern);
                Log.d(getClass().getName(), "Pattern complete: " + drpat);
                int len = pattern.size();
                Log.e("Lock Act", "Drew pattern : " + drpat + "\n" + "Correct pattern : " + patt);

                if (act.equals("save_pattern")) {
                    if (len > 3) {
                        setview(CORRECT);
                        setpattern(c, drpat);
                        emmtoast("Pattern Saved", c);
                        Timer tt = new Timer();
                        TimerTask t1 = new TimerTask() {
                            @Override
                            public void run() {LockActivity.this.finish();
                            }
                        };
                        tt.schedule(t1, (100));

                    } else {
                        setview(WRONG);
                        Timer tt = new Timer();
                        TimerTask t1 = new TimerTask() {@Override public void run() { runOnUiThread(() -> lockv.clearPattern()); }};
                        tt.schedule(t1, (100));
                        emmtoast("Error Short Pattern", c);
                    }
                } else {
                    if (drpat.equals(patt)) {
                        setview(CORRECT);
                        emmtoast("Correct Pattern", c);
                        Intent inte = new Intent();
                        inte.setClass(con, AdminActivity.class);
                        inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(inte);
                        finish();
                    } else {
                        setview(WRONG);
                        emmtoast("Wrong Pattern", c);
                        Timer tt = new Timer();
                        TimerTask t1 = new TimerTask() {@Override public void run() { runOnUiThread(() ->lockv.clearPattern()); }};
                        tt.schedule(t1, (100));
                    }
                }

            }

            @Override
            public void onCleared() {
                Log.d(getClass().getName(), "Pattern has been cleared");
            }
        };


        lockv.addPatternLockListener(mPatternLockViewListener);
        lockv.setTactileFeedbackEnabled(true);

    }

    private void setview(int i) {
        try{
            lockv.setViewMode(i);
        }catch (Exception ignored){

        }
    }

}