package com.wuxianlin.luckyhooker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.wuxianlin.luckyhooker.hooks.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !checkLSPosed(this)) {
            SettingsManager.getInstance(this).fixFolderPermissionsAsync();
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new PrefsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private static boolean checkResult = false;
    private static boolean checked = false;

    @SuppressLint("WorldReadableFiles")
    public static boolean checkLSPosed(Context context) {
        if (checked)
            return checkResult;
        try {
            context.getSharedPreferences(context.getPackageName() + "_preferences",
                    Context.MODE_WORLD_READABLE);
            checkResult = true;
            checked = true;
        } catch (SecurityException exception) {
            Toast.makeText(context, "LuckyHooker Settings may not work", Toast.LENGTH_LONG).show();
            checkResult = false;
            checked = true;
        }
        return checkResult;
    }

    public static class PrefsFragment extends PreferenceFragmentCompat {

        private static final String KEY_APPS = "apps_settings";
        private static final String[] switchPackageNames = new String[]{
                HaiXing.hookPackageName,
                KSWEB.hookPackageName,
                MxPlayer.hookPackageName,
                NetworkCapture.hookPackageName,
                NowCasting.hookPackageNames[0],
                NowCasting.hookPackageNames[1],
                PacketCapture.hookPackageName,
                PerfectPlayer.hookPackageName,
                QiYi.hookPackageNames[0],
                QiYi.hookPackageNames[1],
                QiYi.hookPackageNames[2],
                ShowJava.hookPackageName,
                SuperSU.hookPackageName,
        };

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.pref_luckyhooker, rootKey);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);
            } else if (!checkLSPosed(getContext())) {
                getPreferenceManager().setStorageDeviceProtected();
            }
            PreferenceCategory Apps = (PreferenceCategory) findPreference(KEY_APPS);
            List<Map<String, Object>> switchAppList = getSwitchAppList();
            for (Map map : switchAppList) {
                String appName = map.get("appName").toString();
                String packageName = map.get("packageName").toString();
                SwitchPreference switchPreference = new SwitchPreference(getContext());
                switchPreference.setKey(packageName);
                switchPreference.setDefaultValue(true);
                switchPreference.setIcon((Drawable) map.get("icon"));
                switchPreference.setTitle(appName);
                switchPreference.setSummary(packageName);
                Apps.addPreference(switchPreference);
            }
        }

        private List<Map<String, Object>> getSwitchAppList() {
            List<Map<String, Object>> switchAppList = new ArrayList();
            List<Map<String, Object>> appList = getAppList();
            for (Map map : appList) {
                if (Arrays.asList(switchPackageNames).contains(map.get("packageName")))
                    switchAppList.add(map);
            }
            return switchAppList;
        }

        private List<Map<String, Object>> getAppList() {
            PackageManager pm = getContext().getPackageManager();
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            List<Map<String, Object>> appList = new ArrayList();
            for (PackageInfo pi : packages) {
                if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 &&
                        (pi.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("packageName", pi.packageName);
                    map.put("appName", pi.applicationInfo.loadLabel(pm).toString());
                    map.put("icon", pi.applicationInfo.loadIcon(pm));
                    appList.add(map);
                }
            }
            return appList;
        }

    }
}
