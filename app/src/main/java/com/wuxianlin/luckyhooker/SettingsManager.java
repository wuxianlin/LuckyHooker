package com.wuxianlin.luckyhooker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.FileObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxianlin on 2017/8/10.
 */

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

    private SettingsManager(Context context) {
        mContext = !context.isDeviceProtectedStorage() ?
                context.createDeviceProtectedStorageContext() : context;
        mFileObserverListeners = new ArrayList<>();
        mPrefsMain =  new WorldReadablePrefs(mContext, mContext.getPackageName() + "_preferences");
        mFileObserverListeners.add(mPrefsMain);

        registerFileObserver();
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
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // main dir
                File pkgFolder = mContext.getDataDir();
                if (pkgFolder.exists()) {
                    pkgFolder.setExecutable(true, false);
                    pkgFolder.setReadable(true, false);
                }
            }
        });
    }

    public WorldReadablePrefs getMainPrefs() {
        return mPrefsMain;
    }

    private void registerFileObserver() {
        mFileObserver = new FileObserver(mContext.getDataDir() + "/shared_prefs",
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
