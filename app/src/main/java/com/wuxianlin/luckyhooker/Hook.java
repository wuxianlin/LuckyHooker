package com.wuxianlin.luckyhooker;

import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2016/1/2.
 */
public interface Hook {

    boolean canHook(String packageName);

    void startHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable;

    void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable;

}
