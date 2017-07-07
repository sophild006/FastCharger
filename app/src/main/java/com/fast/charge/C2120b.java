package com.fast.charge;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class C2120b extends PreferenceFragment {
    Intent f6266a;

    class C21131 implements OnPreferenceClickListener {
        final /* synthetic */ C2120b f6258a;

        C21131(C2120b c2120b) {
            this.f6258a = c2120b;
        }

        public boolean onPreferenceClick(Preference preference) {
            this.f6258a.startActivity(new Intent(this.f6258a.getActivity().getApplicationContext(), LicenseActivity.class));
            return true;
        }
    }

    class C21142 implements OnPreferenceClickListener {
        final /* synthetic */ C2120b f6259a;

        C21142(C2120b c2120b) {
            this.f6259a = c2120b;
        }

        public boolean onPreferenceClick(Preference preference) {
            this.f6259a.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=pub:" + this.f6259a.getResources().getString(R.string.developer_name))));
            return true;
        }
    }

    class C21153 implements OnPreferenceClickListener {
        final /* synthetic */ C2120b f6260a;

        C21153(C2120b c2120b) {
            this.f6260a = c2120b;
        }

        public boolean onPreferenceClick(Preference preference) {
            try {
                this.f6260a.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + this.f6260a.getActivity().getPackageName())));
            } catch (ActivityNotFoundException e) {
                this.f6260a.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + this.f6260a.getActivity().getPackageName())));
            }
            return true;
        }
    }

    class C21164 implements OnPreferenceClickListener {
        final /* synthetic */ C2120b f6261a;

        C21164(C2120b c2120b) {
            this.f6261a = c2120b;
        }

        public boolean onPreferenceClick(Preference preference) {
            this.f6261a.f6266a = new Intent();
            this.f6261a.f6266a.setAction("android.intent.action.SEND");
            this.f6261a.f6266a.setType("text/plain");
            this.f6261a.f6266a.putExtra("android.intent.extra.TEXT", this.f6261a.getResources().getString(R.string.checkout) + " " + this.f6261a.getResources().getString(R.string.app_name) + ", " + this.f6261a.getResources().getString(R.string.checkout_1) + " " + this.f6261a.getResources().getString(R.string.app_name) + ". https://play.google.com/store/apps/details?id=" + this.f6261a.getActivity().getPackageName());
            this.f6261a.startActivity(Intent.createChooser(this.f6261a.f6266a, this.f6261a.getResources().getString(R.string.share) + " " + this.f6261a.getResources().getString(R.string.app_name)));
            return true;
        }
    }

    class C21175 implements OnPreferenceClickListener {
        final /* synthetic */ C2120b f6262a;

        C21175(C2120b c2120b) {
            this.f6262a = c2120b;
        }

        public boolean onPreferenceClick(Preference preference) {
            this.f6262a.f6266a = new Intent(this.f6262a.getActivity(), AboutActivity.class);
            this.f6262a.startActivity(this.f6262a.f6266a);
            return true;
        }
    }

    class C21197 implements OnPreferenceClickListener {
        final /* synthetic */ C2120b f6265a;

        C21197(C2120b c2120b) {
            this.f6265a = c2120b;
        }

        public boolean onPreferenceClick(Preference preference) {
            try {
                this.f6265a.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + this.f6265a.getActivity().getPackageName())));
            } catch (ActivityNotFoundException e) {
                this.f6265a.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + this.f6265a.getActivity().getPackageName())));
            }
            return true;
        }
    }

    public static String m9393a() {
        String str = Build.MANUFACTURER;
        String str2 = Build.MODEL;
        return str2.startsWith(str) ? C2120b.m9394a(str2) : C2120b.m9394a(str) + " " + str2;
    }

    private static String m9394a(String str) {
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

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.setting);
        PackageInfo packageInfo = null;
        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        final CharSequence charSequence = packageInfo.versionName;
        findPreference("Licence").setOnPreferenceClickListener(new C21131(this));
        findPreference("MoreApp").setOnPreferenceClickListener(new C21142(this));
        findPreference("RateUs").setOnPreferenceClickListener(new C21153(this));
        findPreference("ShareApp").setOnPreferenceClickListener(new C21164(this));
        findPreference("About").setOnPreferenceClickListener(new C21175(this));
        findPreference("FeedBack").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            C2120b f6264b;

            @Override
            public boolean onPreferenceClick(Preference preference) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                this.f6264b.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int i = displayMetrics.heightPixels;
                int i2 = displayMetrics.widthPixels;
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("message/rfc822");
                intent.putExtra("android.intent.extra.EMAIL", new String[]{this.f6264b.getResources().getString(R.string.contact_email)});
                intent.putExtra("android.intent.extra.SUBJECT", this.f6264b.getResources().getString(R.string.app_name) + charSequence);
                intent.putExtra("android.intent.extra.TEXT", "\n Device :" + C2120b.m9393a() + "\n SystemVersion:" + VERSION.SDK_INT + "\n Display Height  :" + i + "px\n Display Width  :" + i2 + "px\n\n " + this.f6264b.getResources().getString(R.string.email_placeholder) + " \n");
                this.f6264b.startActivity(Intent.createChooser(intent, this.f6264b.getResources().getString(R.string.send_email)));
                return true;
            }
        });
        findPreference("Update").setOnPreferenceClickListener(new C21197(this));
        findPreference("Version").setSummary(charSequence);
    }
}