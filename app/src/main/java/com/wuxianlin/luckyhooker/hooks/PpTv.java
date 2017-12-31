package com.wuxianlin.luckyhooker.hooks;

import android.content.Context;

import com.wuxianlin.luckyhooker.Hook;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2017/9/25.
 */

public class PpTv implements Hook {

    public static final String[] hookPackageNames = new String[] {"com.pplive.androidphone", "com.pplive.androidpad"};

    @Override
    public boolean canHook(String packageName) {
        return Arrays.asList(hookPackageNames).contains(packageName);
    }

    @Override
    public void startHook(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start Hook PpTv:" + lpparam.packageName);
        XposedHelpers.findAndHookMethod("com.pplive.streamingsdk.PPStreamingSDK", lpparam.classLoader, "setConfig", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String config = (String) param.args[0];
                config = config.replace("platform=android3","platform=atv")
                        .replace("platform=apad","platform=atv")
                        .replace("appplt=aph","appplt=atv")
                        .replace("appplt=apd","appplt=atv");
                param.args[0] = config;
            }
        });
        XposedHelpers.findAndHookMethod("com.pplive.android.data.account.AccountPreferences", lpparam.classLoader, "isVip", Context.class, XC_MethodReplacement.returnConstant(true));
        XposedHelpers.findAndHookMethod("com.pplive.android.data.account.AccountPreferences", lpparam.classLoader, "getVipValidDate", Context.class, XC_MethodReplacement.returnConstant("2099-12-31 23:59:59"));
        XposedHelpers.findAndHookMethod("com.pplive.android.data.account.AccountPreferences", lpparam.classLoader, "isSVip", Context.class, XC_MethodReplacement.returnConstant(true));
        XposedHelpers.findAndHookMethod("com.pplive.android.data.account.AccountPreferences", lpparam.classLoader, "getSVipValidDate", Context.class, XC_MethodReplacement.returnConstant("2099-12-31 23:59:59"));
        XposedHelpers.findAndHookMethod("com.pplive.android.data.account.AccountPreferences", lpparam.classLoader, "getUsername", Context.class, XC_MethodReplacement.returnConstant("Android-Hacker"));
        XposedHelpers.findAndHookMethod("com.pplive.android.data.account.AccountPreferences", lpparam.classLoader, "getLogin", Context.class, XC_MethodReplacement.returnConstant(true));
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
