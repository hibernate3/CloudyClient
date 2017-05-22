package com.example.cloudyclient.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cloudyclient.MainApplication;
import com.example.cloudyclient.R;
import com.example.cloudyclient.model.bean.PicEntity;
import com.example.cloudyclient.model.biz.LocalStorageManager;
import com.example.cloudyclient.model.biz.PicEntityDBManager;
import com.example.cloudyclient.model.biz.ToastUtil;
import com.example.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PicMainActivity extends AppCompatActivity {

    @BindView(R.id.show_img)
    PhotoView showImg;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.search_panel)
    LinearLayout searchPanel;
    @BindView(R.id.aperture_cb)
    CheckBox apertureCb;
    @BindView(R.id.iso_cb)
    CheckBox isoCb;
    @BindView(R.id.exposure_cb)
    CheckBox exposureCb;
    @BindView(R.id.len_cb)
    CheckBox lenCb;
    @BindView(R.id.search_btn)
    Button searchBtn;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_show:
                    showImg.setVisibility(View.VISIBLE);
                    message.setVisibility(View.INVISIBLE);
                    searchPanel.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_data:
                    showData();
                    return true;
                case R.id.navigation_compare:
                    showCompare();
                    return true;
            }
            return false;
        }

    };

    private String mPicPath;//文件名（含完整路径）
    private PicEntity mPicEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_main);
        ButterKnife.bind(this);

        initData();

        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        mPicPath = intent.getStringExtra("pic_path");
    }

    private void initView() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setSupportActionBar(toolbar);//工具栏
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_action_delete:
                        new AlertDialog.Builder(PicMainActivity.this)
                                .setMessage("确认是否删除此张照片？")
                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LocalStorageManager.deletePhoto(mPicPath);
                                        finish();
                                    }
                                })
                                .setNegativeButton("否", null).show();
                        break;
                }

                return true;
            }
        });

        showImg.enable();
        Picasso
                .with(this)
                .load(new File(mPicPath))
                .config(Bitmap.Config.RGB_565)
                .into(showImg);
    }

    private void showData() {
        showImg.setVisibility(View.INVISIBLE);
        message.setVisibility(View.VISIBLE);
        searchPanel.setVisibility(View.INVISIBLE);

        Observable
                .create(new ObservableOnSubscribe<PicEntity>() {
                    @Override
                    public void subscribe(ObservableEmitter<PicEntity> emitter) throws Exception {
                        if (mPicEntity == null) {
                            emitter.onNext(PicEntityDBManager.getInstance().query(mPicPath).get(0));
                        } else {
                            emitter.onNext(mPicEntity);
                        }
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PicEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(MainApplication.TAG, "开始获取照片数据...");
                    }

                    @Override
                    public void onNext(PicEntity picEntity) {
                        mPicEntity = picEntity;

                        message.setText("文件路径: " + picEntity.getFileName()
                                + "\n\n相机厂家: " + picEntity.getFMake()
                                + "\n\n相机机型: " + picEntity.getFModel()
                                + "\n\n拍摄时间: " + picEntity.getFDateTime()
                                + "\n\n光圈值: " + picEntity.getFFNumber()
                                + "\n\n曝光时间: " + picEntity.getFExposureTime()
                                + "\n\nISO值: " + picEntity.getFISOSpeedRatings()
                                + "\n\n焦距: " + picEntity.getFFocalLength()
                                + "\n\n照片像素高度: " + picEntity.getFImageLength()
                                + "\n\n照片像素宽度: " + picEntity.getFImageWidth()
                        );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(MainApplication.TAG, "获取照片数据异常: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(MainApplication.TAG, "获取照片数据结束.");
                    }
                });
    }

    private void showCompare() {
        showImg.setVisibility(View.INVISIBLE);
        message.setVisibility(View.INVISIBLE);
        searchPanel.setVisibility(View.VISIBLE);
    }

    private void search() {
        Observable
                .create(new ObservableOnSubscribe<PicEntity>() {
                    @Override
                    public void subscribe(ObservableEmitter<PicEntity> emitter) throws Exception {
                        if (mPicEntity == null) {
                            emitter.onNext(PicEntityDBManager.getInstance().query(mPicPath).get(0));
                        } else {
                            emitter.onNext(mPicEntity);
                        }
                        emitter.onComplete();
                    }
                })
                .map(new Function<PicEntity, List<PicEntity>>() {
                    @Override
                    public List<PicEntity> apply(PicEntity picEntity) throws Exception {
                        mPicEntity = picEntity;

                        List<PicEntity> list = PicEntityDBManager.getInstance().query(
                                mPicEntity.getFileName(),
                                apertureCb.isChecked() ? mPicEntity.getFFNumber() : null,
                                isoCb.isChecked() ? mPicEntity.getFISOSpeedRatings() : null,
                                exposureCb.isChecked() ? mPicEntity.getFExposureTime() : null,
                                lenCb.isChecked() ? mPicEntity.getFFocalLength() : null);

                        return list;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<PicEntity>>() {
                    @Override
                    public void accept(List<PicEntity> picEntities) throws Exception {
                        gotoPicListActivity(picEntities);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(MainApplication.TAG, "搜索比对照片数据异常: " + throwable.toString());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(MainApplication.TAG, "搜索比对照片数据结束.");
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.d(MainApplication.TAG, "开始搜索比对照片数据...");
                    }
                });
    }

    private void gotoPicListActivity(List<PicEntity> picEntities) {
        Intent intent = new Intent(PicMainActivity.this, PicListActivity.class);
        intent.putParcelableArrayListExtra("pic_entities", (ArrayList<? extends Parcelable>) picEntities);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pic_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.search_btn)
    public void onViewClicked() {
        if (apertureCb.isChecked() || isoCb.isChecked() || exposureCb.isChecked() || lenCb.isChecked()) {
            search();
        } else {
            ToastUtil.showToast(PicMainActivity.this, "请选择其中至少一项属性");
        }
    }
}
