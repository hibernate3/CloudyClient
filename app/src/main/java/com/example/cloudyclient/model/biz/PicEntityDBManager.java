package com.example.cloudyclient.model.biz;

import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;

import com.example.cloudyclient.MainApplication;
import com.example.cloudyclient.greendao.gen.PicEntityDao;
import com.example.cloudyclient.model.bean.PicEntity;

import org.greenrobot.greendao.query.Join;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.io.File;
import java.io.IOException;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * Created by wangyuhang on 17-5-9.
 */

public class PicEntityDBManager {
    private static PicEntityDBManager mInstance;

    private PicEntityDao mPicEntityDao;

    public static PicEntityDBManager getInstance() {
        if (mInstance == null) {
            mInstance = new PicEntityDBManager();
        }
        return mInstance;
    }

    private PicEntityDBManager() {
        mPicEntityDao = GreenDaoManager.getInstance().getSession().getPicEntityDao();
    }

    public void insert(PicEntity picEntity) {
        mPicEntityDao.insertOrReplace(picEntity);
    }

    public void insert(String fileName) {
        try {
            ExifInterface exifInterface = new ExifInterface(fileName);
            PicEntity picEntity = new PicEntity();


            picEntity.setFileSize(new File(fileName).length());
            picEntity.setFileName(fileName);
            picEntity.setFMake(exifInterface.getAttribute(ExifInterface.TAG_MAKE));
            picEntity.setFModel(exifInterface.getAttribute(ExifInterface.TAG_MODEL));
            picEntity.setFDateTime(exifInterface.getAttribute(ExifInterface.TAG_DATETIME));
            picEntity.setFFNumber(exifInterface.getAttribute(ExifInterface.TAG_F_NUMBER));

            double tempTime = exifInterface.getAttributeDouble(ExifInterface.TAG_EXPOSURE_TIME, 0.00);
            if (tempTime < 1.00) {
                picEntity.setFExposureTime("1/" + Math.rint(1 / tempTime));
            } else {
                picEntity.setFExposureTime("" + tempTime);
            }

            picEntity.setFISOSpeedRatings(exifInterface.getAttribute(ExifInterface
                    .TAG_ISO_SPEED_RATINGS));
            picEntity.setFFocalLength(exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH));
            picEntity.setFImageLength(exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
            picEntity.setFImageWidth(exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));

            mPicEntityDao.insertOrReplace(picEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(List<String> fileNames) {
        for (int i = 0; i < fileNames.size(); i++) {
            try {
                ExifInterface exifInterface = new ExifInterface(fileNames.get(i));
                PicEntity picEntity = new PicEntity();

                picEntity.setFileSize(new File(fileNames.get(i)).length());
                picEntity.setFileName(fileNames.get(i));
                picEntity.setFMake(exifInterface.getAttribute(ExifInterface.TAG_MAKE));
                picEntity.setFModel(exifInterface.getAttribute(ExifInterface.TAG_MODEL));
                picEntity.setFDateTime(exifInterface.getAttribute(ExifInterface.TAG_DATETIME));
                picEntity.setFFNumber(exifInterface.getAttribute(ExifInterface.TAG_F_NUMBER));

                double tempTime = exifInterface.getAttributeDouble(ExifInterface.TAG_EXPOSURE_TIME, 0.00);
                if (tempTime < 1.00) {
                    picEntity.setFExposureTime("1/" + Math.rint(1 / tempTime));
                } else {
                    picEntity.setFExposureTime("" + tempTime);
                }

                picEntity.setFISOSpeedRatings(exifInterface.getAttribute(ExifInterface
                        .TAG_ISO_SPEED_RATINGS));
                picEntity.setFFocalLength(exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH));
                picEntity.setFImageLength(exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
                picEntity.setFImageWidth(exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));

                mPicEntityDao.insertOrReplace(picEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void delete(String fileName) {
        PicEntity picEntity = mPicEntityDao.queryBuilder().where(PicEntityDao.Properties.FileName.eq
                (fileName)).build().unique();

        if (picEntity != null) {
            mPicEntityDao.deleteByKey(picEntity.getId());
            Log.d(MainApplication.TAG, "成功删除数据库记录： " + picEntity.getFileName());
        } else {
            Log.d(MainApplication.TAG, "数据库记录不存在");
        }
    }

    public void update() {

    }

    public List<PicEntity> query(String selfFileName, String aperture, String iso, String exposure, String len) {
        StringBuffer sql = new StringBuffer("SELECT FILE_NAME FROM PIC_ENTITY WHERE 1 = 1");

        if (!TextUtils.isEmpty(aperture)) {
            sql.append(" AND FFNUMBER = '" + aperture + "'");
        }

        if (!TextUtils.isEmpty(iso)) {
            sql.append(" AND FISOSPEED_RATINGS = '" + iso + "'");
        }

        if (!TextUtils.isEmpty(exposure)) {
            sql.append(" AND FEXPOSURE_TIME = '" + exposure + "'");
        }

        if (!TextUtils.isEmpty(len)) {
            sql.append(" AND FFOCAL_LENGTH = '" + len + "'");
        }

        if (!TextUtils.isEmpty(selfFileName)) {
            sql.append(" AND FILE_NAME != '" + selfFileName + "'");//排除自己
        }

        Log.d(MainApplication.TAG, sql.toString());

        Query<PicEntity> query = mPicEntityDao.queryBuilder().where(
                new WhereCondition.StringCondition("FILE_NAME IN " + "(" + sql.toString() + ")")
        ).build();

        return query.list();
    }

    public List<PicEntity> query(String fileName) {
        return mPicEntityDao.queryBuilder().where(PicEntityDao.Properties.FileName.eq(fileName)).build()
                .list();
    }

    public List<PicEntity> queryAll() {
        return mPicEntityDao.loadAll();
    }
}
