package com.example.cloudyclient.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangyuhang on 17-5-9.
 */

@Entity
public class PicEntity {
    @Id
    private Long id;
    @Unique
    private String fileName;

    private Long fileSize;//照片文件大小

    private String FMake;//相机厂家
    private String FModel;//相机机型
    private String FDateTime;//拍摄时间
    private String FFNumber;//光圈值
    private String FExposureTime;//曝光时间
    private String FISOSpeedRatings;//ISO值
    private String FFocalLength;//焦距
    private String FImageLength;//照片像素高度
    private String FImageWidth;//照片像素宽度

    @Unique
    private String MD5;//文件指纹

    @Generated(hash = 652472277)
    public PicEntity(Long id, String fileName, Long fileSize, String FMake,
            String FModel, String FDateTime, String FFNumber, String FExposureTime,
            String FISOSpeedRatings, String FFocalLength, String FImageLength,
            String FImageWidth, String MD5) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.FMake = FMake;
        this.FModel = FModel;
        this.FDateTime = FDateTime;
        this.FFNumber = FFNumber;
        this.FExposureTime = FExposureTime;
        this.FISOSpeedRatings = FISOSpeedRatings;
        this.FFocalLength = FFocalLength;
        this.FImageLength = FImageLength;
        this.FImageWidth = FImageWidth;
        this.MD5 = MD5;
    }

    @Generated(hash = 328434693)
    public PicEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFMake() {
        return this.FMake;
    }

    public void setFMake(String FMake) {
        this.FMake = FMake;
    }

    public String getFModel() {
        return this.FModel;
    }

    public void setFModel(String FModel) {
        this.FModel = FModel;
    }

    public String getFDateTime() {
        return this.FDateTime;
    }

    public void setFDateTime(String FDateTime) {
        this.FDateTime = FDateTime;
    }

    public String getFFNumber() {
        return this.FFNumber;
    }

    public void setFFNumber(String FFNumber) {
        this.FFNumber = FFNumber;
    }

    public String getFExposureTime() {
        return this.FExposureTime;
    }

    public void setFExposureTime(String FExposureTime) {
        this.FExposureTime = FExposureTime;
    }

    public String getFISOSpeedRatings() {
        return this.FISOSpeedRatings;
    }

    public void setFISOSpeedRatings(String FISOSpeedRatings) {
        this.FISOSpeedRatings = FISOSpeedRatings;
    }

    public String getFFocalLength() {
        return this.FFocalLength;
    }

    public void setFFocalLength(String FFocalLength) {
        this.FFocalLength = FFocalLength;
    }

    public String getFImageLength() {
        return this.FImageLength;
    }

    public void setFImageLength(String FImageLength) {
        this.FImageLength = FImageLength;
    }

    public String getFImageWidth() {
        return this.FImageWidth;
    }

    public void setFImageWidth(String FImageWidth) {
        this.FImageWidth = FImageWidth;
    }

    public String getMD5() {
        return this.MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }
}
