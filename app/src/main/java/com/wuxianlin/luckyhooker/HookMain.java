package com.wuxianlin.luckyhooker;

import android.os.Build;

import com.wuxianlin.luckyhooker.hooks.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 * Created by wuxianlin on 2016/1/2.
 */

public class HookMain implements IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {

    private Set<Hook> hooks = new HashSet<Hook>();

    public static XSharedPreferences prefs;
    public static final String PACKAGE_NAME = HookMain.class.getPackage().getName();
    public static String MODULE_PATH = null;
    private static final File prefsFileProt = new File("/data/user_de/0/" + PACKAGE_NAME + "/shared_prefs/" + PACKAGE_NAME + "_preferences.xml");

    public HookMain(){
        hooks.add(new KSWEB());
        hooks.add(new MgTv());
        hooks.add(new MxPlayer());
        hooks.add(new PacketCapture());
        hooks.add(new PerfectPlayer());
        hooks.add(new PpTv());
        hooks.add(new RootExplorer());
        hooks.add(new ShowJava());
        hooks.add(new SuperSU());
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            prefs = new XSharedPreferences(prefsFileProt);
        } else {
            prefs = new XSharedPreferences(PACKAGE_NAME);
        }
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        prefs.reload();
        for (Hook hook : hooks) {
            try {
                String packageName = resparam.packageName;
                if (hook.canHook(packageName) &&
                        prefs.getBoolean(packageName, true))
                    hook.startHook(resparam);
            } catch (Throwable t) {
                XposedBridge.log(t.toString());
            }
        }
    }

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        prefs.reload();
        for (Hook hook : hooks) {
            try {
                String packageName = lpparam.packageName;
                if (hook.canHook(packageName) &&
                        prefs.getBoolean(packageName, true))
                    hook.startHook(lpparam);
            } catch (Throwable t) {
                XposedBridge.log(t.toString());
            }
        }
    }
}
