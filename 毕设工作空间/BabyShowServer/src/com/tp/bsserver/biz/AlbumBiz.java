package com.tp.bsserver.biz;

/**
 * Created by Administrator on 2015/12/5.
 */
public interface AlbumBiz {
    public int addAlbum(String aname, int uid);//添加相册

    public int addPhoto(String imgname, int aid, String pcontent);//添加相册图片

    public int removeAlbumById(int aid);//删除相册

    public String getAll(int uid);  // JsonArray -> 一个个相册

    public String getPhotosById(int id); //通过相册id获取对应的图片信息  JsonArray ->一个个 图片

    int renameAlbum(int aid, String aname);//重命名相册
}
