package com.example.cloudyclient;

import android.app.Application;
import android.content.Context;

import com.example.cloudyclient.model.biz.GreenDaoManager;
import com.example.cloudyclient.model.biz.ScreenPropManager;

/**
 * Created by wangyuhang on 17-5-5.
 */
public class MainApplication extends Application {
    public static final String TAG = "myLog";
    private static Context mContext;

    public static final String PHOTOS_DIR = "Photo Receiver";//程序指定照片目录名

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        GreenDaoManager.getInstance();

        ScreenPropManager.getAndroiodScreenProperty();
    }

    public static Context getContext() {
        return mContext;
    }
}
