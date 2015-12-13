package com.tp.bsserver.biz.bizimpl;

import com.tp.bsserver.biz.AlbumBiz;
import com.tp.bsserver.dao.AlbumDao;
import com.tp.bsserver.dao.daoimpl.AlbumDaoImpl;

/**
 * Created by Administrator on 2015/12/5.
 */
public class AlbumBizImpl implements AlbumBiz {
    AlbumDao albumDao = new AlbumDaoImpl();

    @Override
    public int addAlbum(String aname, int uid) {
        return albumDao.insertAlbum(aname, uid);
    }

    @Override
    public int addPhoto(String imgname, int aid) {
        return albumDao.insertPhoto(aid, imgname);
    }

    @Override
    public int removeAlbumById(int aid) {
        return albumDao.delAlbumById(aid);
    }

    @Override
    public String getAll(int uid) {
        return albumDao.queryAll(uid);
    }

    @Override
    public String getPhotosById(int id) {
        return albumDao.queryAlbumById(id);
    }

    @Override
    public int renameAlbum(int aid, String aname) {
        return albumDao.updateAlbum(aid, aname);
    }
}
