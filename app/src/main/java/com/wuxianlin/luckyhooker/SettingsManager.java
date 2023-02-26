package com.wuxianlin.luckyhooker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.FileObserver;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/GravityBox/GravityBox/blob/r/GravityBox/src/main/java/com/ceco/r/gravitybox/SettingsManager.java
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class SettingsManager {

    public interface FileObserverListener {
        void onFileUpdated(String path);

        void onFileAttributesChanged(String path);
    }

    private static Context mContext;
    private static SettingsManager mInstance;
    private WorldReadablePrefs mPrefsMain;
    private FileObserver mFileObserver;
    private List<FileObserverListener> mFileObserverListeners;
    private String mPreferenceDir;

    private SettingsManager(Context context) {
        mContext = !context.isDeviceProtectedStorage() ?
                context.createDeviceProtectedStorageContext() : context;
        mFileObserverListeners = new ArrayList<>();
        mPrefsMain =  new WorldReadablePrefs(mContext, getPreferenceDir(), mContext.getPackageName() + "_preferences");
        mFileObserverListeners.add(mPrefsMain);

        registerFileObserver();
    }

    public String getPreferenceDir() {
        if (mPreferenceDir == null) {
            try {
                SharedPreferences prefs = mContext.getSharedPreferences("dummy", Context.MODE_PRIVATE);
                prefs.edit().putBoolean("dummy", false).commit();
                Field f = prefs.getClass().getDeclaredField("mFile");
                f.setAccessible(true);
                mPreferenceDir = new File(((File) f.get(prefs)).getParent()).getAbsolutePath();
                Log.d("GravityBox", "Preference folder: " + mPreferenceDir);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                Log.e("GravityBox", "Could not determine preference folder path. Returning default.");
                e.printStackTrace();
                mPreferenceDir = mContext.getDataDir() + "/shared_prefs";
            }
        }
        return mPreferenceDir;
    }

    public static synchronized SettingsManager getInstance(Context context) {
        if (context == null && mInstance == null)
            throw new IllegalArgumentException("Context cannot be null");

        if (mInstance == null) {
            mInstance = new SettingsManager(context.getApplicationContext() != null ?
                    context.getApplicationContext() : context);
        }
        return mInstance;
    }

    public void fixFolderPermissionsAsync() {
        AsyncTask.execute(() -> {
            // main dir
            File pkgFolder = mContext.getDataDir();
            if (pkgFolder.exists()) {
                pkgFolder.setExecutable(true, false);
                pkgFolder.setReadable(true, false);
            }
        });
    }

    public WorldReadablePrefs getMainPrefs() {
        return mPrefsMain;
    }

    private void registerFileObserver() {
        mFileObserver = new FileObserver(getPreferenceDir(),
                FileObserver.ATTRIB | FileObserver.CLOSE_WRITE) {
            @Override
            public void onEvent(int event, String path) {
                for (FileObserverListener l : mFileObserverListeners) {
                    if ((event & FileObserver.ATTRIB) != 0)
                        l.onFileAttributesChanged(path);
                    if ((event & FileObserver.CLOSE_WRITE) != 0)
                        l.onFileUpdated(path);
                }
            }
        };
        mFileObserver.startWatching();
    }
}
