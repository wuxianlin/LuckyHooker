package com.wuxianlin.luckyhooker;

import com.wuxianlin.luckyhooker.hooks.*;

import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 * Created by wuxianlin on 2016/1/2.
 */

public class HookMain implements IXposedHookLoadPackage, IXposedHookInitPackageResources {

    private Set<Hook> hooks = new HashSet<Hook>();

    public HookMain(){
        hooks.add(new KSWEB());
        hooks.add(new MgTv());
        hooks.add(new MxPlayer());
        hooks.add(new PacketCapture());
        hooks.add(new PpTv());
        hooks.add(new RootExplorer());
        hooks.add(new ShowJava());
        hooks.add(new SuperSU());
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        for (Hook hook : hooks) {
            try {
                if (hook.canHook(resparam.packageName))
                    hook.startHook(resparam);
            } catch (Throwable t) {
                XposedBridge.log(t.toString());
            }
        }
    }

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        for (Hook hook : hooks) {
            try {
                if (hook.canHook(lpparam.packageName))
                    hook.startHook(lpparam);
            } catch (Throwable t) {
                XposedBridge.log(t.toString());
            }
        }
    }
}
