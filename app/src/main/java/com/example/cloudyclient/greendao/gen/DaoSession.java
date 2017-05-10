package com.example.cloudyclient.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.cloudyclient.model.bean.PicEntity;

import com.example.cloudyclient.greendao.gen.PicEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig picEntityDaoConfig;

    private final PicEntityDao picEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        picEntityDaoConfig = daoConfigMap.get(PicEntityDao.class).clone();
        picEntityDaoConfig.initIdentityScope(type);

        picEntityDao = new PicEntityDao(picEntityDaoConfig, this);

        registerDao(PicEntity.class, picEntityDao);
    }
    
    public void clear() {
        picEntityDaoConfig.clearIdentityScope();
    }

    public PicEntityDao getPicEntityDao() {
        return picEntityDao;
    }

}