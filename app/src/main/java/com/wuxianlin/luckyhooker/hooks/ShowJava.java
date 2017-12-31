package com.wuxianlin.luckyhooker.hooks;

import com.wuxianlin.luckyhooker.Hook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2016/10/13.
 */

public class ShowJava implements Hook {

    public static final String hookPackageName = "com.njlabs.showjava";

    @Override
    public boolean canHook(String packageName) {
        return hookPackageName.equals(packageName);
    }

    @Override
    public void startHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start Hook ShowJava");
        XposedHelpers.findAndHookMethod("com.njlabs.showjava.ui.BaseActivity", lpparam.classLoader, "get", XC_MethodReplacement.returnConstant(true));
        XposedHelpers.findAndHookMethod("com.njlabs.showjava.ui.BaseActivity", lpparam.classLoader, "put", boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.args[0]=true;
            }
        });
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
