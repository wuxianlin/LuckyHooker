package com.wuxianlin.luckyhooker.hooks;

import android.os.Build;
import android.provider.Settings;

import com.wuxianlin.luckyhooker.Hook;
import com.wuxianlin.luckyhooker.HookUtils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxianlin on 2016/10/2.
 */

public class KSWEB implements Hook {

    public static final String hookPackageName = "ru.kslabs.ksweb";

    @Override
    public boolean canHook(String packageName) {
        return hookPackageName.equals(packageName);
    }

    @Override
    public void startHook(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("start Hook KSWEB");
        XposedHelpers.findAndHookMethod("android.app.SharedPreferencesImpl", lpparam.classLoader, "getString", String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                File mFile = (File)XposedHelpers.getObjectField(param.thisObject,"mFile");
                if (!mFile.getName().equals("sec.xml")) return;
                String key = (String)param.args[0];
                if (key.equals("value"))
                    param.setResult(getEncodedString(encrypt(getUUID() + "32eh2jrk345h34jgdcn34")));
                else if (key.equals("type"))
                    param.setResult(getEncodedString(encrypt("2")));
            }
        });
    }

    @Override
    public void startHook(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
    }

    private static final String ivStr = "fe2cb6987a543d10";
    private static final IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
    private static final String keyStr = "0213456789badcef";
    private static final String KEY_ALGORITHM = "AES";
    private static final SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), KEY_ALGORITHM);
    private static final String CBC_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static String getEncodedString(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        int length = bArr.length;
        String str = "";
        for (int i = 0; i < length; i++) {
            str = (bArr[i] & 0xff) < 0x10 ? str + "0" + Integer.toHexString(bArr[i] & 0xff) : str + Integer.toHexString(bArr[i] & 0xff);
        }
        return str;
    }

    private static byte[] getDecodedByte(String str) {
        byte[] bArr = null;
        if (str != null && str.length() >= 2) {
            int length = str.length() / 2;
            bArr = new byte[length];
            for (int i = 0; i < length; i++) {
                bArr[i] = (byte) Integer.parseInt(str.substring(i * 2, (i * 2) + 2), 0x10);
            }
        }
        return bArr;
    }

    private byte[] encrypt(String str) throws Exception {
        if (str == null || str.length() == 0) {
            throw new Exception("Empty string");
        }
        try {
            Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            return cipher.doFinal(str.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }
    }

    private byte[] decrypt(String str) throws Exception {
        if (str == null || str.length() == 0) {
            throw new Exception("Empty string");
        }
        try {
            Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            return cipher.doFinal(getDecodedByte(str));
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
    }

    private static String getUUID() {
        String str = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.DISPLAY.length() % 10) + (Build.HOST.length() % 10) + (Build.ID.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10) + (Build.TAGS.length() % 10) + (Build.TYPE.length() % 10) + (Build.USER.length() % 10);
        try {
            return md5(new UUID((long) str.hashCode(), (long) Build.class.getField("SERIAL").get(null).toString().hashCode()).toString());
        } catch (Exception e) {
            return md5(new UUID((long) str.hashCode(), (long) ("HJ34KD87" + Settings.Secure.getString(HookUtils.getContext().getContentResolver(),"android_id")).hashCode()).toString());
        }
    }

    private static String md5(String str) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                stringBuffer.append(Integer.toHexString((b & 0xff) | 0x100).substring(1, 3));
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
