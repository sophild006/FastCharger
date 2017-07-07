package com.fast.charge.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class mPreference extends Preference {
    public mPreference(Context context) {
        super(context);
    }

    public mPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public mPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    protected void onBindView(View view) {
        super.onBindView(view);
    }
}