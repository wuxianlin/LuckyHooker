package com.wuxianlin.luckyhooker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * https://github.com/LSPosed/LSPosed/wiki/New-XSharedPreferences
 */
public class LSPosedUtils {

    private static boolean checkResult = false;
    private static boolean checked = false;

    @SuppressLint("WorldReadableFiles")
    public static boolean checkLSPosed(Context context) {
        if (checked)
            return checkResult;
        try {
            context.getSharedPreferences(context.getPackageName() + "_preferences",
                    Context.MODE_WORLD_READABLE);
            checkResult = true;
            checked = true;
        } catch (SecurityException exception) {
            Toast.makeText(context, "LuckyHooker Settings may not work", Toast.LENGTH_LONG).show();
            checkResult = false;
            checked = true;
        }
        return checkResult;
    }

}
