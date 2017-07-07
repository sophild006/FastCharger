package com.fast.charge;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;

    protected void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(0);
        }
        super.onCreate(bundle);
        setContentView(R.layout.activity_about);
        this.toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 16908332:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}