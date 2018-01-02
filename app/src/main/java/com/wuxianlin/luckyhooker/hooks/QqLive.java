package com.wuxianlin.luckyhooker.hooks;

import android.content.Context;
import android.os.Build;

import com.wuxianlin.luckyhooker.Hook;
import com.wuxianlin.luckyhooker.HookUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2017/9/25.
 */

public class QqLive implements Hook {

    public static final String hookPackageName = "com.tencent.qqlive";

    @Override
    public boolean canHook(String packageName) {
        return hookPackageName.equals(packageName);
    }

    @Override
    public void startHook(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        int versionCode = HookUtils.getPackageVersionCode(hookPackageName);
        XposedBridge.log("start Hook QqLive:" + versionCode);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && versionCode > 12708) {
            XposedHelpers.findAndHookMethod("com.tencent.qqlive.ona.base.QQLiveApplication", lpparam.classLoader, "attachBaseContext", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    doHook(lpparam);
                }
            });
        } else
            doHook(lpparam);
    }

    private void doHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedHelpers.findAndHookConstructor("com.tencent.qqlive.mediaplayer.config.MediaPlayerConfig$AdConfig", lpparam.classLoader, new XC_MethodHook() {
        	@Override
        	protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        		XposedHelpers.setBooleanField(param.thisObject, "use_ad", false);
        		XposedHelpers.setBooleanField(param.thisObject, "pre_ad_on", false);
        		XposedHelpers.setBooleanField(param.thisObject, "offline_video_use_ad", false);
        		XposedHelpers.setBooleanField(param.thisObject, "pause_use_ad", false);
        		XposedHelpers.setBooleanField(param.thisObject, "ivb_use_ad", false);
        		XposedHelpers.setBooleanField(param.thisObject, "super_ivb_use_ad", false);
        		XposedHelpers.setBooleanField(param.thisObject, "loop_ad_on", false);
        		XposedHelpers.setBooleanField(param.thisObject, "postroll_use_ad", false);
        		XposedHelpers.setBooleanField(param.thisObject, "mid_ad_on", false);
        	}
        });
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
