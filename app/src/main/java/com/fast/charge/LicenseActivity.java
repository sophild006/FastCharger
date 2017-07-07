package com.fast.charge;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class LicenseActivity extends AppCompatActivity {
    Toolbar f6203a;

    protected void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(0);
        }
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_license);
        this.f6203a = (Toolbar) findViewById(R.id.tool_bar);
        this.f6203a.setTitleTextColor(-1);
        setSupportActionBar(this.f6203a);
        getSupportActionBar().setTitle(getResources().getString(R.string.setting_license_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}