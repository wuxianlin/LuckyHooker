package com.wuxianlin.luckyhooker;

import android.content.Context;
import android.content.pm.PackageManager;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by wuxianlin on 2016/1/2.
 */

public class HookUtils {

    public static Context getContext(){
        Object thread = XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread",null),"currentActivityThread");
        if (thread == null) {
            return null;
        }
        Context context = (Context) XposedHelpers.callMethod(thread,"getSystemContext");
        if (context == null) {
            return null;
        }
        return context;
    }

    public static int getPackageVersionCode(String packageName){
        Context context = getContext();
        if(context == null)
            return 0;
        PackageManager packageManager = context.getPackageManager();
        if(packageManager == null)
            return 0;
        try {
            return packageManager.getPackageInfo(packageName, 0).versionCode;
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return 0;
    }
}
