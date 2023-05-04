package com.aga.disabler.pro.tools;

import android.Manifest.permission;
import android.content.Context;

import com.aga.disabler.pro.R;

import java.util.ArrayList;

public class PermInfo {
    
    //CAMERA
    
    public static final String camera = permission.CAMERA;
    public static final String camerainfo = "Required to be able to access the camera device.";
    
    
    //CONTACTS
    
    public static final String get_accounts = permission.GET_ACCOUNTS;
    public static final String get_accountsinfo = "Allows the app to get list of accounts known by your phone . tThis may include any accounts created by apps you have installed";
    
    public static final String manage_accounts = "android.permission.MANAGE_ACCOUNTS";
    public static final String manage_accountsinfo = "Allows the app to perform operations like adding and removing accounts, and deleting their password.";

    public static final String read_contacts = permission.READ_CONTACTS;
    public static final String read_contactsinfo = "Allows the app to read about your contacts stored on your phone , including the frequency with which yo've called , emailed , or communicated in other ways with specific individuals . This permission allows apps to save your contacts data , and malicious apps may share contacts data without your knowledge";
    
    public static final String write_contacts = permission.WRITE_CONTACTS;
    public static final String write_contactsinfo = "Allows the app to modify the data about your contacts stored your phone , including the frequency with which yo've called , emailed , or communicated in other ways with specific contacts . This permission allows apps to delete contacts data";
        
    
    //LOCATION
    
    public static final String coarse_location = permission.ACCESS_COARSE_LOCATION;
    public static final String coarse_locationinfo = "This app can get your location based on network sources such as cell towers and Wi-Fi networks . These location services must be turned on and availabel on your phone for the app to be able to use them";
    
    public static final String fine_location = permission.ACCESS_FINE_LOCATION;
    public static final String fine_locationinfo = "This app can get your location based on GPS or network location sources such as cell towers and Wi-Fi networks . These location services must be turned on and availabel on your phone for the app to be able to use them . This may increase battery consumption";
    
    public static final String back_loc = "android.permission.ACCESS_BACKGROUND_LOCATION";
    public static final String back_locinfo = "Allows an app to access location in the background.";
    
    //MICROPHONE
    
    public static final String record_audio = permission.RECORD_AUDIO;
    public static final String record_audioinfo = "This app can record audio using the microphone at any time";
    
    //PHONE
    
    public static final String phone_state = permission.READ_PHONE_STATE;
    public static final String phone_stateinfo = "Allow the app to access the phone features of the device . This permission allows the app to determine the phone number and device IDs , whether a call is active , and the remote number connected by a call";
    
    public static final String read_call_logs = permission.READ_CALL_LOG;
    public static final String read_call_logsinfo = "This app can read your call history";
       
    public static final String manage_own_logs = permission.MANAGE_OWN_CALLS;
    public static final String manage_own_logsinfo = "Allows the app to call phone numbers without your intervention . This may result in unexpected charges or calls . Note that this doesn't allow the app to call emergencey numbers . Malicious apps may cost you money by making calls without your confirmation";   
        
    public static final String modify_phone_state = permission.MODIFY_PHONE_STATE;
    public static final String modify_phone_stateinfo = "Allows the app to control the phone features of the device. An app with this permission can switch networks, turn the phone radio on and off and the like without ever notifying you.";    
        
    //SMS
    
    public static final String read_sms = permission.READ_SMS;
    public static final String read_smsinfo = "Allows an application to read SMS messages";

    public static final String send_sms = permission.SEND_SMS;
    public static final String send_smsinfo = "Allows an application to send SMS messages without user confirmation";
    
    public static final String receive_sms = permission.RECEIVE_SMS;
    public static final String receive_smsinfo = "Allow the app to receive and process SMS messages . This means the app could monitor or delete messages sent to your device without showing them to you";

    //STORAGE
    
    public static final String read_storage = permission.READ_EXTERNAL_STORAGE;
    public static final String read_storageinfo = "Allow an app to get information about SD card and read it's contents .";

    public static final String write_storage = permission.WRITE_EXTERNAL_STORAGE;
    public static final String write_storageinfo = "Allows an application to modify , delete or write SD card contents";
    
