package com.wuxianlin.luckyhooker.hooks;

import android.content.Context;
import android.os.Build;

import com.wuxianlin.luckyhooker.Hook;
import com.wuxianlin.luckyhooker.HookUtils;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2017/9/25.
 */

public class MgTv implements Hook {

    private static final String hookPackageName = "com.hunantv.imgo.activity";

    @Override
    public boolean canHook(String packageName) {
        return hookPackageName.equals(packageName);
    }

    @Override
    public void startHook(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            XposedHelpers.findAndHookMethod("com.mgtv.ui.ImgoApplication", lpparam.classLoader, "attachBaseContext", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    doHook(lpparam);
                }
            });
        } else
            doHook(lpparam);
    }

    private void doHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        int versionCode = HookUtils.getPackageVersionCode(hookPackageName);
        XposedBridge.log("start Hook MgTV:"+versionCode);

        String mgmiConfigManager = versionCode > 556100 ? "com.mgmi.platform.a" : "com.mgmi.platform.ConfigManager";
        String getAdHost = versionCode > 556100 ? "h" : "getAdHost";
        String mgmiOfflineAdMananger = versionCode > 556100 ? "com.mgmi.d.a" : "com.mgmi.offline.OfflineAdMananger";
        String getOfflineVast = versionCode > 556100 ? "a" : "getOfflineVast";
        String mvpVodPlayerView = versionCode > 556100 ? "com.hunantv.player.vod.mvp.VodPlayerView" : "com.mgtv.ui.play.vod.mvp.VodPlayerView";
        String showJustLook = versionCode > 556100 ? "showJustLookLayout" : "showJustLookPanel";
        String VodPlayerModel = versionCode > 556100 ? "com.hunantv.player.vod.mvp.VodPlayerModel" : "com.mgtv.ui.play.vod.mvp.VodPlayerModel";

        XposedHelpers.findAndHookMethod(mgmiConfigManager, lpparam.classLoader, getAdHost, XC_MethodReplacement.returnConstant("http://127.0.0.1"));
        XposedHelpers.findAndHookMethod(mgmiOfflineAdMananger, lpparam.classLoader, getOfflineVast, int.class, XC_MethodReplacement.returnConstant(null));
        XposedHelpers.findAndHookMethod(mvpVodPlayerView, lpparam.classLoader, showJustLook, XC_MethodReplacement.returnConstant(null));
        XposedHelpers.findAndHookMethod(mvpVodPlayerView, lpparam.classLoader, "showJustLookRemind", XC_MethodReplacement.returnConstant(null));
        XposedHelpers.findAndHookMethod(VodPlayerModel, lpparam.classLoader, "getVideoUrl", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String url = (String) param.getResult();
                if (url.endsWith("m3u8")) return;
                try {
                    url = url.replace(url.substring(0, url.indexOf("//") + 2), "");
                    String m3u8name = url.substring(url.indexOf("/"), url.indexOf("m3u8")+4);
                    String mp4name = m3u8name.substring(0, m3u8name.lastIndexOf("/"));
                    String fidname = mp4name.substring(mp4name.lastIndexOf("/") + 1, mp4name.indexOf("_", mp4name.lastIndexOf("/")));
                    List domains = (List) XposedHelpers.callMethod(param.thisObject, "getVideoDomains");
                    param.setResult(domains.get(0) + "/vod.do?fmt=4&pno=1021&fid=" + fidname + "&file="+m3u8name);
                } catch (Exception e) {
                    XposedBridge.log(e.toString());
                }
            }
        });
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}
