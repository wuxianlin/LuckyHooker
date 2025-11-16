package com.wuxianlin.luckyhooker.hooks;

import com.wuxianlin.luckyhooker.Hook;
import com.wuxianlin.luckyhooker.HookUtils;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2023/2/26.
 */

public class NowCasting implements Hook {

    public static final String[] hookPackageNames = new String[]{"com.nowcasting.huawei",
            "com.nowcasting.activity"};

    @Override
    public boolean canHook(String packageName) {
        return Arrays.asList(hookPackageNames).contains(packageName);
    }

    @Override
    public void startHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start hook NowCasting");
        if (hookPackageNames[0].equals(lpparam.packageName)) {
            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.huawei.android.sdk.drm.Drm", lpparam.classLoader), "check", XC_MethodReplacement.returnConstant(null));
        }
        XposedBridge.hookAllMethods(XposedHelpers.findClass("com.nowcasting.entity.UserInfo", lpparam.classLoader),"isVIP", XC_MethodReplacement.returnConstant(true));
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
