package com.tp.bsserver.biz;

import com.tp.bsserver.vo.News;

import java.util.List;

/**
 * Created by Administrator on 2015/11/27.
 */
public interface NewsBiz {
    int add(int uid, String content, String[] imgs);

    int remove(int id);

    List<News> getAll(int currPage);

    String getNewsDetailById(int id,int uid);

    int zan(int uid, int nid);

    int comment(int uid, int nid, String content);

    List<News> getNewsByUid(int uid);

    int col(int uid, int nid);

    List<News> getColByUid(int uid);
}
