package com.example.cloudyclient.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cloudyclient.MainApplication;
import com.example.cloudyclient.R;
import com.example.cloudyclient.service.PicDBService;
import com.example.cloudyclient.model.biz.LocalStorageManager;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.grantBtn)
    Button grantBtn;
    @BindView(R.id.noticeTv)
    TextView noticeTv;

    private boolean isNeverAsk = false;

    private final int TO_PIC_WALL = 0;//activity跳转标识

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MainActivityPermissionsDispatcher.requestPermissionWithCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void requestPermission() {
        PicDBService.startActionInsert(MainActivity.this, LocalStorageManager.getAllPicPath());//遍历图片数据，写入数据库

        grantBtn.setVisibility(View.GONE);
        noticeTv.setVisibility(View.GONE);

        Intent intent = new Intent(MainActivity.this, PicWallActivity.class);
        startActivityForResult(intent, TO_PIC_WALL);

//        Intent intent = new Intent(MainActivity.this, PicSyncActivity.class);
//        startActivityForResult(intent, TO_PIC_WALL);
    }

    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationale(PermissionRequest request) {
        showRationaleDialog(R.string.permission_rationale, request);
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void permissionDenied() {
        grantBtn.setVisibility(View.VISIBLE);
        noticeTv.setVisibility(View.VISIBLE);
        noticeTv.setText(R.string.permission_denied);
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void neverAskAgain() {
        isNeverAsk = true;
        grantBtn.setVisibility(View.VISIBLE);
        noticeTv.setVisibility(View.VISIBLE);
        noticeTv.setText(R.string.permission_never_askagain);
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    @OnClick(R.id.grantBtn)
    public void onViewClicked() {
        if (isNeverAsk) {
            getAppDetailSettingIntent(this);
        } else {
            MainActivityPermissionsDispatcher.requestPermissionWithCheck(this);
        }
    }

    private void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            if (requestCode == TO_PIC_WALL) {//从PicWallActivity返回的情况
                finish();
            }
        }
    }
}
