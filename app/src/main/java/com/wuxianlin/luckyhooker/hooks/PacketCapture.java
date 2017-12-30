package com.wuxianlin.luckyhooker.hooks;

import android.view.View;
import android.widget.RelativeLayout;

import com.wuxianlin.luckyhooker.Hook;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2016/1/2.
 */

public class PacketCapture implements Hook {

    private static final String hookPackageName = "app.greyshirts.sslcapture";

    @Override
    public boolean canHook(String packageName) {
        return hookPackageName.equals(packageName);
    }

    @Override
    public void startHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start Hook PacketCapture");
        XposedHelpers.findAndHookMethod("ui.PacketActivity", lpparam.classLoader, "getMInterstitialAd", XC_MethodReplacement.returnConstant(null));
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        XposedBridge.log("start Hook PacketCapture");
        resparam.res.hookLayout(hookPackageName, "layout", "frag_ad", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
                RelativeLayout frag = (RelativeLayout) liparam.view;
                View adFragment = frag.findViewById(liparam.res.getIdentifier("adView", "id", hookPackageName));
                frag.removeView(adFragment);
            }
        });
    }

}
