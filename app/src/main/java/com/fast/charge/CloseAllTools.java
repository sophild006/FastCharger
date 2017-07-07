package com.fast.charge;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings.System;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.fast.charge.util.Utils;

import java.lang.reflect.Method;

public class CloseAllTools extends AppCompatActivity implements OnClickListener {
    CardView cdLocationView;
    CardView cdAirplaneView;//AroplaneCardView
    CardView cdMobelDataView;//MoblieDataCardView
    Toolbar toolbar;
    Button btnTurnOff;//LocationTurnOff
    Button btnAirplaneTurnOn;//AroplaneTurnOn
    Button btnMobleTurnOff;//MoblieDataTurnOff
    LocationManager locationManager;
    Boolean isGpsOpened;
    TextView tvNoIssue;
    Integer SetValue;
    TextView tvLoacationTitle;//TxtTitleLocation
    TextView tvLocationDesc;//txtDicLocation
    TextView tvAirplaneTitle;//TxtTitleAroplane
    TextView tvAirplaneDesc;//txtDicAroplane
    TextView tvMobleDataTitle;//TxtTitleMobileData
    TextView tvMobleDataDesc;//txtDicMobileData

    private static boolean m9340a(Context context) {
        return System.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 0;
    }

    private void initCardView() {
        this.cdLocationView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        this.cdAirplaneView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        this.cdMobelDataView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.cardcolor));
        this.cdLocationView.setCardElevation(0.0f);
        this.cdAirplaneView.setCardElevation(0.0f);
        this.cdMobelDataView.setCardElevation(0.0f);
    }

    private void initAlertCount() {
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.isGpsOpened = Boolean.valueOf(this.locationManager.isProviderEnabled("gps"));
        if (!this.isGpsOpened.booleanValue()) {
            this.cdLocationView.setVisibility(View.GONE);
        }
        if (Utils.isAirPlaneOpend(getApplicationContext())) {
            this.cdAirplaneView.setVisibility(View.GONE);
            this.cdMobelDataView.setVisibility(View.GONE);
        }else{
            if (!Utils.isMobleDataOpend().booleanValue()) {
                this.cdMobelDataView.setVisibility(View.GONE);
            }
        }
        if (this.cdLocationView.getVisibility() == View.GONE && this.cdAirplaneView.getVisibility() == View.GONE && this.cdMobelDataView.getVisibility() == View.GONE) {
            this.tvNoIssue.setVisibility(View.VISIBLE);
        }
    }

    private void startSeeting() {
        Intent intent = new Intent("android.settings.SETTINGS");
        intent.addFlags(268435456);
        startActivity(intent);
    }

    private void startMobleSet() {
        startActivity(new Intent("android.settings.AIRPLANE_MODE_SETTINGS"));
    }

    private void startLocationSet() {
        startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.LocationCardView:
                startLocationSet();
                return;
            case R.id.LocationTurnOff:
                startLocationSet();
                return;
            case R.id.AroplaneCardView:
                startMobleSet();
                return;
            case R.id.AroplaneTurnOn:
                startMobleSet();
                return;
            case R.id.MoblieDataCardView:
                startSeeting();
                return;
            case R.id.MoblieDataTurnOff:
                startSeeting();
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        this.SetValue = Integer.valueOf(getIntent().getIntExtra("SetValue", 0));
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(0);
        }
        setContentView((int) R.layout.activity_close_all_tools);
        this.toolbar = (Toolbar) findViewById(R.id.tool_bar);
        this.toolbar.setTitleTextColor(-1);
        setSupportActionBar(this.toolbar);
        if (this.SetValue.intValue() == 0) {
            getSupportActionBar().setTitle(getResources().getString(R.string.close_tool_title_saver));
        } else {
            getSupportActionBar().setTitle(getResources().getString(R.string.close_tool_title_charger));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.cdLocationView = (CardView) findViewById(R.id.LocationCardView);
        this.cdAirplaneView = (CardView) findViewById(R.id.AroplaneCardView);
        this.cdMobelDataView = (CardView) findViewById(R.id.MoblieDataCardView);
        this.btnTurnOff = (Button) findViewById(R.id.LocationTurnOff);
        this.btnAirplaneTurnOn = (Button) findViewById(R.id.AroplaneTurnOn);
        this.btnMobleTurnOff = (Button) findViewById(R.id.MoblieDataTurnOff);
        this.tvNoIssue = (TextView) findViewById(R.id.txtNoMoreIssue);
        this.tvLoacationTitle = (TextView) findViewById(R.id.TxtTitleLocation);
        this.tvLocationDesc = (TextView) findViewById(R.id.txtDicLocation);
        this.tvAirplaneTitle = (TextView) findViewById(R.id.TxtTitleAroplane);
        this.tvAirplaneDesc = (TextView) findViewById(R.id.txtDicAroplane);
        this.tvMobleDataTitle = (TextView) findViewById(R.id.TxtTitleMobileData);
        this.tvMobleDataDesc = (TextView) findViewById(R.id.txtDicMobileData);
        this.cdLocationView.setOnClickListener(this);
        this.cdAirplaneView.setOnClickListener(this);
        this.cdMobelDataView.setOnClickListener(this);
        this.btnTurnOff.setOnClickListener(this);
        this.btnAirplaneTurnOn.setOnClickListener(this);
        this.btnMobleTurnOff.setOnClickListener(this);
        initCardView();
        initAlertCount();
        if (this.SetValue.intValue() == 0) {
            this.tvLoacationTitle.setText(getResources().getString(R.string.close_tool_saver_location_off));
            this.tvLocationDesc.setText(getResources().getString(R.string.close_tool_saver_location_off_desc));
            this.tvAirplaneTitle.setText(getResources().getString(R.string.close_tool_saver_airplane_on));
            this.tvAirplaneDesc.setText(getResources().getString(R.string.close_tool_saver_airplane_on_desc));
            this.tvMobleDataTitle.setText(getResources().getString(R.string.close_tool_saver_internet_off));
            this.tvMobleDataDesc.setText(getResources().getString(R.string.close_tool_saver_internet_off_desc));
            return;
        }
        this.tvLoacationTitle.setText(getResources().getString(R.string.close_tool_location_off));
        this.tvLocationDesc.setText(getResources().getString(R.string.close_tool_location_off_desc));
        this.tvAirplaneTitle.setText(getResources().getString(R.string.close_tool_airplane_on));
        this.tvAirplaneDesc.setText(getResources().getString(R.string.close_tool_airplane_on_desc));
        this.tvMobleDataTitle.setText(getResources().getString(R.string.close_tool_internet_off));
        this.tvMobleDataDesc.setText(getResources().getString(R.string.close_tool_internet_off_desc));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return false;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initAlertCount();
    }
}