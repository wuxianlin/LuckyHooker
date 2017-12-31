package com.wuxianlin.luckyhooker.hooks;

import android.content.ContextWrapper;
import android.os.Build;

import com.wuxianlin.luckyhooker.Hook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2016/1/2.
 */

public class MxPlayer implements Hook {

    public static final String hookPackageName = "com.mxtech.videoplayer.pro";

    @Override
    public boolean canHook(String packageName) {
        return hookPackageName.equals(packageName);
    }

    @Override
    public void startHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start Hook MxPlayer Pro");
        XposedHelpers.findAndHookMethod("com.mxtech.app.Apps", lpparam.classLoader, Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? "c" : "get", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                int input = (int)param.args[0];
                if (input==1 || input==2)
                    param.setResult(1L);
            }
        });
        XposedHelpers.findAndHookMethod(ContextWrapper.class, "checkPermission", String.class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String perm = (String)param.args[0];
                if (perm.equals("com.android.vending.CHECK_LICENSE")
                        || perm.equals("android.permission.GET_ACCOUNTS"))
                    param.setResult(0);
            }
        });
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
