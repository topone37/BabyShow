package com.tp.bsserver.dao;

/**
 * Created by Administrator on 2015/12/5.
 */
public interface AlbumDao {
    public int insertAlbum(String aname, int uid);//添加相册

    public int insertPhoto(int aid, String imgname);//添加照片

    public int delAlbumById(int aid);//删除相册

    public int updateAlbum(int aid, String name);//删除相册

    public String queryAll(int uid);  // JsonArray -> 一个个相册

    public String queryAlbumById(int id); //通过相册id获取对应的图片信息  JsonArray ->一个个 图片
}
