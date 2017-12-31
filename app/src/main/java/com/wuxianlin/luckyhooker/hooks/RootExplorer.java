package com.wuxianlin.luckyhooker.hooks;

import com.wuxianlin.luckyhooker.Hook;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2016/2/26.
 */

public class RootExplorer implements Hook {

    public static final String hookPackageName = "com.speedsoftware.rootexplorer";

    @Override
    public boolean canHook(String packageName) {
        return hookPackageName.equals(packageName);
    }

    @Override
    public void startHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start Hook RootExplorer");
        XposedHelpers.findAndHookMethod("com.google.android.vending.licensing.LongTermCachePolicy", lpparam.classLoader, "allowAccess", XC_MethodReplacement.returnConstant(true));
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
