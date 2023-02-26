package com.wuxianlin.luckyhooker.hooks;

import android.content.Context;

import com.wuxianlin.luckyhooker.Hook;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2023/2/26.
 */

public class NetworkCapture implements Hook {

    public static final String hookPackageName = "com.minhui.networkcapture";

    @Override
    public boolean canHook(String packageName) {
        return hookPackageName.equals(packageName);
    }

    @Override
    public void startHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start Hook Network Capture");
        XposedHelpers.findAndHookMethod("com.minhui.networkcapture.utils.ContextUtil", lpparam.classLoader, "isProVersion", Context.class, XC_MethodReplacement.returnConstant(true));
        XposedHelpers.findAndHookMethod("com.minhui.networkcapture.utils.ContextUtil", lpparam.classLoader, "hasRegister", Context.class, XC_MethodReplacement.returnConstant(true));
        XposedHelpers.findAndHookMethod("com.minhui.networkcapture.utils.ContextUtil", lpparam.classLoader, "isNoGooglePlayNoAds", Context.class, XC_MethodReplacement.returnConstant(true));

    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }
}
