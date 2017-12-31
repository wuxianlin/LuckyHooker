package com.wuxianlin.luckyhooker;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.wuxianlin.luckyhooker.hooks.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment {

        private static final String KEY_APPS = "apps_settings";
        private static final String[] switchPackageNames = new String[]{
                KSWEB.hookPackageName,
                MgTv.hookPackageName,
                MxPlayer.hookPackageName,
                PacketCapture.hookPackageName,
                PpTv.hookPackageNames[0],
                PpTv.hookPackageNames[1],
                RootExplorer.hookPackageName,
                ShowJava.hookPackageName,
                SuperSU.hookPackageName
        };

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getPreferenceManager().setStorageDeviceProtected();
                SettingsManager.getInstance(getActivity());
            } else {
                getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);
            }
            addPreferencesFromResource(R.xml.pref_luckyhooker);
            PreferenceCategory Apps = (PreferenceCategory) findPreference(KEY_APPS);
            List<Map<String, String>> switchAppList = getSwitchAppList();
            for (Map map : switchAppList){
                String appName = map.get("appName").toString();
                String packageName = map.get("packageName").toString();
                SwitchPreference switchPreference = new SwitchPreference(getActivity());
                switchPreference.setKey(packageName);
                switchPreference.setDefaultValue(true);
                switchPreference.setTitle(appName);
                switchPreference.setSummary(packageName);
                Apps.addPreference(switchPreference);
            }
        }

        private List<Map<String, String>> getSwitchAppList(){
            List<Map<String, String>> switchAppList = new ArrayList();
            List<Map<String, String>> appList = getAppList();
            for (Map map : appList){
                if (Arrays.asList(switchPackageNames).contains(map.get("packageName")))
                    switchAppList.add(map);
            }
            return switchAppList;
        }

        private List<Map<String, String>> getAppList() {
            PackageManager pm = getActivity().getPackageManager();
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            List<Map<String, String>> appList = new ArrayList();
            for (PackageInfo pi : packages) {
                if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0 &&
                        (pi.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)==0){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("packageName", pi.packageName);
                    map.put("appName", pi.applicationInfo.loadLabel(pm).toString());
                    appList.add(map);
                }
            }
            return appList;
        }

    }
}
