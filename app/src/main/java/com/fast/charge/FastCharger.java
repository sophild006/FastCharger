package com.fast.charge;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fast.charge.util.Utils;
import com.fast.charge.view.WaveProgressView;

import java.lang.reflect.Method;
import java.util.List;

public class FastCharger extends AppCompatActivity implements OnClickListener {
    CardView cdTickView;//CardViewTickleview
    CardView cdToolsView;//CardViewTools
    CardView cdShareView;
    RelativeLayout rlAlert;
    TextView tvAlert;//AlertText
    TextView tvpowerType;//PowerType
    Integer alertCount = Integer.valueOf(0);
    Integer audioType;
    private int brightValue;
    private int rotateVlaue;
    private int timeoutValue;
    private ContentResolver contentResolver;
    private Window window;
    Toolbar toolbar;
    WaveProgressView waveProgressbar;
    BroadcastReceiver batteryReceiver;
    ImageView ivFirstTickProcess;// 
    ImageView ivSecendProcess;//SecondTickleProcess
    ImageView ivThirdProcess;//ThirdTickleProcess
    ImageView ivWifi;
    ImageView ivTimeOut;//tool_timeout
    ImageView ivBrightness;
    ImageView ivBlueteeth;
    ImageView ivMode;
    ImageView ivRotate;
    Animation animation;
    BluetoothAdapter blueAdapter;
    RelativeLayout rlTickView;//main_lout_tickleview
    Button btnStartFastCharging;//StartFastCharger
    AudioManager audioManager;
    RelativeLayout rlBeforeCharging;
    RelativeLayout rlAfterView;
    Button btnFeedBack;
    Button btnRateNow;
    CardView cdRateView;
    CardView cdChargingType;//CardViewBattery

    class BatteryReceiver extends BroadcastReceiver {
        /* synthetic */ FastCharger activity;

        BatteryReceiver(FastCharger fastCharger) {
            this.activity = fastCharger;
        }

        public void onReceive(Context context, Intent intent) {
            int i = -1;
            if (intent.getAction() == "android.intent.action.BATTERY_CHANGED") {
                int intExtra = intent.getIntExtra("level", -1);
                int intExtra2 = intent.getIntExtra("scale", -1);
                int intExtra3 = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                if (intExtra >= 0 && intExtra2 > 0) {
                    i = (intExtra * 100) / intExtra2;
                    Log.e("%", "" + i);
                }
                waveProgressbar.setMaxProgress(100);
                waveProgressbar.m543a(i, String.valueOf(i) + "%");
                waveProgressbar.m542a(8.0f, 130.0f);
                waveProgressbar.m544a("#4D000000", 70);
                waveProgressbar.setWaveColor("#4D76FF03");
                waveProgressbar.setmWaveSpeed(15);
                ivFirstTickProcess.setVisibility(View.GONE);
                ivSecendProcess.setVisibility(View.GONE);
                ivThirdProcess.setVisibility(View.GONE);
                if (i <= 90) {
                    ivFirstTickProcess.startAnimation(animation);
                    ivSecendProcess.clearAnimation();
                    ivThirdProcess.clearAnimation();
                    ivFirstTickProcess.setVisibility(View.VISIBLE);
                    ivSecendProcess.setVisibility(View.GONE);
                    ivThirdProcess.setVisibility(View.GONE);
                }
                if (i <= 98 && i >= 91) {
                    ivSecendProcess.startAnimation(animation);
                    ivFirstTickProcess.clearAnimation();
                    ivThirdProcess.clearAnimation();
                    ivSecendProcess.setVisibility(View.VISIBLE);
                    ivFirstTickProcess.setVisibility(View.GONE);
                    ivThirdProcess.setVisibility(View.GONE);
                }
                if (i >= 99) {
                    ivThirdProcess.startAnimation(animation);
                    ivFirstTickProcess.clearAnimation();
                    ivSecendProcess.clearAnimation();
                    ivThirdProcess.setVisibility(View.VISIBLE);
                    ivFirstTickProcess.setVisibility(View.GONE);
                    ivSecendProcess.setVisibility(View.GONE);
                }
                switch (intExtra3) {
                    case 1:
                        tvpowerType.setText(this.activity.getResources().getString(R.string.fast_charger_ac));
                        break;
                    case 2:
                        tvpowerType.setText(this.activity.getResources().getString(R.string.fast_charger_usb));
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        activity.batteryFull();
                        break;
                }
            }
            if (intent.getAction() == "android.intent.action.ACTION_POWER_DISCONNECTED") {
                this.activity.finish();
            }
        }
    }


