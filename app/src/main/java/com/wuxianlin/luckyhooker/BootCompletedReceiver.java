package com.wuxianlin.luckyhooker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by wuxianlin on 2017/8/10.
 */

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(intent.getAction())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
                    !MainActivity.checkLSPosed(context)) {
                SettingsManager.getInstance(context).fixFolderPermissionsAsync();
            }
        }
    }
}
