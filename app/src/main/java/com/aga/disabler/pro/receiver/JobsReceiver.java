package com.aga.disabler.pro.receiver;

import static com.aga.disabler.pro.receiver.JobsService.startAction;
import static com.aga.disabler.pro.tools.Helper.emmtoast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class JobsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String act = intent.getAction();
       Intent serint = new Intent(context, JobsService.class);
       switch (act){
           case Intent.ACTION_BOOT_COMPLETED:
               emmtoast("Boot Compelete", context);
               context.startService(serint);
           break;
           case JobsService.startAction:
               emmtoast("MainActivity Start", context);
               context.startService(serint);
           break;
           case JobsService.destroyAction:
               emmtoast("JobService Destroyed", context);
               context.startService(serint);
           break;
           case Intent.ACTION_USER_PRESENT:
               emmtoast("User Present", context);
               context.startService(serint);
           break;
           case Intent.ACTION_USER_UNLOCKED:
               emmtoast("User Unlocked", context);
               context.startService(serint);
               break;
       }
    }
}