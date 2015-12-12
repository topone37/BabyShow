package com.tp.bsserver.dao;

import com.tp.bsserver.vo.News;

import java.util.List;

/**
 * Created by Administrator on 2015/11/26.
 */
public interface NewsDao {
    int insert(int uid, String content, String[] imgs);

    int delete(int id);

    List<News> getAll(int currPage, int uid);

    String getNewsDetailById(int id, int uid);//通过动态id获取动态详情

    int updateZan(int uid, int nid);

    int updateCol(int uid, int nid);

    List<News> queryNewsByUid(int uid);//通过用户ID获取用户发布的动态

    int updateComment(int uid, int nid, String content);//对动态进行评论

    List<News> queryColByUid(int uid);

}
