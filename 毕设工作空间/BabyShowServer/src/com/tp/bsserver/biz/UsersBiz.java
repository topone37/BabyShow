package com.tp.bsserver.biz;

import com.tp.bsserver.po.Users;

/**
 * Created by Administrator on 2015/11/15.
 */
public interface UsersBiz {
    public Users login(String username, String password);

    public int regist(String username, String password);

    public int modifyHead(int uid, String imgname);

    public int modifyInfo(int uid, String nickname, String sex, String intro);

}
