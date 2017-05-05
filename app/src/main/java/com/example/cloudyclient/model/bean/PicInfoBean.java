package com.example.cloudyclient.model.bean;

import android.media.ExifInterface;

/**
 * Created by wangyuhang on 17-5-5.
 */

public class PicInfoBean {
    private String FMake;//相机厂家
    private String FModel;//相机机型
    private String FDateTime;//拍摄时间
    private String FFNumber;//光圈值
    private String FExposureTime;//曝光时间
    private String FISOSpeedRatings;//ISO值
    private String FFocalLength;//焦距
    private String FImageLength;//图片像素长度
    private String FImageWidth;//图片像素宽度

    public PicInfoBean() {
    }

    public PicInfoBean(ExifInterface exifInterface) {
        this.FMake = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
        this.FModel = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
        this.FDateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
        this.FFNumber = exifInterface.getAttribute(ExifInterface.TAG_F_NUMBER);
        this.FExposureTime = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
        this.FISOSpeedRatings = exifInterface.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS);
        this.FFocalLength = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
        this.FImageLength = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
        this.FImageWidth = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
    }

    public PicInfoBean(String FMake, String FModel, String FDateTime, String FFNumber, String
            FExposureTime, String FISOSpeedRatings, String FFocalLength, String FImageLength, String
            FImageWidth) {
        this.FMake = FMake;
        this.FModel = FModel;
        this.FDateTime = FDateTime;
        this.FFNumber = FFNumber;
        this.FExposureTime = FExposureTime;
        this.FISOSpeedRatings = FISOSpeedRatings;
        this.FFocalLength = FFocalLength;
        this.FImageLength = FImageLength;
        this.FImageWidth = FImageWidth;
    }

    public String getFMake() {
        return FMake;
    }

    public void setFMake(String FMake) {
        this.FMake = FMake;
    }

    public String getFModel() {
        return FModel;
    }

    public void setFModel(String FModel) {
        this.FModel = FModel;
    }

    public String getFDateTime() {
        return FDateTime;
    }

    public void setFDateTime(String FDateTime) {
        this.FDateTime = FDateTime;
    }

    public String getFFNumber() {
        return FFNumber;
    }

    public void setFFNumber(String FFNumber) {
        this.FFNumber = FFNumber;
    }

    public String getFExposureTime() {
        return FExposureTime;
    }

    public void setFExposureTime(String FExposureTime) {
        this.FExposureTime = FExposureTime;
    }

    public String getFISOSpeedRatings() {
        return FISOSpeedRatings;
    }

    public void setFISOSpeedRatings(String FISOSpeedRatings) {
        this.FISOSpeedRatings = FISOSpeedRatings;
    }

    public String getFFocalLength() {
        return FFocalLength;
    }

    public void setFFocalLength(String FFocalLength) {
        this.FFocalLength = FFocalLength;
    }

    public String getFImageLength() {
        return FImageLength;
    }

    public void setFImageLength(String FImageLength) {
        this.FImageLength = FImageLength;
    }

    public String getFImageWidth() {
        return FImageWidth;
    }

    public void setFImageWidth(String FImageWidth) {
        this.FImageWidth = FImageWidth;
    }
}
