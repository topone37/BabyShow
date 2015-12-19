package com.tp.bsserver.biz.bizimpl;

import com.tp.bsserver.biz.FriendsBiz;
import com.tp.bsserver.dao.FriendsDao;
import com.tp.bsserver.dao.daoimpl.FriendsDaoImpl;

/**
 * Created by Administrator on 2015/11/29.
 */
public class FriendsBizImpl implements FriendsBiz {
    FriendsDao friendsDao = new FriendsDaoImpl();

    @Override
    public String findFriendsById(int id) {
        return friendsDao.queryById(id);
    }

    @Override
    public int addFriends(int id1, int id2) {
        return friendsDao.insert(id1, id2);
    }

    @Override
    public String findFriendsByKeyWord(String keyword) {
        return friendsDao.queryByKeyWord(keyword);
    }
}