    public static final String access_internal_storage = "android.permission.WRITE_INTERNAL_STORAGE";
    public static final String access_internal_storageinfo = "Required for apps targeting Build.VERSION_CODES.Q that want to use notification full screen intents.";
    
    //BLUETOOTH
    
    public static final String bluetooth = permission.BLUETOOTH;
    public static final String bluetoothinfo = "Allows the app to view configuration of Bluetooth on your phone , and to make accept connections with paired devices";
    
    public static final String bluetoothadmin = permission.BLUETOOTH_ADMIN;
    public static final String bluetoothadmininfo = "Allows applications to configure the local Bluetooth phone , and to discover and pair with remote devices";
    
    public static final String bluetooth_privileged = "android.permission.BLUETOOTH_PRIVILEGED";
    public static final String bluetooth_privilegedinfo = "allow Bluetooth pairing by Application and Allows the app to pair with remote devices without user interaction.";
    
    //NETWORK
    
    public static final String internet = permission.INTERNET;
    public static final String internetinfo = "Allows the app to create network sockets and use custom network protocols . The browser and other apps provide means to send data to the internet , So this permission is not required to send data to the internet";
    
    public static final String network_state = permission.ACCESS_NETWORK_STATE;
    public static final String network_stateinfo = "Allows the app to view information about networks connections such as which networks exist and are connected or not";

    public static final String change_network_state = permission.CHANGE_NETWORK_STATE;
    public static final String change_network_stateinfo = "Allows the app to change the state of network connectivity.";

    public static final String network_settings = "android.permission.NETWORK_SETTINGS";
    public static final String network_settingsinfo = "Allos the app to access network settings without user confirmation";

    //WI-FI
    
    public static final String wifi_state = permission.ACCESS_WIFI_STATE;
    public static final String wifi_stateinfo = "Allows the app to view information about Wi-Fi networking , such as whether Wi-Fi is enabled and the name of connected Wi-Fi devices";
    
    public static final String change_wifi_state = permission.CHANGE_WIFI_STATE;
    public static final String change_wifi_stateinfo = "Allows the app to connect and disconnect from Wi-Fi access points and to make changes to device configuration for Wi-Fi networks";

    //Manage Other Apps
    
    public static final String kill_apps = permission.KILL_BACKGROUND_PROCESSES;
    public static final String kill_appsinfo = "Allows the app to end background processes of other apps . This may cause other apps to stop running";
    
    public static final String pkg_usage_stats = permission.PACKAGE_USAGE_STATS;
    public static final String pkg_usage_statsinfo = "Allows an application to collect component usage statistics Declaring the permission implies intention to use the API and the user of the device can grant permission through the Settings application.";
    
    public static final String request_delete_packages = permission.REQUEST_DELETE_PACKAGES;
    public static final String request_delete_packagesinfo = "Allows an app to request deletion of other apps .";
        
    public static final String request_install_packages = permission.REQUEST_INSTALL_PACKAGES;
    public static final String request_install_packagesinfo = "Allows an app to request installing any package to your device";
        
    public static final String delete_packages = permission.DELETE_PACKAGES;
    public static final String delete_packagesinfo = "Allows an application to deleting packages.";
        
    public static final String install_packages = permission.INSTALL_PACKAGES;
    public static final String install_packagesinfo = "Allows an application to installing packages.";

    //NOTIFICATIONS
    
    public static final String access_noti_policy = permission.ACCESS_NOTIFICATION_POLICY;
    public static final String access_noti_policyinfo = "Allows the app to read and write DO NOT DISTURB CONFIGURATION";

    public static final String access_noti = "android.permission.ACCESS_NOTIFICATIONS";
    public static final String access_notiinfo = "Access ";
    
    public static final String download_notnoti = "android.permission.DOWNLOAD_WITHOUT_NOTIFICATION";
    public static final String download_notnotiinfo = "Allows an application to download files without user verfiy.";
    
    //MANAGE SYSTEM
    
    public static final String receive_boot = permission.RECEIVE_BOOT_COMPLETED;
    public static final String receive_bootinfo = "Allows an application to receive the Intent.ACTION_BOOT_COMPLETED that is broadcast after the system finishes booting.";
                    
    public static final String system_alert_window = permission.SYSTEM_ALERT_WINDOW;
    public static final String system_alert_windowinfo = "Allows an app to create windows using the type WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, shown on top of all other apps.";

