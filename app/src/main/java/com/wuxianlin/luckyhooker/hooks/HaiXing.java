package com.wuxianlin.luckyhooker.hooks;

import com.wuxianlin.luckyhooker.Hook;

import java.io.File;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by guhb on 2018/1/18.
 */

public class HaiXing implements Hook {

    public static final String hookPackageName = "com.zhangyangjing.starfish";

    @Override
    public boolean canHook(String packageName) {
        return hookPackageName.equals(packageName);
    }

    @Override
    public void startHook(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start Hook HaiXing:" + lpparam.packageName);
        XposedHelpers.findAndHookMethod("android.app.SharedPreferencesImpl", lpparam.classLoader, "getString", String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                File mFile = (File)XposedHelpers.getObjectField(param.thisObject,"mFile");
                if (!mFile.getName().equals("app_settings.xml")) return;
                String key = (String)param.args[0];
                if (key.equals("account_type"))
                    param.setResult("VIP1");
            }
        });
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
