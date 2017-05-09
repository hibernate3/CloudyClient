package com.example.cloudyclient.model.biz;

import android.media.ExifInterface;
import android.util.Log;

import com.example.cloudyclient.MainApplication;
import com.example.cloudyclient.greendao.gen.PicEntityDao;
import com.example.cloudyclient.model.bean.PicEntity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by wangyuhang on 17-5-9.
 */

public class PicEntityManager {
    private static PicEntityManager mInstance;

    private PicEntityDao mPicEntityDao;

    public static PicEntityManager getInstance() {
        if (mInstance == null) {
            mInstance = new PicEntityManager();
        }
        return mInstance;
    }

    private PicEntityManager() {
        mPicEntityDao = GreenDaoManager.getInstance().getSession().getPicEntityDao();
    }

    public void insert(PicEntity picEntity) {
        mPicEntityDao.insertOrReplace(picEntity);
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
                picEntity.setFExposureTime(exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME));
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
            Log.d(MainApplication.TAG, "成功删除： " + picEntity.getFileName());
        } else {
            Log.d(MainApplication.TAG, "文件不存在");
        }
    }

    public void update() {

    }

    public List<PicEntity> query(String aperture, String iso, String expose, String len) {
        QueryBuilder<PicEntity> qb = mPicEntityDao.queryBuilder();

        if (aperture != null) {
            qb.where(PicEntityDao.Properties.FFNumber.eq(aperture));
        } else if (iso != null) {
            qb.where(PicEntityDao.Properties.FISOSpeedRatings.eq(iso));
        } else if (expose != null) {
            qb.where(PicEntityDao.Properties.FExposureTime.eq(expose));
        } else if (len != null) {
            qb.where(PicEntityDao.Properties.FFocalLength.eq(len));
        } else {
            return null;
        }

        return qb.build().list();
    }

    public List<PicEntity> query(String fileName) {
        return mPicEntityDao.queryBuilder().where(PicEntityDao.Properties.FileName.eq(fileName)).build()
                .list();
    }

    public List<PicEntity> queryAll() {
        return mPicEntityDao.loadAll();
    }
}