    public static final String write_secure_settings = permission.WRITE_SECURE_SETTINGS;
    public static final String write_secure_settingsinfo = "Allows an application to read or write the secure system settings.";
        
    public static final String write_settings = permission.WRITE_SETTINGS;
    public static final String write_settingsinfo = "Allows an application to read or write the system settings.";
    
    public static final String modify_audio_settings = permission.MODIFY_AUDIO_SETTINGS;
    public static final String modify_audio_settingsinfo = "Allows an application to modify global audio settings.";

    //LAUNCHER
    
    public static final String read_launcher = "com.android.launcher.permission.READ_SETTINGS";
    public static final String read_launcherinfo = "Allows the app to read the settings and shortcuts in Home.";   
    
    public static final String write_launcher = "com.android.launcher.permission.WRITE_SETTINGS";
    public static final String write_launcherinfo = "Allows the app to write the settings and shortcuts in Home.";      

    public static final String install_launcher = "com.android.launcher.permission.INSTALL_SHORTCUT";
    public static final String install_launcherinfo = "Allows an application to add Homescreen shortcuts without user intervention.";   
    
    public static final String uninstall_launcher = "com.android.launcher.permission.UNINSTALL_SHORTCUT";
    public static final String uninstall_launcherinfo = "Allows an application to remove Homescreen shortcuts without user intervention.";      

    //VENDING
    
    public static final String vending_check_license = "com.android.vending.CHECK_LICENSE";
    public static final String vending_check_licenseinfo = "Allows the app to check if the license key with the app is Matches GOOGLE PLAY STORE LICENSE KEY";
    
    public static final String vending_billing = "com.android.vending.BILLING";
    public static final String vending_billinginfo = "Allows the app to purchase items with google account from playstore with Visa, PayPal or any creditcard";
    
    //OTHER_PERMISSION
    
    
    public static final String handover = permission.ACCEPT_HANDOVER;
    public static final String handoverinfo = "Allows a calling app to continue a call which was started in another app.";
    
    public static final String checkinprop = permission.ACCESS_CHECKIN_PROPERTIES;
    public static final String checkinpropinfo = "Allows read/write access to the (properties) table in the checkin database, to change values that get uploaded.";
                       
    public static final String answer_phone = permission.ANSWER_PHONE_CALLS;
    public static final String answer_phoneinfo = "Allows the app to answer an incoming phone call.";
    
    public static final String battery_state = permission.BATTERY_STATS;
    public static final String battery_stateinfo = "Allows an application to collect battery statistics";
    
    public static final String bind_admin = permission.BIND_DEVICE_ADMIN;
    public static final String bind_admininfo = "Must be required by device administration receiver, to ensure that only the system can interact with it.";
        
    public static final String call_phone = permission.CALL_PHONE;
    public static final String call_phoneinfo = "Allows an application to initiate a phone call without going through the Dialer user interface for the user to confirm the call.";
    
               
    public static final String foreground = permission.FOREGROUND_SERVICE;
    public static final String foregroundinfo = "Allows a regular application to use Service.startForeground.";
                
    public static final String nfc = permission.NFC;
    public static final String nfcinfo = "Allows applications to perform I/O operations over NFC.";                
      
    public static final String sync_stats = permission.READ_SYNC_STATS;
    public static final String sync_statsinfo = "Allows applications to read the sync stats.";
            
    public static final String vibrate = permission.VIBRATE;
    public static final String vibrateinfo = "Allows access to the vibrator.";
    
    public static final String wake_lock = permission.WAKE_LOCK;
    public static final String wake_lockinfo = "Allows using PowerManager WakeLocks to keep processor from sleeping or screen from dimming.";
                
    public static final String write_sync_settings = permission.WRITE_SYNC_SETTINGS;
    public static final String write_sync_settingsinfo = "Allows applications to write the sync settings.";
    
    public static final String read_sync_settings = permission.READ_SYNC_SETTINGS;
    public static final String read_sync_settingsinfo = "Allows applications to read the sync settings.";
        
    public static final String account_manager = permission.ACCOUNT_MANAGER;
    public static final String account_managerinfo = "Allows applications to call into AccountAuthenticators.";
    
