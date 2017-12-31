package com.wuxianlin.luckyhooker.hooks;

import com.wuxianlin.luckyhooker.Hook;

import java.io.File;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2017/12/31.
 */

public class WkCast implements Hook {

    public static final String hookPackageName = "com.wukongtv.wkcast";

    @Override
    public boolean canHook(String packageName) {
        return hookPackageName.equals(packageName);
    }

    @Override
    public void startHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start Hook WuKong Cast");
        XposedHelpers.findAndHookMethod("android.app.SharedPreferencesImpl", lpparam.classLoader, "getInt", String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                File mFile = (File)XposedHelpers.getObjectField(param.thisObject,"mFile");
                if (!mFile.getName().equals("preference.xml")) return;
                String key = (String)param.args[0];
                if (key.equals("TIPPED_AMOUNT"))
                    param.setResult(1);
            }
        });
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
