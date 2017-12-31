package com.wuxianlin.luckyhooker.hooks;

import android.content.Context;

import com.wuxianlin.luckyhooker.Hook;

import java.io.File;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2017/9/25.
 */

public class PerfectPlayer implements Hook {

    public static final String hookPackageName = "com.niklabs.pp";

    @Override
    public boolean canHook(String packageName) {
        return hookPackageName.equals(packageName);
    }

    @Override
    public void startHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start Hook PerfectPlayer");
        XposedHelpers.findAndHookMethod("android.app.SharedPreferencesImpl", lpparam.classLoader, "getBoolean", String.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                File mFile = (File)XposedHelpers.getObjectField(param.thisObject,"mFile");
                if (!mFile.getName().equals(hookPackageName + "_preferences.xml")) return;
                String key = (String)param.args[0];
                if (key.equals("pref_key_unlocked_full_version"))
                    param.setResult(true);
            }
        });
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}