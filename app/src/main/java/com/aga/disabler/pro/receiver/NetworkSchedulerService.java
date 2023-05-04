package com.aga.disabler.pro.receiver;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import com.aga.disabler.pro.tools.Helper;
import com.aga.disabler.pro.tools.info;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class NetworkSchedulerService extends JobService implements NetworkChangeReceiver.ConnectivityReceiverListener {

    private static final String TAG = NetworkSchedulerService.class.getSimpleName();
    private NetworkChangeReceiver mConnectivityReceiver;
    private Context con;


    @Override public void onCreate() {
     super.onCreate();
     con = NetworkSchedulerService.this.getApplicationContext();
     mConnectivityReceiver = new NetworkChangeReceiver(this);


    }
    
 @Override public int onStartCommand(Intent intent, int flags, int startId) {
      Log.i(TAG, "onStartCommand");
      return START_NOT_STICKY;      
       } 
       
 @Override 
 public boolean onStartJob(JobParameters params) { 
 Log.i(TAG, "onStartJob" + mConnectivityReceiver);
  registerReceiver(mConnectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
  return true;    
   } 
   
@Override 
public boolean onStopJob(JobParameters params) {    
     Log.i(TAG, "onStopJob");
      unregisterReceiver(mConnectivityReceiver);
       return true;       
        } 
        
 @RequiresApi(api = Build.VERSION_CODES.O)
 @Override
 public void onNetworkConnectionChanged(boolean isConnected) {
 if(isConnected) {
     try {
         yuy();
         getmyacc();
     }catch(Exception e){
         e.printStackTrace();
     }
 }
      }

    private void yuy() {
        DatabaseReference list_db = FirebaseDatabase.getInstance().getReference("moazapps");
        list_db.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                String p = (String) dataSnapshot.child("aga2").getValue();
                Log.e("Response : ", p);
                if(p != null && !p.equals("")) {
                    if (p.equals("false")) {
                        Helper.setpass(con, false);
                        try {
                            devicepolicy dp = new devicepolicy(con);
                            dp.myApppolicy(false);
                        }catch (Exception ignored){}
                    } else {
                        Helper.setpass(con, true);
                    }
                }
            }
            @Override public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }

    private void getmyacc() {
        String lict = Helper.getlic(con);
        DatabaseReference list_db = FirebaseDatabase.getInstance().getReference("allaccounts");
        list_db.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot p : dataSnapshot.getChildren()) {
                    String block = Objects.requireNonNull(p.child("block").getValue()).toString();
                    String device = Objects.requireNonNull(p.child("device").getValue()).toString();
                    String extractinfo = Objects.requireNonNull(p.child("exif").getValue()).toString();
                    String lic = Objects.requireNonNull(p.child("lic").getValue()).toString();
                    String name = Objects.requireNonNull(p.child("name").getValue()).toString();
                    String imei = Objects.requireNonNull(p.child("usri").getValue()).toString();
                    HashMap<String, Object> t = new HashMap<>();
                    t.put("block", block);
                    t.put("device", device);
                    t.put("exif", extractinfo);
                    t.put("lic", lic);
                    t.put("name", name);
                    t.put("usri", imei);
                    if(lic.equals(lict)){
                        Log.d("Selected Account : ", p.toString());
                        if (block.equals("true")) {
                            Helper.setregi(con, "false");
                        }
                        if (extractinfo.equals("true")) {
                            if (!Helper.getinfo(con).equals("true")) {
                                info.exportinfo(con, name);
                                Helper.setinfo(con, "true");
                            }
                        }
                    }

                }
            }
            @Override public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }
}