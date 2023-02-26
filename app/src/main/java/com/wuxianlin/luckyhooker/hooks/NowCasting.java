package com.wuxianlin.luckyhooker.hooks;

import com.wuxianlin.luckyhooker.Hook;
import com.wuxianlin.luckyhooker.HookUtils;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2023/2/26.
 */

public class NowCasting implements Hook {

    public static final String[] hookPackageNames = new String[]{"com.nowcasting.huawei",
            "com.nowcasting.activity"};

    @Override
    public boolean canHook(String packageName) {
        return Arrays.asList(hookPackageNames).contains(packageName);
    }

    @Override
    public void startHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start hook NowCasting");
        if (hookPackageNames[0].equals(lpparam.packageName)) {
            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.huawei.android.sdk.drm.Drm", lpparam.classLoader), "check", XC_MethodReplacement.returnConstant(null));
        }
        String[][] usedUserClasses = new String[][]{
                {"com.nowcasting.activity.APPCenterActivity", "setUser", "0"},
                {"com.nowcasting.view.MainActivityView", "setUser", "0"},
                {"com.nowcasting.view.MainTitleView", "setUser", "0"},
                {"com.nowcasting.viewmodel.APPCenterViewModel", "getBannerInfo", "0"},
                {"com.nowcasting.viewmodel.APPCenterViewModel", "handleBannerStrategy", "0"},
                {"com.nowcasting.viewmodel.APPCenterViewModel", "parseBannerInfo", "1"}
        };
        Method userParse = null;
        for (String[] usedUserClass : usedUserClasses) {
            for (Method method : HookUtils.findMethodWithName(usedUserClass[0], lpparam.classLoader,
                    usedUserClass[1])) {
                Class[] paramClasses = method.getParameterTypes();
                int userIndex = Integer.parseInt(usedUserClass[2]);
                if (paramClasses.length <= userIndex)
                    continue;
                Class userClass = paramClasses[userIndex];
                if (userClass == null)
                    continue;
                for (Method methodInUser : userClass.getDeclaredMethods()) {
                    Class[] paramUserClasses = methodInUser.getParameterTypes();
                    if (paramUserClasses.length == 1 &&
                            "org.json.JSONObject".equals(paramUserClasses[0].getName())) {
                        userParse = methodInUser;
                    }
                }
                if (userParse != null)
                    break;
            }
        }
        if (userParse == null)
            return;
        XposedBridge.log("finded user.parse(JsonObject) method:" +
                userParse.getDeclaringClass().getName() + "." + userParse.getName() + "(JsonObject)");
        XposedBridge.hookMethod(userParse, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                JSONObject jsonObject = (JSONObject) param.args[0];
                //XposedBridge.log(jsonObject.toString('\n'));
                long time = System.currentTimeMillis() / 1000 + 365 * 24 * 60 * 60;
                jsonObject.put("vip_expired_at", time);
                jsonObject.put("svip_expired_at", time);
                jsonObject.put("vip_type", "s");
                jsonObject.put("is_vip", true);
                param.args[0] = jsonObject;
            }
        });
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
