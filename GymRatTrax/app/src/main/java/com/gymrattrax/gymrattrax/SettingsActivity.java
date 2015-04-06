package com.gymrattrax.gymrattrax;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class SettingsActivity extends ActionBarActivity {
    public static final String PREF_DATE_FORMAT    = "pref_date_format";
    public static final String PREF_NOTIFY_ENABLED = "pref_notify_enabled";
    public static final String PREF_NOTIFY_VIBRATE = "pref_notify_vibrate";
    public static final String PREF_NOTIFY_ADVANCE = "pref_notify_advance";
    public static final String PREF_NOTIFY_TONE    = "pref_notify_tone";
    public static final String PREF_NOTIFY_ONGOING = "pref_notify_ongoing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