    private static String m9352a(String str) {
        if (!TextUtils.isEmpty(str)) {
            char[] toCharArray = str.toCharArray();
            str = "";
            int length = toCharArray.length;
            int i = 0;
            Object obj = 1;
            while (i < length) {
                String str2;
                char c = toCharArray[i];
                if (obj == null || !Character.isLetter(c)) {
                    if (Character.isWhitespace(c)) {
                        obj = 1;
                    }
                    str2 = str + c;
                } else {
                    str2 = str + Character.toUpperCase(c);
                    obj = null;
                }
                i++;
                str = str2;
            }
        }
        return str;
    }

    private void resetTimeout(int i) {
        int i2;
        switch (i) {
            case 0:
                i2 = 10000;
                break;
            case 1:
                i2 = 20000;
                break;
            case 2:
                i2 = 30000;
                break;
            case 3:
                i2 = 40000;
                break;
            default:
                i2 = -1;
                break;
        }
        System.putInt(getContentResolver(), "screen_off_timeout", i2);
    }

    public static String m9356d() {
        String str = Build.MANUFACTURER;
        String str2 = Build.MODEL;
        return str2.startsWith(str) ? m9352a(str2) : m9352a(str) + " " + str2;
    }

    private void initEvent() {
        this.ivWifi.setOnClickListener(this);
        this.btnStartFastCharging.setOnClickListener(this);
        this.ivMode.setOnClickListener(this);
        this.ivBlueteeth.setOnClickListener(this);
        this.ivBrightness.setOnClickListener(this);
        this.ivTimeOut.setOnClickListener(this);
        this.ivRotate.setOnClickListener(this);
        this.cdShareView.setOnClickListener(this);
        this.btnFeedBack.setOnClickListener(this);
        this.btnRateNow.setOnClickListener(this);
        this.rlAlert.setOnClickListener(this);
    }

    private void initView() {
        this.waveProgressbar = (WaveProgressView) findViewById(R.id.waveProgressbar);
        this.ivFirstTickProcess = (ImageView) findViewById(R.id.FirstTickleProcess);
        this.ivSecendProcess = (ImageView) findViewById(R.id.SecondTickleProcess);
        this.ivThirdProcess = (ImageView) findViewById(R.id.ThirdTickleProcess);
        this.ivWifi = (ImageView) findViewById(R.id.tool_wifi);
        this.ivRotate = (ImageView) findViewById(R.id.tool_rotate);
        this.ivBrightness = (ImageView) findViewById(R.id.tool_brightness);
        this.ivBlueteeth = (ImageView) findViewById(R.id.tool_bluetooth);
        this.ivMode = (ImageView) findViewById(R.id.tool_mode);
        this.ivTimeOut = (ImageView) findViewById(R.id.tool_timeout);
        this.btnStartFastCharging = (Button) findViewById(R.id.StartFastCharger);
        this.rlTickView = (RelativeLayout) findViewById(R.id.main_rl_tickview);
        this.rlBeforeCharging = (RelativeLayout) findViewById(R.id.beforeFullCharge);
        this.rlAfterView = (RelativeLayout) findViewById(R.id.afterFullCharge);
        this.cdRateView = (CardView) findViewById(R.id.CardViewRate);
        this.cdChargingType = (CardView) findViewById(R.id.CardViewBattery);
        this.cdTickView = (CardView) findViewById(R.id.CardViewTickleview);
        this.cdToolsView = (CardView) findViewById(R.id.CardViewTools);
        this.cdShareView = (CardView) findViewById(R.id.CardViewShare);
        this.btnFeedBack = (Button) findViewById(R.id.btn_feedbak);
        this.btnRateNow = (Button) findViewById(R.id.btn_ratenow);
        this.rlAlert = (RelativeLayout) findViewById(R.id.AlertLout);
        this.tvAlert = (TextView) findViewById(R.id.AlertText);
        this.tvpowerType = (TextView) findViewById(R.id.PowerType);
    }

