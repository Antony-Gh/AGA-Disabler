package com.aga.disabler.pro.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.aga.disabler.pro.R;
import com.aga.disabler.pro.tools.CustomDialog;
import com.aga.disabler.pro.tools.Helper;
import com.samsung.android.knox.EnterpriseDeviceManager;
import com.samsung.android.knox.application.ApplicationPolicy;
import com.samsung.android.knox.bluetooth.BluetoothPolicy;
import com.samsung.android.knox.custom.CustomDeviceManager;
import com.samsung.android.knox.custom.SystemManager;
import com.samsung.android.knox.kiosk.KioskMode;
import com.samsung.android.knox.net.firewall.Firewall;
import com.samsung.android.knox.net.wifi.WifiPolicy;
import com.samsung.android.knox.restriction.RestrictionPolicy;

import org.jetbrains.annotations.NotNull;

import static com.aga.disabler.pro.R.id;
import static com.aga.disabler.pro.R.layout;
import static com.aga.disabler.pro.receiver.devicepolicy.*;
import static com.aga.disabler.pro.tools.Helper.keys;

public class PoliciesActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private CheckBox wifi;
    private CheckBox wifichange;
    private CheckBox wifiauto;
    private CheckBox wifiscan;
    private CheckBox wifidirect;
    private CheckBox wifihot;
    private CheckBox bluetooth;
    private CheckBox bluetoothteth;
    private CheckBox bluetoothpairing;
    private CheckBox bluetoothvisible;
    private CheckBox bluetoothpcpairing;
    private CheckBox bluetoothdata;
    private CheckBox adminact;
    private CheckBox adminins;
    private CheckBox nonmarket;
    private CheckBox airplane;
    private CheckBox androidbeam;
    private CheckBox clipapps;
    private CheckBox datasaver;
    private CheckBox devmode;
    private CheckBox facres;
    private CheckBox downmode;
    private CheckBox googlesync;
    private CheckBox googlerep;
    private CheckBox lockv;
    private CheckBox otaup;
    private CheckBox powers;
    private CheckBox usbhost;
    private CheckBox moblim;
    private CheckBox vpn;
    private CheckBox wallc;
    private CheckBox googleb;
    private CheckBox cam;
    private CheckBox mobda;
    private CheckBox clip;
    private CheckBox head;
    private CheckBox lock;
    private CheckBox mic;
    private CheckBox sccap;
    private CheckBox sdca;
    private CheckBox usbco;
    private CheckBox safe;
    private CheckBox teth;
    private CheckBox scrpin;
    private CheckBox shli;
    private CheckBox stae;
    private CheckBox setc;
    private CheckBox key1;
    private CheckBox key2;
    private CheckBox key3;
    private CheckBox key4;
    private CheckBox key5;
    private CheckBox key6;
    private CheckBox porn;
    private CheckBox devoff;
    private CheckBox ch;
    private KioskMode kio;
    private RestrictionPolicy rp;
    private Context c;
    private WifiPolicy wifiPolicy;
    private BluetoothPolicy bluetoothPolicy;
    private ApplicationPolicy applicationPolicy;
    private CustomDeviceManager cdm;
    private SystemManager sysma;
    private Firewall fire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.policies);
        init();
        initcheckstate();
        initlisten();
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

