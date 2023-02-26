package com.wuxianlin.luckyhooker.hooks;

import com.wuxianlin.luckyhooker.Hook;

import java.util.Arrays;
import java.util.Properties;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2018/1/3.
 */

public class QiYi implements Hook {

    public static final String[] hookPackageNames = new String[]{"com.qiyi.video", "com.qiyi.video.pad", "tv.pps.mobile"};

    @Override
    public boolean canHook(String packageName) {
        return Arrays.asList(hookPackageNames).contains(packageName);
    }

    @Override
    public void startHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start Hook IQiYi");
        XposedBridge.hookAllMethods(Properties.class, "getProperty", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if ("qiyi.export.key".equals(param.args[0])) {
                    param.setResult("59e36a5e70e4c4efc6fcbc4db7ea59c1");
                    //param.setResult("20485102b09bfb5842bf370463bed900");
                    //param.setResult("200852026c791ac910651df45b27da50");
                }
            }
        });
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
