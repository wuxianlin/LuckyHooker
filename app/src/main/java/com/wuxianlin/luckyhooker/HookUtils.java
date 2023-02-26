package com.wuxianlin.luckyhooker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by wuxianlin on 2016/1/2.
 */

public class HookUtils {

    public static Context getContext() {
        Object thread = XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread");
        if (thread == null) {
            return null;
        }
        Context context = (Context) XposedHelpers.callMethod(thread, "getSystemContext");
        if (context == null) {
            return null;
        }
        return context;
    }

    public static int getPackageVersionCode(String packageName) {
        Context context = getContext();
        if (context == null)
            return 0;
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null)
            return 0;
        try {
            return packageManager.getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<Method> findMethodWithName(String className, ClassLoader classLoader, String methodName) {
        List<Method> methods = new ArrayList<>();
        if (TextUtils.isEmpty(className) || TextUtils.isEmpty(methodName))
            return methods;
        Class hookClass = XposedHelpers.findClassIfExists(className, classLoader);
        if (hookClass == null)
            return methods;
        for (Method method : hookClass.getDeclaredMethods()) {
            if (methodName.equals(method.getName())) {
                methods.add(method);
            }
        }
        return methods;
    }

    public static int getMyUserId() {
        try {
            int uid = android.os.Process.myUid();
            Integer userId = (Integer) getGetUserIdMethod().invoke(null, uid);
            return userId;
        } catch (NoSuchMethodException ignored) {
        } catch (IllegalAccessException ignored) {
        } catch (InvocationTargetException ignored) {
        }
        return 0;
    }

    @Nullable
    private static Method sGetUserIdMethod;

    @SuppressLint("DiscouragedPrivateApi")
    private static Method getGetUserIdMethod() throws NoSuchMethodException {
        if (sGetUserIdMethod == null) {
            sGetUserIdMethod = UserHandle.class.getDeclaredMethod("getUserId", int.class);
            sGetUserIdMethod.setAccessible(true);
        }
        return sGetUserIdMethod;
    }

}