private void init() {
        c = PoliciesActivity.this.getApplicationContext();
    EnterpriseDeviceManager edm = EnterpriseDeviceManager.getInstance(c);
        rp = edm.getRestrictionPolicy();
        wifiPolicy = edm.getWifiPolicy();
        bluetoothPolicy = edm.getBluetoothPolicy();
        applicationPolicy = edm.getApplicationPolicy();
        cdm = CustomDeviceManager.getInstance();
        sysma = cdm.getSystemManager();
        kio = edm.getKioskMode();
        wifi = findViewById(id._wifi_check);
        wifichange = findViewById(id._wifichange_check);
        fire = edm.getFirewall();
    wifiauto = findViewById(id._wifiauto_check);
    wifiscan = findViewById(id._wifiscan_check);
    wifidirect = findViewById(id._wifidirect_check);
    wifihot = findViewById(id._wifihot_check);
    bluetooth = findViewById(id._bluetooth_check);
    bluetoothteth = findViewById(id._bluetoothtethering_check);
    bluetoothpairing = findViewById(id._bluetoothpairing_check);
    bluetoothvisible = findViewById(id._bluetoothvisible_check);
    bluetoothpcpairing = findViewById(id._bluetoothlaptop_check);
    bluetoothdata = findViewById(id._bluetoothdata_check);
    adminact = findViewById(id._appadminact_check);
    adminins = findViewById(id._appadminins_check);
    nonmarket = findViewById(id._appun_check);
    airplane = findViewById(id._airplane_check);
    androidbeam = findViewById(id._androidbeam_check);
    clipapps = findViewById(id._clipapps_check);
    datasaver = findViewById(id._datasaver_check);
    devmode = findViewById(id._devmode_check);
    facres = findViewById(id._facres_check);
    downmode = findViewById(id._downmode_check);
    googlesync = findViewById(id._googlesync_check);
    googlerep = findViewById(id._googlerep_check);
    lockv = findViewById(id._lockviews_check);
    otaup = findViewById(id._otaup_check);
    powers = findViewById(id._powers_check);
    usbhost = findViewById(id._usbhost_check);
    moblim = findViewById(id._moblim_check);
    vpn = findViewById(id._vpn_check);
    wallc = findViewById(id._wallc_check);
    googleb = findViewById(id._googleb_check);
    cam = findViewById(id._cam_check);
    mobda = findViewById(id._mobda_check);
    clip = findViewById(id._clip_check);
    head = findViewById(id._head_check);
    lock = findViewById(id._lock_check);
    mic = findViewById(id._mic_check);
    sccap = findViewById(id._sccap_check);
    sdca = findViewById(id._sdca_check);
    usbco = findViewById(id._usbco_check);
    safe = findViewById(id._safe_check);
    teth = findViewById(id._teth_check);
    scrpin = findViewById(id._scrpin_check);
    shli = findViewById(id._shli_check);
    stae = findViewById(id._stae_check);
    setc = findViewById(id._setc_check);
    key1 = findViewById(id._key1_check);
    key2 = findViewById(id._key2_check);
    key3 = findViewById(id._key3_check);
    key4 = findViewById(id._key4_check);
    key5 = findViewById(id._key5_check);
    key6 = findViewById(id._key6_check);
    porn = findViewById(id._porn_check);
    devoff = findViewById(id._devoff_check);
    ch = findViewById(id._ch_check);
}

