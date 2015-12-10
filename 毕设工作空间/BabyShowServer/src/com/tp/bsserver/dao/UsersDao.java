package com.tp.bsserver.dao;

import com.tp.bsserver.po.Users;

/**
 * Created by Administrator on 2015/11/15.
 */
public interface UsersDao {


    public Users query(String username, String password);//查选 ->登录

    public int insert(String username, String password);//添加 ->注册

    public int updateHead(int id, String imgname);//更新头像


    public int updateInfo(int id, String nickname, String sex, String intro);//更新信息  简介 性别 昵称


}
