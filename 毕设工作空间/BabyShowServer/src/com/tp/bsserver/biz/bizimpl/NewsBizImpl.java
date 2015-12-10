package com.tp.bsserver.biz.bizimpl;

import com.tp.bsserver.biz.NewsBiz;
import com.tp.bsserver.dao.NewsDao;
import com.tp.bsserver.dao.daoimpl.NewsDaoImpl;
import com.tp.bsserver.vo.News;

import java.util.List;

/**
 * Created by Administrator on 2015/11/27.
 */
public class NewsBizImpl implements NewsBiz {
    private NewsDao newsDao = new NewsDaoImpl();

    @Override
    public int add(int uid, String content, String[] imgs) {
        return newsDao.insert(uid, content, imgs);
    }

    @Override
    public int remove(int id) {
        return newsDao.delete(id);
    }

    @Override
    public List<News> getAll(int page) {
        return newsDao.getAll(page);
    }

    @Override
    public String getNewsDetailById(int id,int uid) {
        return newsDao.getNewsDetailById(id,uid);
    }

    @Override
    public int zan(int uid, int nid) {
        return newsDao.updateZan(uid, nid);
    }

    @Override
    public int col(int uid, int nid) {
        return newsDao.updateCol(uid, nid);
    }

    @Override
    public int comment(int uid, int nid, String content) {
        return newsDao.updateComment(uid, nid, content);
    }

    @Override
    public List<News> getNewsByUid(int uid) {
        return newsDao.queryNewsByUid(uid);
    }

    public List<News> getColByUid(int uid) {
        return newsDao.queryColByUid(uid);
    }

}
