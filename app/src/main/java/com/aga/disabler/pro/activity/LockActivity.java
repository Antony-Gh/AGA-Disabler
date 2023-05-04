package com.aga.disabler.pro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.aga.disabler.pro.R;
import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.aga.disabler.pro.tools.Helper.emmtoast;
import static com.aga.disabler.pro.tools.Helper.*;
import static com.andrognito.patternlockview.PatternLockView.PatternViewMode.*;

public class LockActivity extends AppCompatActivity {
    private PatternLockView lockv;
    private String patt;
    private Context c;
    private String act;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        initlogic();
    }

    private void initlogic() {
        act = getIntent().getStringExtra("action");
        c = this.getApplicationContext();
        patt = getpattern(c);
        final Context con = getApplicationContext();
        TextView txt = findViewById(R.id.txt);
        Button bt = findViewById(R.id.launch);

        if (act.equals("save_pattern")) {
            txt.setText(R.string.draw_patt);
            if(patt != null && !patt.equals("")) bt.setVisibility(View.VISIBLE);
        }else{
            txt.setText(R.string.draw_pattern_to_unlock);
            bt.setVisibility(View.GONE);
        }

        PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
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
                        Intent inte = new Intent();
                        inte.setClass(con, AdminActivity.class);
                        inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(inte);
                        finish();
                    } else {
                        setview(WRONG);
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

        lockv = findViewById(R.id.pattern_lock_view);
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