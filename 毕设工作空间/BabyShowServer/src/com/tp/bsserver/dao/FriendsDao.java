package com.tp.bsserver.dao;

/**
 * Created by Administrator on 2015/11/28.
 */
public interface FriendsDao {
    String queryById(int id);

    int insert(int id1, int id2);
}
