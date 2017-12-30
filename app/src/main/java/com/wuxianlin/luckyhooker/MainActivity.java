package com.wuxianlin.luckyhooker;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_luckyhooker);
        }
    }
}