    public static final String use_finger = permission.USE_FINGERPRINT;
    public static final String use_fingerinfo = "This constant was deprecated in API level 28. Applications should request USE_BIOMETRIC instead , this permission for allow an app to use fingerprint but after api28 it has been deleted";
    
    public static final String use_bio = permission.USE_BIOMETRIC;
    public static final String use_bioinfo = "Allows an app to use device supported biometric modalities.";
           
    public static final String use_fullscreen_intent = "android.permission.USE_FULL_SCREEN_INTENT";
    public static final String use_fullscreen_intentinfo = "Required for apps targeting Build.VERSION_CODES.Q that want to use notification full screen intents.";

    public static final String receive_info = "com.google.android.c2dm.permission.RECEIVE";
    public static final String receive_infoinfo = "Allows apps to accept cloud to device messages sent by the app's service . Using this service will incur data usage . Malicious apps could cause excess data usage";
    
    public static final String read_google_service = "com.google.android.providers.gsf.permission.READ_GSERVICES";
    public static final String read_google_serviceinfo = "Allows apps to read Google Service Configuration data";       
       
    public static final String write_google_service = "com.google.android.providers.gsf.permission.WRITE_GSERVICES";
    public static final String write_google_serviceinfo = "Allows apps to read Google Service Configuration data";
       
    public static final String knox_enterprise_admin = "com.samsung.android.knox.permission.KNOX_ENTERPRISE_DEVICE_ADMIN";
    public static final String knox_enterprise_admininfo = "A pemission for samsung devices make this app a knox device admin";   
      
    public static final String get_tasks = "android.permission.GET_TASKS";
    public static final String get_tasksinfo = "Allows the app to retrieve information about currently and recently running tasks. This may allow the app to discover information about which applications are used on the device.";  
      
    public static final String bind_get_install = "com.google.android.finsky.permission.BIND_GET_INSTALL_REFERRER_SERVICE";
    public static final String bind_get_installinfo = "Allows other apps to tell if their installation was launched from an ad in Privacy Browser Free."; 
      
    public static final String write_media_storage = "android.permission.WRITE_MEDIA_STORAGE";
    public static final String write_media_storageinfo = "modify/delete internal media storage contents and Allows the app to modify the contents of the internal media storage.";  
      
       
       
