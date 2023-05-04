package com.aga.disabler.pro.receiver;

import static com.aga.disabler.pro.fragment.AppGameModeList.App_Disable;
import static com.aga.disabler.pro.fragment.AppGameModeList.App_Enable;
import static com.aga.disabler.pro.tools.Helper.emmtoast;
import static com.aga.disabler.pro.tools.Helper.getgamemode;
import static com.aga.disabler.pro.tools.Helper.loadGameList;
import static com.aga.disabler.pro.tools.Helper.setgamemode;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.aga.disabler.pro.fragment.AppGameModeList;
import com.aga.disabler.pro.tools.FileUtil;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class JobsService extends AccessibilityService {
    public static final String startAction = "com.aga.disabler.pro.START";
    public static final String destroyAction = "com.aga.disabler.pro.DESTROYED";
    private boolean fir = false;


    public JobsService() {
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        devicepolicy dp = new devicepolicy(this);
        String path = FileUtil.getPackageDataDir(this).concat("/GameModeList.txt");
        List<AppGameModeList> list;
        if(action == KeyEvent.ACTION_UP){
            if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
                if(fir){
                    if(new File(path).exists()){
                        list = loadGameList(path);
                        if(getgamemode(this)){
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
                            emmtoast("GameMode OFF", this);
                        }else{
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
                            emmtoast("GameMode ON", this);
                        }
                    }else{
                        emmtoast("Please Setup Game Mode Settings", this);
                    }

                }
                fir = true;
                Timer tt = new Timer();TimerTask t1 = new TimerTask() {@Override public void run() {fir = false;}};tt.schedule(t1, (300));
            }
        }
        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {

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
        super.onServiceConnected();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}