package com.example.cloudyclient.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.example.cloudyclient.MainApplication;

/**
 * Created by wangyuhang on 17-5-15.
 */

public class ScreenPropUtil {
    public static int screenWidth_dp;//单位dp
    public static int screenHeight_dp;//单位dp
    public static int screenWidth_px;//单位px
    public static int screenHeight_px;//单位px

    public static void getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) MainApplication.getContext().getSystemService(Context
                .WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height = dm.heightPixels; // 屏幕高度（像素）
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）

        //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);//屏幕宽度(dp)
        int screenHeight = (int) (height / density);//屏幕高度(dp)

        screenHeight_px = height;
        screenWidth_px = width;
        screenWidth_dp = screenWidth;
        screenHeight_dp = screenHeight;
    }

    /**
     * dp2px
     */
    public static int dip2px(float dpValue) {
        float scale = MainApplication.getContext().getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dp
     */
    public static int px2dip(float pxValue) {
        final float scale = MainApplication.getContext().getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);
    }
}
