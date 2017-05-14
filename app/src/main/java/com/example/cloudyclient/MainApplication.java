package com.example.cloudyclient;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.example.cloudyclient.model.biz.GreenDaoManager;
import com.example.cloudyclient.model.biz.PicEntityDBManager;
import com.example.cloudyclient.service.PicDBService;
import com.example.cloudyclient.util.LocalStorageUtil;

/**
 * Created by wangyuhang on 17-5-5.
 */
public class MainApplication extends Application {
    public static final String TAG = "myLog";
    private static Context mContext;

    public static final String PHOTOS_DIR = "Photo Receiver";//程序指定照片目录名

    public static int screenWidth;//单位dp
    public static int screenHeight;//单位dp

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        GreenDaoManager.getInstance();

        getAndroiodScreenProperty();
    }

    public static Context getContext() {
        return mContext;
    }

    public void getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height = dm.heightPixels; // 屏幕高度（像素）
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）

        //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);//屏幕宽度(dp)
        int screenHeight = (int) (height / density);//屏幕高度(dp)

        MainApplication.screenWidth = screenWidth;
        MainApplication.screenHeight = screenHeight;
    }
}