    private void initCardView() {
        this.cdRateView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        this.cdChargingType.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        this.cdTickView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        this.cdToolsView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        this.cdShareView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        this.cdChargingType.setCardElevation(0.0f);
        this.cdToolsView.setCardElevation(0.0f);
        this.cdTickView.setCardElevation(0.0f);
        this.cdRateView.setCardElevation(0.0f);
        this.cdShareView.setCardElevation(0.0f);
    }

    private void registerBatterReceiver() {
        this.batteryReceiver = new BatteryReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        registerReceiver(this.batteryReceiver, intentFilter);
    }

    private void batteryFull() {
        this.rlBeforeCharging.setVisibility(View.GONE);
        this.rlAfterView.setVisibility(View.VISIBLE);
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        final Animation loadAnimation2 = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        this.cdRateView.setVisibility(View.VISIBLE);
        this.cdRateView.startAnimation(loadAnimation);
        loadAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                cdShareView.setVisibility(View.VISIBLE);
                cdShareView.startAnimation(loadAnimation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    private void initStatus() {

        if (((WifiManager) getSystemService(Context.WIFI_SERVICE)).isWifiEnabled()) {
            this.ivWifi.setImageResource(R.drawable.ic_tool_wifi_on);
        } else {
            this.ivWifi.setImageResource(R.drawable.ic_tool_wifi_off);
        }
        if (this.rotateVlaue == 1) {
            this.ivRotate.setImageResource(R.drawable.ic_tool_rotate_autorotate);
        } else {
            this.ivRotate.setImageResource(R.drawable.ic_tool_rotate_portiat);
        }
        this.blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.blueAdapter != null) {
            if (this.blueAdapter.isEnabled()) {
                this.ivBlueteeth.setImageResource(R.drawable.ic_tool_bluetooth_on);
            } else {
                this.ivBlueteeth.setImageResource(R.drawable.ic_tool_bluetooth_off);
            }
        }
        if (this.brightValue > 20) {
            this.ivBrightness.setImageResource(R.drawable.ic_tool_brightness_on);
        } else {
            this.ivBrightness.setImageResource(R.drawable.ic_tool_brightness_off);
        }
        if (this.timeoutValue == 10000) {
            this.ivTimeOut.setImageResource(R.drawable.ic_tool_timeout_ten);
        } else if (this.timeoutValue == 20000) {
            this.ivTimeOut.setImageResource(R.drawable.ic_tool_timeout_twenty);
        } else if (this.timeoutValue == 30000) {
            this.ivTimeOut.setImageResource(R.drawable.ic_tool_timeout_thirty);
        } else if (this.timeoutValue == 40000) {
            this.ivTimeOut.setImageResource(R.drawable.ic_tool_timeout_fourty);
        } else {
            this.ivTimeOut.setImageResource(R.drawable.ic_tool_timeout_fourty);
            resetTimeout(3);
            this.timeoutValue = 40000;
        }
        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        switch (this.audioManager.getRingerMode()) {
            case 0:
                this.ivMode.setImageResource(R.drawable.ic_tool_profile_silent);
                this.audioType = Integer.valueOf(0);
                return;
            case 1:
                this.ivMode.setImageResource(R.drawable.ic_tool_profile_vibrate);
                this.audioType = Integer.valueOf(1);
                return;
            case 2:
                this.ivMode.setImageResource(R.drawable.ic_tool_profile_normal);
                this.audioType = Integer.valueOf(2);
                return;
            default:
                return;
        }

    }

    public void initAlert() {
        this.alertCount = Integer.valueOf(0);
        if (Boolean.valueOf(((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps")).booleanValue()) {
            this.alertCount = Integer.valueOf(this.alertCount.intValue() + 1);
        }
        if (!Utils.isAirPlaneOpend(getApplicationContext())) {
            this.alertCount = Integer.valueOf(this.alertCount.intValue() + 1);
            if (Utils.isMobleDataOpend().booleanValue()) {
                this.alertCount = Integer.valueOf(this.alertCount.intValue() + 1);
            }
        }
        this.tvAlert.setText(String.valueOf(this.alertCount));
        if (this.alertCount.intValue() == 0) {
            this.rlAlert.setVisibility(View.GONE);
        }
    }

    public void m9365c() {
        List<ApplicationInfo> installedApplications = getPackageManager().getInstalledApplications(0);
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ApplicationInfo applicationInfo : installedApplications) {
            Log.e("pakages", installedApplications + "");
            if ((applicationInfo.flags & 1) != 1) {
                activityManager.killBackgroundProcesses(applicationInfo.packageName);
            }
        }
        if (this.brightValue > 20) {
            System.putInt(contentResolver, "screen_brightness", 20);
            LayoutParams attributes = this.window.getAttributes();
            attributes.screenBrightness = 20.0f;
            this.window.setAttributes(attributes);
            this.ivBrightness.setImageResource(R.drawable.ic_tool_brightness_off);
            this.brightValue = 20;
        }
        ((WifiManager) getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(false);
        this.ivWifi.setImageResource(R.drawable.ic_tool_wifi_off);
        this.blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.blueAdapter != null && this.blueAdapter.isEnabled()) {
            this.blueAdapter.disable();
        }
        System.putInt(getContentResolver(), "accelerometer_rotation", 0);
        this.ivRotate.setImageResource(R.drawable.ic_tool_rotate_portiat);
        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        this.audioManager.setRingerMode(0);
        this.ivMode.setImageResource(R.drawable.ic_tool_profile_silent);
        resetTimeout(0);
        this.timeoutValue = 10000;
        this.ivTimeOut.setImageResource(R.drawable.ic_tool_timeout_ten);
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        this.btnStartFastCharging.startAnimation(loadAnimation);
        loadAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btnStartFastCharging.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_feedbak:
//                DisplayMetrics displayMetrics = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//                int i = displayMetrics.heightPixels;
//                int i2 = displayMetrics.widthPixels;
//                PackageInfo packageInfo = null;
//                try {
//                    packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
//                } catch (NameNotFoundException e) {
//                    e.printStackTrace();
//                }
//                String str = packageInfo.versionName;
//                Intent intent = new Intent("android.intent.action.SEND");
//                intent.setType("message/rfc822");
//                intent.putExtra("android.intent.extra.EMAIL", new String[]{getResources().getString(R.string.contact_email)});
//                intent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name) + str);
//                intent.putExtra("android.intent.extra.TEXT", "\n Device :" + m9356d() + "\n SystemVersion:" + VERSION.SDK_INT + "\n Display Height  :" + i + "px\n Display Width  :" + i2 + "px\n\n " + getResources().getString(R.string.email_placeholder) + "\n");
//                startActivity(Intent.createChooser(intent, getResources().getString(R.string.send_email)));
                return;
            case R.id.btn_ratenow:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
                    return;
                } catch (ActivityNotFoundException e2) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    return;
                }
            case R.id.CardViewShare:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.SEND");
                intent1.setType("text/plain");
                intent1.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.checkout) + " " + getResources().getString(R.string.app_name) + ", " + getResources().getString(R.string.checkout_1) + " " + getResources().getString(R.string.app_name) + ". https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(intent1, getResources().getString(R.string.share) + " " + getResources().getString(R.string.app_name)));
                return;
            case R.id.AlertLout:
                Intent intent2 = new Intent(getApplicationContext(), CloseAllTools.class);
                intent2.putExtra("SetValue", 1);
                startActivity(intent2);
                return;
            case R.id.tool_wifi:
                ((WifiManager) getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(false);
                this.ivWifi.setImageResource(R.drawable.ic_tool_wifi_off);
                return;
            case R.id.tool_bluetooth:
                this.blueAdapter = BluetoothAdapter.getDefaultAdapter();
                if (this.blueAdapter != null && this.blueAdapter.isEnabled()) {
                    this.ivBlueteeth.setImageResource(R.drawable.ic_tool_bluetooth_off);
                    this.blueAdapter.disable();
                    return;
                }
                return;
            case R.id.tool_brightness:
                if (this.brightValue > 20) {
                    System.putInt(contentResolver, "screen_brightness", 20);
                    LayoutParams attributes = this.window.getAttributes();
                    attributes.screenBrightness = 20.0f;
                    this.window.setAttributes(attributes);
                    this.ivBrightness.setImageResource(R.drawable.ic_tool_brightness_off);
                    this.brightValue = 20;
                    return;
                }
                return;
            case R.id.tool_rotate:
                if (this.rotateVlaue == 1) {
                    System.putInt(getContentResolver(), "accelerometer_rotation", 0);
                    this.ivRotate.setImageResource(R.drawable.ic_tool_rotate_portiat);
                    this.rotateVlaue = 1;
                    return;
                }
                return;
            case R.id.tool_mode:
                this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                switch (this.audioManager.getRingerMode()) {
                    case 0:
                        return;
                    case 1:
                        this.ivMode.setImageResource(R.drawable.ic_tool_profile_silent);
                        this.audioManager.setRingerMode(0);
                        return;
                    case 2:
                        this.ivMode.setImageResource(R.drawable.ic_tool_profile_silent);
                        this.audioManager.setRingerMode(0);
                        return;
                    default:
                        this.ivMode.setImageResource(R.drawable.ic_tool_profile_silent);
                        this.audioManager.setRingerMode(0);
                        return;
                }
            case R.id.tool_timeout:
                if (this.timeoutValue == 10000) {
                    this.ivTimeOut.setImageResource(R.drawable.ic_tool_timeout_ten);
                    resetTimeout(0);
                    this.timeoutValue = 10000;
                    return;
                }
                this.ivTimeOut.setImageResource(R.drawable.ic_tool_timeout_ten);
                resetTimeout(0);
                this.timeoutValue = 10000;
                return;
            case R.id.StartFastCharger:
                Animation loadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                final Animation loadAnimation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                this.cdToolsView.startAnimation(loadAnimation);
                loadAnimation.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        cdToolsView.setVisibility(View.GONE);
                        cdTickView.setVisibility(View.VISIBLE);
                        cdTickView.startAnimation(loadAnimation2);
                        m9365c();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                });
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.fast_charger);
        this.toolbar = (Toolbar) findViewById(R.id.tool_bar);
        this.toolbar.setTitleTextColor(-1);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        contentResolver = getContentResolver();
        this.window = getWindow();
        initView();
        registerBatterReceiver();
        initStatus();
        initEvent();
        initAlert();
        initCardView();
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise);
        contentResolver = getContentResolver();
        this.window = getWindow();
        try {
            System.putInt(contentResolver, "screen_brightness_mode", 0);
            this.brightValue = System.getInt(contentResolver, "screen_brightness");
            this.rotateVlaue = System.getInt(contentResolver, "accelerometer_rotation");
            this.timeoutValue = System.getInt(contentResolver, "screen_off_timeout");
            if (this.timeoutValue > 40000) {
                resetTimeout(3);
                this.timeoutValue = 40000;
            }
        } catch (SettingNotFoundException e) {
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }
    }

    protected void onDestroy() {
        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        this.audioManager.setRingerMode(this.audioType.intValue());
        resetTimeout(3);
        unregisterReceiver(batteryReceiver);
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        initStatus();
    }

    protected void onRestart() {
        super.onRestart();
        initStatus();
    }

    protected void onResume() {
        super.onResume();
        initAlert();
        initStatus();

    }
}