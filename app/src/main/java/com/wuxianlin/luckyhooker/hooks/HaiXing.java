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
        XposedHelpers.findAndHookMethod("com.zhangyangjing.starfish.util.f", lpparam.classLoader, "m", Context.class, XC_MethodReplacement.returnConstant("VIP1"));
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
