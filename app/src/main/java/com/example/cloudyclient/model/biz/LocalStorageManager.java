package com.example.cloudyclient.model.biz;

import android.Manifest;
import android.os.Environment;
import android.util.Log;

import com.example.cloudyclient.MainApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import permissions.dispatcher.NeedsPermission;

/**
 * Created by wangyuhang on 17-5-5.
 */

public class LocalStorageManager {

    //获取本地图片的路径集合
    public static List<String> getAllPicPath() {
        List<String> result = new ArrayList<String>();

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment
                    .DIRECTORY_PICTURES), MainApplication.PHOTOS_DIR);
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    result.add(file.getAbsolutePath());
                }
            }
        }

        return result;
    }

    //删除本地照片
    public static void deletePhoto(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            boolean result = file.delete();
            Log.d(MainApplication.TAG, "删除照片: " + path + " , 结果: " + result);
        }
    }
}
