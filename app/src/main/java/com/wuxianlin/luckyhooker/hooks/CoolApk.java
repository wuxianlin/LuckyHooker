package com.wuxianlin.luckyhooker.hooks;

import android.app.Application;
import android.content.Context;

import com.wuxianlin.luckyhooker.Hook;

import java.io.File;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class CoolApk implements Hook {

    public static final String hookPackageName = "com.coolapk.market";

    @Override
    public boolean canHook(String packageName) {
        return hookPackageName.equals(packageName);
    }

    @Override
    public void startHook(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start Hook CoolApk:" + lpparam.packageName);
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                ClassLoader classLoader = ((Context) param.args[0]).getClassLoader();
                try {
                    XposedHelpers.findAndHookMethod("com.qq.e.comm.managers.GDTAdSdk", classLoader, "initWithoutStart", Context.class, String.class, XC_MethodReplacement.returnConstant(null));
                } catch (Throwable t) {
                    XposedBridge.log(t.toString());
                }
                try {
                    XposedHelpers.findAndHookMethod("com.bytedance.sdk.openadsdk.TTAdSdk", classLoader, "init", Context.class, "com.bytedance.sdk.openadsdk.TTAdConfig", XC_MethodReplacement.returnConstant(false));
                } catch (Throwable t) {
                    XposedBridge.log(t.toString());
                }
                try {
                    XposedHelpers.findAndHookMethod("com.bytedance.sdk.openadsdk.TTAdSdk", classLoader, "getAdManager", XC_MethodReplacement.returnConstant(null));
                } catch (Throwable t) {
                    XposedBridge.log(t.toString());
                }
                try {
                    XposedHelpers.findAndHookMethod("com.bytedance.sdk.open.douyin.a", classLoader, "init", "com.bytedance.sdk.open.douyin.DouYinOpenConfig", XC_MethodReplacement.returnConstant(false));
                } catch (Throwable t) {
                    XposedBridge.log(t.toString());
                }
                try {
                    XposedHelpers.findAndHookMethod("com.kwad.sdk.api.KsAdSDK", classLoader, "init", Context.class, "com.kwad.sdk.api.SdkConfig", XC_MethodReplacement.returnConstant(false));
                } catch (Throwable t) {
                    XposedBridge.log(t.toString());
                }
                try {
                    XposedHelpers.findAndHookMethod("com.umeng.commonsdk.UMConfigure", classLoader, "init", Context.class, String.class, String.class, int.class, String.class, XC_MethodReplacement.returnConstant(null));
                } catch (Throwable t) {
                    XposedBridge.log(t.toString());
                }
            }
        });
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
