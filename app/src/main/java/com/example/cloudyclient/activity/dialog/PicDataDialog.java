package com.example.cloudyclient.activity.dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cloudyclient.R;
import com.example.cloudyclient.model.bean.PicEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wangyuhang on 17-5-15.
 */

public class PicDataDialog extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.data_tv)
    TextView dataTv;
    Unbinder unbinder;

    private PicEntity mPicEntity;

    public PicDataDialog(PicEntity picEntity) {
        this.mPicEntity = picEntity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dialog_pic_data, null);
        unbinder = ButterKnife.bind(this, contentView);

        dataTv.setText("文件路径: " + mPicEntity.getFileName()
                + "\n\n相机厂家: " + mPicEntity.getFMake()
                + "\n\n相机机型: " + mPicEntity.getFModel()
                + "\n\n拍摄时间: " + mPicEntity.getFDateTime()
                + "\n\n光圈值: " + mPicEntity.getFFNumber()
                + "\n\n曝光时间: " + mPicEntity.getFExposureTime()
                + "\n\nISO值: " + mPicEntity.getFISOSpeedRatings()
                + "\n\n焦距: " + mPicEntity.getFFocalLength()
                + "\n\n照片像素高度: " + mPicEntity.getFImageLength()
                + "\n\n照片像素宽度: " + mPicEntity.getFImageWidth());

        return contentView;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
