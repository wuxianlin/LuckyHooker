package com.wuxianlin.luckyhooker.hooks;

import com.wuxianlin.luckyhooker.Hook;
import com.wuxianlin.luckyhooker.HookUtils;

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
        int versionCode = HookUtils.getPackageVersionCode(hookPackageName);
        if (versionCode <= 21002) {
            XposedHelpers.findAndHookMethod("com.njlabs.showjava.ui.BaseActivity", lpparam.classLoader, "get", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod("com.njlabs.showjava.ui.BaseActivity", lpparam.classLoader, "put", boolean.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    param.args[0] = true;
                }
            });
        } else {
            //30406//30502//30604--com.njlabs.showjava.activities.a.u()Z->com.njlabs.showjava.utils.d.b.d()Z
            //30300//30200//30107--com.njlabs.showjava.activities.a.s()Z->com.njlabs.showjava.utils.c.b.d()Z
            /*XposedHelpers.findAndHookMethod("com.njlabs.showjava.activities.a",
                    lpparam.classLoader, versionCode>30502?"u":"s",
                    XC_MethodReplacement.returnConstant(true));*/
            XposedHelpers.findAndHookMethod(versionCode >= 30406 ?
                            "com.njlabs.showjava.utils.d.b" :
                            "com.njlabs.showjava.utils.c.b",
                    lpparam.classLoader, "d", XC_MethodReplacement.returnConstant(true));

        }
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
