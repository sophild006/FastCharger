package com.fast.charge;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IInterface;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fast.charge.util.Utils;
import com.fast.charge.view.ArcProgress;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ArcProgress arc_progress;
    private int rotateValue;
    private int brightnessValue;
    private int timeoutValue;//timeoutValue
    private int audioValue;
    private AudioManager audioManager;
    private ImageView ivRoate, ivBrightness, ivMode, ivWifi, ivBluetooth, ivTimeout;
    private Window window;
    private TextView tvOptimize, tvTemperate, tvVoltage, tvLevel, tvAlert;
    private LinearLayout llOptimizeCount, llShare;
    BluetoothAdapter defaultAdapter;
    private Toolbar toolbar;
    private CardView arcCarView, toolsCardView, tempCardView, feedCardView, share_cardView;
    Integer alertCount = Integer.valueOf(0);
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (defaultAdapter != null) {
                        if (defaultAdapter.isEnabled()) {
                            ivBluetooth.setImageResource(R.drawable.ic_tool_bluetooth_off);
                            defaultAdapter.disable();
                            return;
                        }
                        ivBluetooth.setImageResource(R.drawable.ic_tool_bluetooth_on);
                        defaultAdapter.enable();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(0);
        }
        setContentView(R.layout.main);
        window = getWindow();
        arc_progress = (ArcProgress) findViewById(R.id.arc_progress);
        arc_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        initView();
        initAlertCount();
        initBatteryData();
        initExitDialog();
        wifiReceiver = new WifiReceiver();
        registerWifiReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Settings.System.putInt(getContentResolver(), "screen_brightness_mode", 0);
            this.brightnessValue = Settings.System.getInt(getContentResolver(), "screen_brightness");
            this.rotateValue = Settings.System.getInt(getContentResolver(), "accelerometer_rotation");
            Log.d("wwq", "rotateValue: " + rotateValue);
            this.timeoutValue = Settings.System.getInt(getContentResolver(), "screen_off_timeout");
            if (this.timeoutValue > 40000) {
                resetTimeout(3);
                this.timeoutValue = 40000;
            }
            m9389k();
            initAlertCount();
            initBatteryData();
        } catch (Settings.SettingNotFoundException e) {
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }
    }

    private void m9389k() {
        if (((WifiManager) getSystemService(Context.WIFI_SERVICE)).isWifiEnabled()) {
            this.ivWifi.setImageResource(R.drawable.ic_tool_wifi_on);
        } else {
            this.ivWifi.setImageResource(R.drawable.ic_tool_wifi_off);
        }
        if (this.rotateValue == 1) {
            this.ivRoate.setImageResource(R.drawable.ic_tool_rotate_autorotate);
        } else {
            this.ivRoate.setImageResource(R.drawable.ic_tool_rotate_portiat);
        }
        defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.d("wwq", "onResume");
        if (this.defaultAdapter != null) {
            if (this.defaultAdapter.isEnabled()) {
                this.ivBluetooth.setImageResource(R.drawable.ic_tool_bluetooth_on);
            } else {
                this.ivBluetooth.setImageResource(R.drawable.ic_tool_bluetooth_off);
            }
        }
        if (this.brightnessValue > 20) {
            this.ivBrightness.setImageResource(R.drawable.ic_tool_brightness_on);
        } else {
            this.ivBrightness.setImageResource(R.drawable.ic_tool_brightness_off);
        }
        if (this.timeoutValue == 10000) {
            this.ivTimeout.setImageResource(R.drawable.ic_tool_timeout_ten);
        } else if (this.timeoutValue == 20000) {
            this.ivTimeout.setImageResource(R.drawable.ic_tool_timeout_twenty);
        } else if (this.timeoutValue == 30000) {
            this.ivTimeout.setImageResource(R.drawable.ic_tool_timeout_thirty);
        } else if (this.timeoutValue == 40000) {
            this.ivTimeout.setImageResource(R.drawable.ic_tool_timeout_fourty);
        } else {
            this.ivTimeout.setImageResource(R.drawable.ic_tool_timeout_fourty);
            resetTimeout(3);
            this.timeoutValue = 40000;
        }
        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        switch (this.audioManager.getRingerMode()) {
            case 0:
                this.ivMode.setImageResource(R.drawable.ic_tool_profile_silent);
                this.audioValue = Integer.valueOf(0);
                return;
            case 1:
                this.ivMode.setImageResource(R.drawable.ic_tool_profile_vibrate);
                this.audioValue = Integer.valueOf(1);
                return;
            case 2:
                this.ivMode.setImageResource(R.drawable.ic_tool_profile_normal);
                this.audioValue = Integer.valueOf(2);
                return;
            default:
                return;
        }
    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        ivBrightness = (ImageView) findViewById(R.id.iv_brightness);
        ivMode = (ImageView) findViewById(R.id.iv_mode);
        ivRoate = (ImageView) findViewById(R.id.iv_rotate);
        ivWifi = (ImageView) findViewById(R.id.iv_wifi);
        ivBluetooth = (ImageView) findViewById(R.id.iv_bluetooth);

        tvOptimize = (TextView) findViewById(R.id.tv_optimize);
        tvTemperate = (TextView) findViewById(R.id.tv_temperate);
        tvVoltage = (TextView) findViewById(R.id.tv_voltage);
        tvLevel = (TextView) findViewById(R.id.tv_level);
        tvAlert = (TextView) findViewById(R.id.tv_alert);

        llOptimizeCount = (LinearLayout) findViewById(R.id.ll_optimize_count);
        ivTimeout = (ImageView) findViewById(R.id.iv_timeout);

        llShare = (LinearLayout) findViewById(R.id.ll_share);

        arcCarView = (CardView) findViewById(R.id.arc_cardView);
        toolsCardView = (CardView) findViewById(R.id.tools_cardView);
        tempCardView = (CardView) findViewById(R.id.temp_cardView);
        feedCardView = (CardView) findViewById(R.id.feed_cardView);
        share_cardView = (CardView) findViewById(R.id.share_cardView);


        arcCarView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        toolsCardView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        tempCardView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        feedCardView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        share_cardView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        arcCarView.setCardElevation(0.0f);
        toolsCardView.setCardElevation(0.0f);
        tempCardView.setCardElevation(0.0f);
        feedCardView.setCardElevation(0.0f);
        share_cardView.setCardElevation(0.0f);


        initEvent();
    }

    private void initEvent() {
        ivBrightness.setOnClickListener(this);
        ivRoate.setOnClickListener(this);
        ivMode.setOnClickListener(this);
        ivWifi.setOnClickListener(this);
        ivBluetooth.setOnClickListener(this);

        tvOptimize.setOnClickListener(this);
        ivTimeout.setOnClickListener(this);
        tempCardView.setOnClickListener(this);

        llShare.setOnClickListener(this);
        llOptimizeCount.setOnClickListener(this);

        feedCardView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_wifi:
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                if (wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                    this.ivWifi.setImageResource(R.drawable.ic_tool_wifi_off);
                    return;
                }
                wifiManager.setWifiEnabled(true);
                this.ivWifi.setImageResource(R.drawable.ic_tool_wifi_on);
                break;
            case R.id.iv_bluetooth:
                defaultAdapter = BluetoothAdapter.getDefaultAdapter();
                if (defaultAdapter != null) {
                    if (defaultAdapter.isEnabled()) {
//                        this.ivBluetooth.setImageResource(R.drawable.ic_tool_bluetooth_off);
                        defaultAdapter.disable();
                        return;
                    }
//                    this.ivBluetooth.setImageResource(R.drawable.ic_tool_bluetooth_on);
                    defaultAdapter.enable();
                }
                break;
            case R.id.iv_brightness:
                WindowManager.LayoutParams attributes;
                if (this.brightnessValue > 20) {
                    Settings.System.putInt(getContentResolver(), "screen_brightness", 20);
                    attributes = this.window.getAttributes();
                    attributes.screenBrightness = 20.0f;
                    this.window.setAttributes(attributes);
                    this.brightnessValue = 20;
                    ivBrightness.setImageResource(R.drawable.ic_tool_brightness_off);
                    return;
                }
                Settings.System.putInt(getContentResolver(), "screen_brightness", 254);
                attributes = this.window.getAttributes();
                attributes.screenBrightness = 254.0f;
                this.window.setAttributes(attributes);
                ivBrightness.setImageResource(R.drawable.ic_tool_brightness_on);

                this.brightnessValue = 254;

                break;
            case R.id.iv_mode:
                audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                switch (this.audioManager.getRingerMode()) {
                    case 2:
                        this.audioManager.setRingerMode(0);
                        ivMode.setImageResource(R.drawable.ic_tool_profile_silent);
                        return;
                    case 0:
                        this.audioManager.setRingerMode(1);
                        ivMode.setImageResource(R.drawable.ic_tool_profile_vibrate);
                        return;
                    case 1:
                        this.audioManager.setRingerMode(2);
                        ivMode.setImageResource(R.drawable.ic_tool_profile_normal);
                        return;
                }
                break;
            case R.id.iv_rotate:
                Log.d("wwq", "rotateValue: " + rotateValue);
                if (rotateValue == 1) {
                    Settings.System.putInt(getContentResolver(), "accelerometer_rotation", 0);
                    ivRoate.setImageResource(R.drawable.ic_tool_rotate_portiat);
                    rotateValue = 0;
                    return;
                }
                Settings.System.putInt(getContentResolver(), "accelerometer_rotation", 1);
                ivRoate.setImageResource(R.drawable.ic_tool_rotate_autorotate);
                rotateValue = 1;
                break;

            case R.id.iv_timeout:
                if (this.timeoutValue == 10000) {
                    this.ivTimeout.setImageResource(R.drawable.ic_tool_timeout_twenty);
                    resetTimeout(1);
                    this.timeoutValue = 20000;
                    return;
                } else if (this.timeoutValue == 20000) {
                    this.ivTimeout.setImageResource(R.drawable.ic_tool_timeout_thirty);
                    resetTimeout(2);
                    this.timeoutValue = 30000;
                    return;
                } else if (this.timeoutValue == 30000) {
                    this.ivTimeout.setImageResource(R.drawable.ic_tool_timeout_fourty);
                    resetTimeout(3);
                    this.timeoutValue = 40000;
                    return;
                } else if (this.timeoutValue == 40000) {
                    this.ivTimeout.setImageResource(R.drawable.ic_tool_timeout_ten);
                    resetTimeout(0);
                    this.timeoutValue = 10000;
                    return;
                } else {
                    this.ivTimeout.setImageResource(R.drawable.ic_tool_timeout_fourty);
                    resetTimeout(3);
                    this.timeoutValue = 40000;
                    return;
                }
                // System.putInt(getContentResolver(), "screen_off_timeout", i2);

            case R.id.tv_optimize:
                optimizeBattery();
                break;
            case R.id.ll_share:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.TEXT", "Checkout" + " " + getResources().getString(R.string.app_name) + ", " + "the free app for save your battery with" + " " + getResources().getString(R.string.app_name) + ". https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(intent, "share" + " " + getResources().getString(R.string.app_name)));

                break;
            case R.id.temp_cardView:
                startActivity(new Intent(this, BatterDetailActivity.class));
                break;
            case R.id.feed_cardView:
                startActivity(new Intent(this, FastCharger.class));
                break;
            case R.id.ll_optimize_count:
                Intent intent1 = new Intent(this, CloseAllTools.class);
                intent1.putExtra("SetValue", 0);
                startActivity(intent1);
                break;
        }
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
        Settings.System.putInt(getContentResolver(), "screen_off_timeout", i2);
    }

    private void dismissAlpha(final View view) {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0).setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setAlpha(value);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
                translateView();
            }
        });
        valueAnimator.start();
    }

    private void translateView() {
        float v = Utils.dp2px(getResources(), 3);
        ObjectAnimator animator = ObjectAnimator.ofFloat(llOptimizeCount, "translationY", v);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.Setting:
                Intent intent = new Intent(getApplicationContext(), SettingPrefrence.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return false;
    }

    BroadcastReceiver batterReceiver;

    private void initBatteryData() {
        this.batterReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int j = -1;
                int k = intent.getIntExtra("level", -1);
                int m = intent.getIntExtra("scale", -1);
                int i = j;
                if (k >= 0) {
                    i = j;
                    if (m > 0) {
                        i = k * 100 / m;
                        Log.e("%", "" + i);
                    }
                }
                tvTemperate.setText((intent.getIntExtra("temperature", 0) / 10) + Character.toString('°') + " C");
                tvVoltage.setText((((float) intent.getIntExtra("voltage", 0)) / 1000.0f) + Character.toString('°') + " V");
                tvLevel.setText(Integer.toString(intent.getIntExtra("level", 0)));
                final Timer timer = new Timer();
                final int finalI = i;
                timer.schedule(new TimerTask() {
                                   public void run() {
                                       MainActivity.this.runOnUiThread(new Runnable() {
                                           public void run() {
                                               if (arc_progress.getProgress() == finalI) {
                                                   arc_progress.setProgress(finalI);
                                                   arc_progress.setBottomText(MainActivity.this.getResources().getString(R.string.main_battery));
                                                   timer.cancel();
                                                   return;
                                               }
                                               arc_progress.setProgress(arc_progress.getProgress() + 1);
                                               arc_progress.setBottomText(getResources().getString(R.string.main_battery));
                                           }
                                       });
                                   }
                               }
                        , 1000L, i);
            }
        };
        IntentFilter localIntentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        registerReceiver(this.batterReceiver, localIntentFilter);
    }

    private AlertDialog.Builder builder;

    private void initExitDialog() {
        this.builder = new AlertDialog.Builder(this);
        this.builder.setTitle(getResources().getString(R.string.like_this_app));
        this.builder.setMessage(getResources().getString(R.string.main_rate_summary));
        this.builder.setPositiveButton(getResources().getString(R.string.rate_5_star), new PositiveListener(this));
        this.builder.setNegativeButton(getResources().getString(R.string.exit), new NegativeListener(this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            builder.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class PositiveListener implements DialogInterface.OnClickListener {
        final /* synthetic */ MainActivity activity;

        PositiveListener(MainActivity mainActivity) {
            this.activity = mainActivity;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            try {
                this.activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + this.activity.getPackageName())));
            } catch (ActivityNotFoundException e) {
                this.activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + this.activity.getPackageName())));
            }
        }
    }


    class NegativeListener implements DialogInterface.OnClickListener {
        final /* synthetic */ MainActivity activity;

        NegativeListener(MainActivity mainActivity) {
            this.activity = mainActivity;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            audioManager = (AudioManager) this.activity.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(audioValue);
            resetTimeout(3);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.wifiReceiver);
        unregisterReceiver(this.batterReceiver);
    }

    private WifiReceiver wifiReceiver;

    private void registerWifiReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(wifiReceiver, filter);
    }

    private class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (!TextUtils.isEmpty(action) && action.equalsIgnoreCase(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                    switch (wifiState) {

                        case WifiManager.WIFI_STATE_DISABLING:
                            ivWifi.setImageResource(R.drawable.ic_tool_wifi_off);
                            break;
                        case WifiManager.WIFI_STATE_ENABLING:
                            ivWifi.setImageResource(R.drawable.ic_tool_wifi_on);
                            break;
                    }
//                    else if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
//                        //已经关闭
//                    } else if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
//                        //已经打开
//                    }
                } else if (!TextUtils.isEmpty(action) && action.equalsIgnoreCase(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            ivBluetooth.setImageResource(R.drawable.ic_tool_bluetooth_on);
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            ivBluetooth.setImageResource(R.drawable.ic_tool_bluetooth_off);
                            break;

                    }
                }

            }
        }
    }


    private void optimizeBattery() {
        List<ApplicationInfo> installedApplications = getPackageManager().getInstalledApplications(0);
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ApplicationInfo applicationInfo : installedApplications) {
            Log.e("wwq", applicationInfo.packageName + "");
            if ((applicationInfo.flags & 1) != 1) {
                activityManager.killBackgroundProcesses(applicationInfo.packageName);
            }
        }
        ((WifiManager) getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(false);
        this.ivWifi.setImageResource(R.drawable.ic_tool_wifi_off);
        this.defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.defaultAdapter != null && this.defaultAdapter.isEnabled()) {
            this.defaultAdapter.disable();
        }
        if (this.brightnessValue > 20) {
            Settings.System.putInt(getContentResolver(), "screen_brightness", 20);
            WindowManager.LayoutParams attributes = this.window.getAttributes();
            attributes.screenBrightness = 20.0f;
            this.window.setAttributes(attributes);
            this.ivBrightness.setImageResource(R.drawable.ic_tool_brightness_off);
            this.brightnessValue = 20;
        }
        Settings.System.putInt(getContentResolver(), "accelerometer_rotation", 0);
        this.ivRoate.setImageResource(R.drawable.ic_tool_rotate_portiat);
        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        this.audioManager.setRingerMode(0);
        this.ivMode.setImageResource(R.drawable.ic_tool_profile_silent);
        dismissAlpha(tvOptimize);
    }

    public void initAlertCount() {
        this.alertCount = Integer.valueOf(0);
        if (Boolean.valueOf(((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps")).booleanValue()) {
            this.alertCount = Integer.valueOf(this.alertCount.intValue() + 1);
        }
        Log.d("wwq", "isAir:  " + !Utils.isAirPlaneOpend(getApplicationContext()));
        if (!Utils.isAirPlaneOpend(getApplicationContext())) {
            this.alertCount = Integer.valueOf(this.alertCount.intValue() + 1);
            if (Utils.isMobleDataOpend().booleanValue()) {
                this.alertCount = Integer.valueOf(this.alertCount.intValue() + 1);
            }
        }
        this.tvAlert.setText(String.valueOf(this.alertCount));
        if (this.alertCount.intValue() == 0) {
            this.llOptimizeCount.setVisibility(View.GONE);
        }
    }


}