    public static ArrayList<String> allpermission() {
        ArrayList<String> perm = new ArrayList<>();
        perm.add(handover);
        perm.add(checkinprop);
        perm.add(coarse_location);
        perm.add(fine_location);
        perm.add(network_state);
        perm.add(access_noti_policy);
        perm.add(wifi_state);
        perm.add(answer_phone);
        perm.add(battery_state);
        perm.add(bind_admin);
        perm.add(bluetooth);
        perm.add(bluetoothadmin);
        perm.add(call_phone);
        perm.add(camera);
        perm.add(change_network_state);
        perm.add(change_wifi_state);
        perm.add(foreground);
        perm.add(get_accounts);
        perm.add(internet);
        perm.add(kill_apps);
        perm.add(nfc);
        perm.add(pkg_usage_stats);
        perm.add(read_storage);
        perm.add(phone_state);
        perm.add(read_sms);
        perm.add(sync_stats);
        perm.add(receive_boot);
        perm.add(receive_sms);
        perm.add(record_audio);
        perm.add(request_delete_packages);
        perm.add(request_install_packages);
        perm.add(delete_packages);
        perm.add(install_packages);
        perm.add(system_alert_window);
        perm.add(vibrate);
        perm.add(wake_lock);
        perm.add(write_storage);
        perm.add(write_secure_settings);
        perm.add(write_settings);
        perm.add(read_call_logs);
        perm.add(read_contacts);
        perm.add(modify_audio_settings);
        perm.add(read_sync_settings);
        perm.add(write_contacts);
        perm.add(write_sync_settings);
        perm.add(account_manager);
        perm.add(use_finger);
        perm.add(use_bio);
        perm.add(manage_own_logs);
        perm.add(use_fullscreen_intent);
        perm.add(back_loc);
        perm.add(download_notnoti);
        perm.add(modify_phone_state);
        perm.add(send_sms);
        perm.add(bluetooth_privileged);
        perm.add(network_settings);
        perm.add(access_noti);
        perm.add(receive_info);
        perm.add(read_google_service);
        perm.add(read_launcher);
        perm.add(write_launcher);
        perm.add(install_launcher);
        perm.add(uninstall_launcher);
        perm.add(knox_enterprise_admin);
        perm.add(manage_accounts);
        perm.add(write_google_service);
        perm.add(vending_check_license);
        perm.add(vending_billing);
        perm.add(get_tasks);
        perm.add(bind_get_install);
        perm.add(write_media_storage);
        perm.add(access_internal_storage);
        return perm;
    }
    
    
public static ArrayList<String> allpermissioninfo() {
        ArrayList<String> perm = new ArrayList<>();
        perm.add(handoverinfo);
        perm.add(checkinpropinfo);
        perm.add(coarse_locationinfo);
        perm.add(fine_locationinfo);
        perm.add(network_stateinfo);
        perm.add(access_noti_policyinfo);
        perm.add(wifi_stateinfo);
        perm.add(answer_phoneinfo);
        perm.add(battery_stateinfo);
        perm.add(bind_admininfo);
        perm.add(bluetoothinfo);
        perm.add(bluetoothadmininfo);
        perm.add(call_phoneinfo);
        perm.add(camerainfo);
        perm.add(change_network_stateinfo);
        perm.add(change_wifi_stateinfo);
        perm.add(foregroundinfo);
        perm.add(get_accountsinfo);
        perm.add(internetinfo);
        perm.add(kill_appsinfo);
        perm.add(nfcinfo);
        perm.add(pkg_usage_statsinfo);
        perm.add(read_storageinfo);
        perm.add(phone_stateinfo);
        perm.add(read_smsinfo);
        perm.add(sync_statsinfo);
        perm.add(receive_bootinfo);
        perm.add(receive_smsinfo);
        perm.add(record_audioinfo);
        perm.add(request_delete_packagesinfo);
        perm.add(request_install_packagesinfo);
        perm.add(delete_packagesinfo);
        perm.add(install_packagesinfo);
        perm.add(system_alert_windowinfo);
        perm.add(vibrateinfo);
        perm.add(wake_lockinfo);
        perm.add(write_storageinfo);
        perm.add(write_secure_settingsinfo);
        perm.add(write_settingsinfo);
        perm.add(read_call_logsinfo);
        perm.add(read_contactsinfo);
        perm.add(modify_audio_settingsinfo);
        perm.add(read_sync_settingsinfo);
        perm.add(write_contactsinfo);
        perm.add(write_sync_settingsinfo);
        perm.add(account_managerinfo);
        perm.add(use_fingerinfo);
        perm.add(use_bioinfo);
        perm.add(manage_own_logsinfo);
        perm.add(use_fullscreen_intentinfo);
        perm.add(back_locinfo);
        perm.add(download_notnotiinfo);
        perm.add(modify_phone_stateinfo);
        perm.add(send_smsinfo);
        perm.add(bluetooth_privilegedinfo);
        perm.add(network_settingsinfo);
        perm.add(access_notiinfo);
        perm.add(receive_infoinfo);
        perm.add(read_google_serviceinfo);
        perm.add(read_launcherinfo);
        perm.add(write_launcherinfo);
        perm.add(install_launcherinfo);
        perm.add(uninstall_launcherinfo);
        perm.add(knox_enterprise_admininfo);
        perm.add(manage_accountsinfo);
        perm.add(write_google_serviceinfo);
        perm.add(vending_check_licenseinfo);
        perm.add(vending_billinginfo);
        perm.add(get_tasksinfo);
        perm.add(bind_get_installinfo);
        perm.add(write_media_storageinfo);
    perm.add(access_internal_storageinfo);
        return perm;
    }



public static String getpermissioninfo(String peui, Context c) {
    String pey = "";
    String fullpermissioninfo;
    ArrayList<String> perm = allpermission();
    ArrayList<String> perminfo = allpermissioninfo();
    double n1 = perm.size();
    for(int pos= 0; pos < (int)(n1); pos++) {
	if (perm.get((pos)).equals(peui)) {
		pey = perminfo.get((pos));
	}
}
if (!pey.equals("")) {
    fullpermissioninfo = pey;
} else {
    fullpermissioninfo = c.getString(R.string.no_perinfo);
}
return fullpermissioninfo;
}



}