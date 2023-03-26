package com.wuxianlin.luckyhooker.hooks;

import android.content.Context;
import android.os.Build;
import android.util.Base64;

import com.wuxianlin.luckyhooker.Hook;

import java.io.File;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HttpCanary implements Hook {

    public static final String[] hookPackageNames = new String[]{"com.guoshi.httpcanary.premium","com.guoshi.httpcanary"};

    @Override
    public boolean canHook(String packageName) {
        return Arrays.asList(hookPackageNames).contains(packageName);
    }

    @Override
    public void startHook(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start Hook HttpCanary:" + lpparam.packageName);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R)
            XposedHelpers.setStaticObjectField(Build.VERSION.class, "CODENAME", "R");
        boolean isPro = lpparam.packageName.endsWith(".premium");
        if(isPro)XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", lpparam.classLoader, "getInstallerPackageName", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String str = (String)param.args[0];
                if("com.guoshi.httpcanary.premium".equals(str)){
                    //XposedBridge.log("getInstallerPackageName");
                    param.setResult("com.android.vending");
                }
                //XposedBridge.log("getInstallerPackageName:"+str);
            }
        });
        XposedHelpers.findAndHookMethod("android.app.SharedPreferencesImpl", lpparam.classLoader, "getString", String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                File mFile = (File) XposedHelpers.getObjectField(param.thisObject, "mFile");
                if (!mFile.getName().equals("app.xml")) return;
                String key = (String) param.args[0];
                //XposedBridge.log(mFile.getName()+"->"+key);
                if (isPro&&key.equals("premium_verify_token"))
                    param.setResult("test");
                else if(!isPro&&key.equals("premium_email"))
                    param.setResult("test@test.com");
                else if(!isPro&&key.equals("premium_code"))
                    param.setResult("000000000000000000000000000");
                else if(!isPro&&key.equals("key_token"))
                    param.setResult("test");
                else if(!isPro&&key.equals("key_token_encrypt"))
                    param.setResult(encrypt("test"));
            }
        });
        XposedHelpers.findAndHookMethod("android.app.SharedPreferencesImpl", lpparam.classLoader, "getLong", String.class, long.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                File mFile = (File) XposedHelpers.getObjectField(param.thisObject, "mFile");
                if (!mFile.getName().equals("app.xml")) return;
                String key = (String) param.args[0];
                if (isPro&&key.equals("premium_verify_expire_time"))
                    param.setResult(System.currentTimeMillis()+2592000000L-1L);
            }
        });
        if(!isPro)XposedHelpers.findAndHookMethod(android.app.Activity.class, "finish", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                for(StackTraceElement stackTraceElement:new Throwable().getStackTrace()){
                    if(stackTraceElement.getClassName().equals("com.guoshi.httpcanary.ui.HomeActivity")
                            &&stackTraceElement.getMethodName().equals("onCreate")){
                        param.setResult(null);
                        break;
                    }
                }
                //XposedBridge.log("finish");
            }
        });
        if(!isPro)XposedHelpers.findAndHookMethod("com.stub.StubApp", lpparam.classLoader, "attachBaseContext", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Context context = (Context) param.args[0];
                ClassLoader classLoader = context.getClassLoader();
                //XposedHelpers.findAndHookMethod("com.guoshi.httpcanary.jni.Guard", classLoader, "isPremium", Context.class, XC_MethodReplacement.returnConstant(true));
                XposedHelpers.findAndHookMethod("com.guoshi.ﱰ.ﱱ.ﱲ", classLoader, "ﱰ", java.lang.Class.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Class clazz  = (Class)param.args[0];
                        //XposedBridge.log(clazz.getName());
                        if("com.guoshi.httpcanary.model.CodeVerifyResBody".equals(clazz.getName())){
                            XposedHelpers.setIntField(param.getResult(),"status", 1);
                        }
                    }
                });
                XposedHelpers.findAndHookMethod("com.guoshi.httpcanary.jni.Cont", classLoader, "a", android.content.Context.class, XC_MethodReplacement.returnConstant(false));
                XposedHelpers.findAndHookMethod("com.guoshi.httpcanary.jni.Cont", classLoader, "z", android.content.Context.class, XC_MethodReplacement.returnConstant(true));

            }
        });
    }

    private static String encrypt(String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec("DaDaGuoshi666666".getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec("1234ABCD5678EFGH".getBytes());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            return Base64.encodeToString(cipher.doFinal(data.getBytes()), Base64.NO_WRAP);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            return "";
        }
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

}