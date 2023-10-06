package com.aga.disabler.pro.receiver;

import static com.aga.disabler.pro.fragment.AppGameModeList.App_Disable;
import static com.aga.disabler.pro.fragment.AppGameModeList.App_Enable;
import static com.aga.disabler.pro.license.SharedPreferenceManager.setgamemode;
import static com.aga.disabler.pro.tools.Helper.emmtoast;
import static com.aga.disabler.pro.tools.Helper.loadGameList;

import android.accessibilityservice.AccessibilityService;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.fragment.AppGameModeList;
import com.aga.disabler.pro.license.ExecutorServiceII;
import com.aga.disabler.pro.tools.FileUtil;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class JobsService extends AccessibilityService implements ExecutorServiceII.Tasks {
    public static final String startAction = "com.aga.disabler.pro.START";
    public static final String destroyAction = "com.aga.disabler.pro.DESTROYED";
    private boolean fir = false;

    public devicepolicy dp;

    public String path;

    private boolean b = true;

    private ExecutorServiceII exe;

    public List<AppGameModeList> list;

    private Context cc;

    private ProgressDialog progress;


    public JobsService() {
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        exe = new ExecutorServiceII.ExecutorBuilder().setTasks(this).build();
        b = !getgamemode(this);
        if(action == KeyEvent.ACTION_UP){
            if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
                if(fir){
                    if(new File(path).exists()){
                        dialog();
                    }else{
                        emmtoast("Please Setup Game Mode Settings", this);
                    }
                }
                fir = true;
                Timer tt = new Timer();TimerTask t1 = new TimerTask() {@Override public void run() {fir = false;}};tt.schedule(t1, (500));
            }
        }
        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {
        emmtoast("Service Destroyed", this);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        emmtoast("Service Destroyed", this);
        Intent ii = new Intent(destroyAction);
        sendBroadcast(ii);
        super.onDestroy();
    }

    @Override
    protected void onServiceConnected() {
        emmtoast("Service Started", this);
        cc = this.getApplicationContext();
        path = FileUtil.getPackageDataDir(this).concat("/GameModeList.txt");
        dp = new devicepolicy(this);
        super.onServiceConnected();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }




    private void deactivatemode() {
        setgamemode(this, false);
        assert list != null;
        for(AppGameModeList a : list){
            String p = a.packages;
            switch (a.type){
                case App_Enable:
                    dp.disableappswithouttoast(this,p);
                    break;
                case App_Disable:
                    dp.enableappswithouttoast(this, p);
                    break;
                default:
                    break;
            }
        }
    }

    private void activatemode() {
        setgamemode(this, true);
        assert list != null;
        for(AppGameModeList a : list){
            String p = a.packages;
            switch (a.type){
                case App_Enable:
                    dp.enableappswithouttoast(this, p);
                    break;
                case App_Disable:
                    dp.disableappswithouttoast(this,p);
                    break;
                default:
                    break;
            }
        }

    }

    private void prog() {
        progress = new ProgressDialog(this, R.style.Theme_AGADisabler_Dialog);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setIndeterminate(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("Loading...");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            progress.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        progress.show();
    }

    private void dialog() {
        String msg;
        if(b){
            msg = getString(R.string.activate_gamemode_msg);
        }else{
            msg = getString(R.string.deactivate_gamemode_msg);
        }
        AlertDialog aa = new AlertDialog.Builder(cc, R.style.Theme_AGADisabler_Dialog)
                .setCancelable(false)
                .setMessage(msg)
                .setTitle(getString(R.string.activate_gamemode_title))
                .setIcon(R.drawable.logo_rounded)
                .setPositiveButton("Ok", (dialog, which) -> exe.execute())
                .setNegativeButton("Cancel", (dialog, which) -> {})
                .create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            aa.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        aa.show();
    }


    @Override
    public void doinbackground() {
        list = loadGameList(path);
        if(b)
            activatemode();
        else
            deactivatemode();
    }

    @Override
    public void onpreexecute() {
        prog();
    }

    @Override
    public void onpostexecute() {
        try {
            progress.dismiss();
            progress.cancel();
        }catch (Exception ignored){}
        if(b){
            emmtoast("GameMode ON", this);
        }else{
            emmtoast("GameMode OFF", this);
        }
    }
}