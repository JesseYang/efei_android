package com.efei.lib.android.persistence.greendao.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.efei.lib.android.persistence.greendao.User;
import com.efei.lib.android.persistence.greendao.Teacher;

import com.efei.lib.android.persistence.greendao.dao.UserDao;
import com.efei.lib.android.persistence.greendao.dao.TeacherDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig teacherDaoConfig;

    private final UserDao userDao;
    private final TeacherDao teacherDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        teacherDaoConfig = daoConfigMap.get(TeacherDao.class).clone();
        teacherDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        teacherDao = new TeacherDao(teacherDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(Teacher.class, teacherDao);
    }
    
    public void clear() {
        userDaoConfig.getIdentityScope().clear();
        teacherDaoConfig.getIdentityScope().clear();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public TeacherDao getTeacherDao() {
        return teacherDao;
    }

}