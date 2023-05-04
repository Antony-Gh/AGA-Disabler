package com.aga.disabler.pro.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.aga.disabler.pro.tools.Helper.isOnline;

public class NetworkChangeReceiver extends BroadcastReceiver {
    
    private NetworkChangeReceiver.ConnectivityReceiverListener mConnectivityReceiverListener;
    
     public NetworkChangeReceiver(ConnectivityReceiverListener listener) {
          mConnectivityReceiverListener = listener; 
          }
    
    @Override
    public void onReceive(final Context context, final Intent intent) {        
        final boolean o = isOnline(context);
        mConnectivityReceiverListener.onNetworkConnectionChanged(o);       
    }
    
 public interface ConnectivityReceiverListener {
     
      void onNetworkConnectionChanged(boolean isConnected); 
      
 }   
       
}