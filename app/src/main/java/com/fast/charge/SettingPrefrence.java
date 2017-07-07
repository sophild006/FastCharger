package com.fast.charge;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class SettingPrefrence extends AppCompatActivity {
    Toolbar f6253a;

    protected void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(0);
        }
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_setting);
        setTheme(R.style.Theme_DarkText);
        this.f6253a = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(this.f6253a);
        getSupportActionBar().setTitle(getResources().getString(R.string.setting_title));
        this.f6253a.setTitleTextColor(-1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new C2120b()).commit();
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