private void initcheckstate() {
        wifi.setChecked(rp.isWiFiEnabled(false));
    wifichange.setChecked(wifiPolicy.isWifiStateChangeAllowed());
    wifiauto.setChecked(wifiPolicy.getAutomaticConnectionToWifi());
    wifiscan.setChecked(wifiPolicy.isWifiStateChangeAllowed());
    wifidirect.setChecked(rp.isWifiDirectAllowed());
    wifihot.setChecked(rp.isWifiTetheringEnabled());
    bluetooth.setChecked(rp.isBluetoothEnabled(false));
    bluetoothteth.setChecked(rp.isBluetoothTetheringEnabled());
    bluetoothpairing.setChecked(bluetoothPolicy.isPairingEnabled());
    bluetoothvisible.setChecked(bluetoothPolicy.isDiscoverableEnabled());
    bluetoothpcpairing.setChecked(bluetoothPolicy.isDesktopConnectivityEnabled());
    bluetoothdata.setChecked(bluetoothPolicy.getAllowBluetoothDataTransfer());
    adminact.setChecked(applicationPolicy.isNewAdminActivationEnabled(false));
    adminins.setChecked(applicationPolicy.isNewAdminInstallationEnabled(false));
    nonmarket.setChecked(rp.isNonMarketAppAllowed());
    airplane.setChecked(rp.isAirplaneModeAllowed());
    androidbeam.setChecked(rp.isAndroidBeamAllowed());
    clipapps.setChecked(rp.isClipboardShareAllowed());
    datasaver.setChecked(rp.isDataSavingAllowed());
    devmode.setChecked(rp.isDeveloperModeAllowed());
    facres.setChecked(rp.isFactoryResetAllowed());
    downmode.setChecked(rp.isFirmwareRecoveryAllowed(false));
    googlesync.setChecked(rp.isGoogleAccountsAutoSyncAllowed());
    googlerep.setChecked(rp.isGoogleCrashReportAllowed());
    lockv.setChecked(rp.isLockScreenViewAllowed(RestrictionPolicy.LOCKSCREEN_SHORTCUTS_VIEW));
    otaup.setChecked(rp.isOTAUpgradeAllowed());
    powers.setChecked(rp.isPowerSavingModeAllowed());
    usbhost.setChecked(rp.isUsbHostStorageAllowed());
    moblim.setChecked(rp.isUserMobileDataLimitAllowed());
    vpn.setChecked(rp.isVpnAllowed());
    wallc.setChecked(rp.isWallpaperChangeAllowed());
    googleb.setChecked(rp.isBackupAllowed(false));
    cam.setChecked(rp.isCameraEnabled(false));
    mobda.setChecked(rp.isCellularDataAllowed());
    clip.setChecked(rp.isClipboardAllowed(false));
    head.setChecked(rp.isHeadphoneEnabled(false));
    lock.setChecked(rp.isLockScreenEnabled(false));
    mic.setChecked(rp.isMicrophoneEnabled(false));
    sccap.setChecked(rp.isScreenCaptureEnabled(false));
    sdca.setChecked(rp.isSdCardEnabled());
    usbco.setChecked(sysma.getUsbConnectionType() != CustomDeviceManager.USB_CONNECTION_TYPE_CHARGING);
    safe.setChecked(rp.isSafeModeAllowed());
    teth.setChecked(rp.isTetheringEnabled());
    scrpin.setChecked(rp.isScreenPinningAllowed());
    shli.setChecked(rp.isShareListAllowed());
    stae.setChecked(rp.isStatusBarExpansionAllowed());
    setc.setChecked(rp.isSettingsChangesAllowed(false));
key1.setChecked(kio.isHardwareKeyAllowed(keys[0]));
    key2.setChecked(kio.isHardwareKeyAllowed(keys[1]));
    key3.setChecked(kio.isHardwareKeyAllowed(keys[2]));
    key4.setChecked(kio.isHardwareKeyAllowed(keys[3]));
    key5.setChecked(kio.isHardwareKeyAllowed(keys[4]));
    key6.setChecked(kio.isHardwareKeyAllowed(keys[5]));
    boolean b = fire.isFirewallEnabled();
    porn.setChecked(b);
    if(b){
        porn.setEnabled(false);
    }
    devoff.setChecked(rp.isPowerOffAllowed());
    ch.setChecked(true);

}

    private void initlisten() {
        wifi.setOnCheckedChangeListener(this);
        wifichange.setOnCheckedChangeListener(this);
        wifiauto.setOnCheckedChangeListener(this);
        wifiscan.setOnCheckedChangeListener(this);
        wifidirect.setOnCheckedChangeListener(this);
        wifihot.setOnCheckedChangeListener(this);
        bluetooth.setOnCheckedChangeListener(this);
        bluetoothteth.setOnCheckedChangeListener(this);
        bluetoothpairing.setOnCheckedChangeListener(this);
        bluetoothvisible.setOnCheckedChangeListener(this);
        bluetoothpcpairing.setOnCheckedChangeListener(this);
        bluetoothdata.setOnCheckedChangeListener(this);
        adminact.setOnCheckedChangeListener(this);
        adminins.setOnCheckedChangeListener(this);
        nonmarket.setOnCheckedChangeListener(this);
        airplane.setOnCheckedChangeListener(this);
        androidbeam.setOnCheckedChangeListener(this);
        clipapps.setOnCheckedChangeListener(this);
        datasaver.setOnCheckedChangeListener(this);
        devmode.setOnCheckedChangeListener(this);
        facres.setOnCheckedChangeListener(this);
        downmode.setOnCheckedChangeListener(this);
        googlesync.setOnCheckedChangeListener(this);
        googlerep.setOnCheckedChangeListener(this);
        lockv.setOnCheckedChangeListener(this);
        otaup.setOnCheckedChangeListener(this);
        powers.setOnCheckedChangeListener(this);
        usbhost.setOnCheckedChangeListener(this);
        moblim.setOnCheckedChangeListener(this);
        vpn.setOnCheckedChangeListener(this);
        wallc.setOnCheckedChangeListener(this);
        googleb.setOnCheckedChangeListener(this);
        cam.setOnCheckedChangeListener(this);
        mobda.setOnCheckedChangeListener(this);
        clip.setOnCheckedChangeListener(this);
        head.setOnCheckedChangeListener(this);
        lock.setOnCheckedChangeListener(this);
        mic.setOnCheckedChangeListener(this);
        sccap.setOnCheckedChangeListener(this);
        sdca.setOnCheckedChangeListener(this);
        usbco.setOnCheckedChangeListener(this);
        safe.setOnCheckedChangeListener(this);
        teth.setOnCheckedChangeListener(this);
        scrpin.setOnCheckedChangeListener(this);
        shli.setOnCheckedChangeListener(this);
        stae.setOnCheckedChangeListener(this);
        setc.setOnCheckedChangeListener(this);
        key1.setOnCheckedChangeListener(this);
        key2.setOnCheckedChangeListener(this);
        key3.setOnCheckedChangeListener(this);
        key4.setOnCheckedChangeListener(this);
        key5.setOnCheckedChangeListener(this);
        key6.setOnCheckedChangeListener(this);
        porn.setOnCheckedChangeListener(this);
        devoff.setOnCheckedChangeListener(this);
        ch.setOnCheckedChangeListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case id._wifi_check:
                wifi(isChecked, c);
                break;
            case id._wifichange_check:
                wifichange(isChecked, c);
                break;
            case id._wifiauto_check:
                automaticwificonnect(isChecked, c);
                break;
            case id._wifiscan_check:
                wifiscanning(isChecked, c);
                break;
            case id._wifidirect_check:
                wifidirect(isChecked, c);
                break;
            case id._wifihot_check:
                wifihotspot(isChecked, c);
                break;
            case id._bluetooth_check:
                bluetooth(isChecked, c);
                break;
            case id._bluetoothtethering_check:
                bluetoothtethring(isChecked, c);
                break;
            case id._bluetoothpairing_check:
                bluetoothpairing(isChecked, c);
                break;
            case id._bluetoothvisible_check:
                bluetoothvisible(isChecked, c);
                break;
            case id._bluetoothlaptop_check:
                bluetoothpairinglaptop(isChecked, c);
                break;
            case id._bluetoothdata_check:
                bluetoothdatatransfer(isChecked, c);
                break;
            case id._appadminact_check:
                adminactivate(isChecked, c);
                break;
            case id._appadminins_check:
                admininstall(isChecked, c);
                break;
            case id._appun_check:
                nonmarket(isChecked, c);
                break;
            case id._airplane_check:
            airplane(isChecked, c);
            break;
            case id._androidbeam_check:
                androidbeam(isChecked, c);
                break;
            case id._clipapps_check:
                clipapps(isChecked, c);
                break;
            case id._datasaver_check:
                datasaver(isChecked, c);
                break;
            case id._devmode_check:
                devmode(isChecked,c);
                break;
            case id._facres_check:
                facres(isChecked,c);
                break;
            case id._downmode_check:
                downmode(isChecked,c);
                break;
            case id._googlesync_check:
                googlesync(isChecked, c);
                break;
            case id._googlerep_check:
                googlerep(isChecked, c);
                break;
            case id._lockviews_check:
                lockv(isChecked,c);
                break;
            case id._otaup_check:
                otaup(isChecked,c);
                break;
            case id._powers_check:
                powers(isChecked,c);
                break;
            case id._usbhost_check:
                usbhost(isChecked,c);
                break;
            case id._moblim_check:
                moblim(isChecked,c);
                break;
            case id._vpn_check:
                vpn(isChecked,c);
                break;
            case id._wallc_check:
                wallc(isChecked,c);
                break;
            case id._googleb_check:
                googleb(isChecked,c);
                break;
            case id._cam_check:
                cam(isChecked,c);
                break;
            case id._mobda_check:
                mobda(isChecked,c);
                break;
            case id._clip_check:
                clip(isChecked,c);
                break;
            case id._head_check:
                head(isChecked,c);
                break;
            case id._lock_check:
                lock(isChecked, c);
                break;
            case id._mic_check:
                mic(isChecked, c);
                break;
            case id._sccap_check:
                sccap(isChecked,c);
                break;
            case id._sdca_check:
                sdca(isChecked,c);
                break;
            case id._usbco_check:
                usbco(isChecked,c);
                break;
            case id._safe_check:
                safe(isChecked,c);
                break;
            case id._teth_check:
                teth(isChecked,c);
                break;
            case id._scrpin_check:
               scrpin(isChecked,c);
                break;
            case id._shli_check:
                shli(isChecked,c);
                break;
            case id._stae_check:
                stae(isChecked,c);
                break;
            case id._setc_check:
                setc(isChecked,c);
                break;
            case id._key1_check:
                backkey(isChecked, c);
                break;
            case id._key2_check:
                homekey(isChecked, c);
                break;
            case id._key3_check:
                recentkey(isChecked, c);
                break;
            case id._key4_check:
                powerkey(isChecked, c);
                break;
            case id._key5_check:
                volupkey(isChecked, c);
                break;
            case id._key6_check:
                voldkey(isChecked, c);
                break;
            case id._porn_check:
                if(isChecked) {
                    Helper.CreateKnoxAlert(this, getString(R.string.dis_porn_title), getString(R.string.dis_porn_desc), new CustomDialog.onClick() {
                        @Override
                        public void onOkClick() {
                            disableporn(true, c);
                            porn.setEnabled(false);
                        }
                        @Override
                        public void onCancelClick() {
                            porn.setChecked(false);
                            porn.setEnabled(true);
                        }
                    });
                }else{
                    disableporn(false, c);
                }
                break;
            case id._devoff_check:
                devoff(isChecked, c);
                break;
            case id._ch_check:

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + buttonView.getId());
        }
    }

}
