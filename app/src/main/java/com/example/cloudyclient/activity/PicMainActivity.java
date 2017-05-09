package com.example.cloudyclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cloudyclient.MainApplication;
import com.example.cloudyclient.R;
import com.example.cloudyclient.model.bean.PicInfoBean;
import com.example.cloudyclient.model.biz.PicEntityManager;
import com.example.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.expose_cb)
    CheckBox exposeCb;
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
                    showImg.setVisibility(View.INVISIBLE);
                    message.setVisibility(View.VISIBLE);
                    searchPanel.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_compare:
                    showImg.setVisibility(View.INVISIBLE);
                    message.setVisibility(View.INVISIBLE);
                    searchPanel.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }

    };

    private String picPath;
    private PicInfoBean picInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_main);
        ButterKnife.bind(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        picPath = intent.getStringExtra("pic_path");
        picInfoBean = intent.getParcelableExtra("pic_info");

        showImg.enable();
        Picasso.with(this).load(new File(picPath)).into(showImg);

        message.setText("相机厂家: " + picInfoBean.getFMake()
                + "\n\n相机机型: " + picInfoBean.getFModel()
                + "\n\n拍摄时间: " + picInfoBean.getFDateTime()
                + "\n\n光圈值: " + picInfoBean.getFFNumber()
                + "\n\n曝光时间: " + picInfoBean.getFExposureTime()
                + "\n\nISO值: " + picInfoBean.getFISOSpeedRatings()
                + "\n\n焦距: " + picInfoBean.getFFocalLength()
                + "\n\n照片像素高度: " + picInfoBean.getFImageLength()
                + "\n\n照片像素宽度: " + picInfoBean.getFImageWidth()
        );
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
    }
}
