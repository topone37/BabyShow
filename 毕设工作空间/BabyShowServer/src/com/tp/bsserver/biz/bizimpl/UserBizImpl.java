package com.tp.bsserver.biz.bizimpl;

import com.tp.bsserver.biz.UsersBiz;
import com.tp.bsserver.dao.UsersDao;
import com.tp.bsserver.dao.daoimpl.UsersDaoImpl;
import com.tp.bsserver.po.Users;

/**
 * Created by Administrator on 2015/11/17.
 */
public class UserBizImpl implements UsersBiz {
    private UsersDao usersDao = new UsersDaoImpl();

    @Override
    public Users login(String username, String password) {
        return usersDao.query(username, password);
    }

    @Override
    public int regist(String username, String password) {
        return usersDao.insert(username, password);
    }

    @Override
    public int modifyHead(int uid, String imgname) {
        return usersDao.updateHead(uid, imgname);
    }

    @Override
    public int modifyInfo(int uid, String nickname, String sex, String intro) {
        return usersDao.updateInfo(uid, nickname, sex, intro);
    }



}
