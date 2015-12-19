package com.tp.bsserver.biz;

/**
 * Created by Administrator on 2015/11/29.
 */
public interface FriendsBiz {
    public String findFriendsById(int id);

    public int addFriends(int id1, int id2);

    public String findFriendsByKeyWord(String keyword);
}
