package com.wuxianlin.luckyhooker.hooks;

import android.content.Context;
import android.os.Build;

import com.wuxianlin.luckyhooker.Hook;

import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2018/1/3.
 */

public class KuWo implements Hook {

    public static final String[] hookPackageNames = new String[] {"cn.kuwo.player", "cn.kuwo.kwmusichd"};

    @Override
    public boolean canHook(String packageName) {
        return Arrays.asList(hookPackageNames).contains(packageName);
    }

    @Override
    public void startHook(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start Hook KuWo:" + lpparam.packageName);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            XposedHelpers.findAndHookMethod("android.support.multidex.MultiDexApplication", lpparam.classLoader, "attachBaseContext", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    doHook(lpparam);
                }
            });
        } else
            doHook(lpparam);
    }

    private void doHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedHelpers.findAndHookMethod("cn.kuwo.mod.vipreal.VipRealInfo", lpparam.classLoader, "getState", XC_MethodReplacement.returnConstant(1));
        XposedHelpers.findAndHookMethod("cn.kuwo.mod.vipreal.VipRealInfo", lpparam.classLoader, "getLeftDays", XC_MethodReplacement.returnConstant(365L));
        XposedHelpers.findAndHookMethod("cn.kuwo.mod.vipnew.ConsumptionQueryUtil", lpparam.classLoader, "hasBought", long.class, List.class, XC_MethodReplacement.returnConstant(true));
        XposedHelpers.findAndHookMethod("cn.kuwo.mod.mobilead.KuwoAdUrl$AdUrlDef", lpparam.classLoader, "getUrl", String.class, XC_MethodReplacement.returnConstant(""));
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
