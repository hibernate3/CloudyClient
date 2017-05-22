package com.example.cloudyclient.model.biz;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wangyuhang on 17-5-5.
 */

public class ToastUtil {
    private static Toast toast = null;

    public static void showToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }

        toast.show();
    }
}
