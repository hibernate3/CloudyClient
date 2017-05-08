package com.example.cloudyclient.util;

import android.Manifest;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import permissions.dispatcher.NeedsPermission;

/**
 * Created by wangyuhang on 17-5-5.
 */

public class LocalStorageUtil {
    private static final String picFolderName = "Photo Receiver";

    //获取本地图片的路径集合
    public static List<String> getAllPicPath() {
        List<String> result = new ArrayList<String>();

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), picFolderName);
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    result.add(file.getAbsolutePath());
                }
            }
        }

        return result;
    }
}
