package com.tp.bsserver.dao.daoimpl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tp.bsserver.dao.NewsDao;
import com.tp.bsserver.dbutil.DBHelper;
import com.tp.bsserver.util.ConvertTime;
import com.tp.bsserver.vo.News;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/26.
 */
public class NewsDaoImpl implements NewsDao {

    private DBHelper dbHelper = new DBHelper();
    private String sql;//sql语句
    private ResultSet rs; //sql执行的结果集

    @Override
    public int insert(int uid, String content, String[] imgs) {
        //先插入动态  动态ID(自增长) 用户ID 日期（系统时间） zan（默认0） content
        sql = "insert into news values (null,?,sysdate(),?)";

        int affectRows = dbHelper.execOthers(sql, uid, content);
        if (affectRows > 0) {
            //动态表插入成功
            //查出最大的动态ID
            int maxNid = 0;
            rs = dbHelper.execQuery("select max(nid) from news");
            try {
                if (rs.next()) {
                    maxNid = rs.getInt(1);
                    //继续插入图片
                    for (int i = 0; i < imgs.length; i++) {
                        sql = "insert into images values (null,?,?)";
                        if (dbHelper.execOthers(sql, maxNid, imgs[i]) == 0) {
                            return -1; //图片插入失败、
                        }
                    }
                    return 1;//图片添加成功
                } else {
                    return -1; //图片添加失败
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;//图片添加异常
            }

        } else {
            //动态插入失败
            return -1;
        }

    }

    @Override
    public int delete(int id) {
        sql = "delete from news where nid = ?";
        return dbHelper.execOthers(sql, id);
    }

    @Override
    public List<News> getAll(int currPage, int uid) {
        List<News> list = new ArrayList<News>();
        //动态 ID 头像 ,介绍 ，内容
        sql = "select nid , head ,nickname,  intro , content  ,date from news , users where news.uid = users.uid order by date desc limit ?,?";
        rs = dbHelper.execQuery(sql, (currPage - 1) * 10, 10);//分页算法 每页 5条
        try {
            while (rs.next()) {
                //每一条动态
                News news = new News();
                int nid = rs.getInt(1);
                //动态ID
                news.setNid(nid);
                //动态的用户头像
                news.setHead(rs.getString(2));
                //动态的用户昵称
                news.setNickname(rs.getString(3));
                //动态用户的介绍
                news.setIntro(rs.getString(4));
                //动态用户的介绍
                news.setContent(rs.getString(5));
                //动态时间
                news.setDate(ConvertTime.convert(rs.getString(6)));

                //点赞数 收藏数 评论数

                news.setColNum(getColNum(nid));
                news.setZanNum(getZanNum(nid));
                news.setComNum(getComNum(nid));

                news.setZanStatue(getZanStatue(uid, nid));
                news.setColStatue(getColStatue(uid, nid));

                //获取对应的图片
                List<String> imgs = new ArrayList<String>(); //图片列表

                sql = "select imgname from images where nid = ? limit 0,3 ";
                ResultSet _rs = dbHelper.execQuery(sql, nid);
                while (_rs.next()) {
                    imgs.add(_rs.getString(1));//将图片加入图片列表中
                }
                news.setImgs(imgs);
                list.add(news);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public String getNewsDetailById(int id, int uid) {

        dbHelper = new DBHelper();
        //动态详情
        JsonObject object = new JsonObject();
        //动态 ID 头像 ,介绍 ，内容
        sql = "select nid , head ,nickname, intro , content ,date,users.uid  from news , users where news.uid = users.uid and nid = ?";
        rs = dbHelper.execQuery(sql, id);
        try {
            if (rs.next()) {
                int nid = rs.getInt(1);
                //动态ID
                object.addProperty("nid", nid);
                //动态的用户头像
                object.addProperty("head", rs.getString(2));
                //动态的用户昵称
                object.addProperty("nickname", rs.getString(3));
                //动态用户的介绍
                object.addProperty("intro", rs.getString(4));
                //动态用户的介绍
                object.addProperty("content", rs.getString(5));
                object.addProperty("date", ConvertTime.convert(rs.getString(6)));
                int _uid = rs.getInt(7);

                 /*收藏数 通过查询收藏表获取收藏数*/
                object.addProperty("colnum", getColNum(nid));


                //评论数 通过查询评论表获取 数量
                object.addProperty("comnum", getComNum(nid));
                /*点赞数 通过查询点赞表获取点赞数*/
                object.addProperty("zannum", getZanNum(nid));

                //获取是否已经赞
                if (getZanStatue(uid, nid)) {
                    object.addProperty("zanstatue", "yes");
                } else {
                    object.addProperty("zanstatue", "no");
                }
                //获取是否已经收藏
                if (getColStatue(uid, nid)) {
                    object.addProperty("colstatue", "yes");
                } else {
                    object.addProperty("colstatue", "no");
                }

                //获取对应的图片
                sql = "select imgname from images where nid = ?";
                JsonObject _objectname = new JsonObject();
                ResultSet _rs = dbHelper.execQuery(sql, nid);
                int i = 1;
                while (_rs.next()) {
                    _objectname.addProperty("img" + i, _rs.getString(1));
                    i++;
                }
                //对应图片插入
                object.add("img", _objectname);

                //查出相关动态的相关评论信息
                sql = "select head, nickname , date , content  from comment ,users  where comment.uid = users.uid and nid = ? order by date desc";
                JsonArray com_array = new JsonArray();
                ResultSet com_rs = dbHelper.execQuery(sql, nid);
                while (com_rs.next()) {
                    JsonObject com_object = new JsonObject();
                    com_object.addProperty("chead", com_rs.getString(1));
                    com_object.addProperty("cnickname", com_rs.getString(2));
                    com_object.addProperty("cdate", ConvertTime.convert(com_rs.getString(3)));
                    com_object.addProperty("content", com_rs.getString(4));
                    com_array.add(com_object);
                }
                object.add("com_array", com_array); //加入相关评论

            }
            return object.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";

    }

    @Override
    public int updateZan(int uid, int nid) {
        //判断是否已经收藏
        sql = "select * from zan where uid = ? and nid = ?";
        try {
            if (dbHelper.execQuery(sql, uid, nid).next()) { //收藏过
                //删除
                sql = "delete from zan where uid = ? and nid = ?";
                if (dbHelper.execOthers(sql, uid, nid) > 0) {
                    return 1;
                }
            } else {
                sql = "insert into zan values(null,?,?)";
                if (dbHelper.execOthers(sql, uid, nid) > 0) {
                    return 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;

    }

    @Override
    public int updateCol(int uid, int nid) {
        //判断是否已经收藏
        sql = "select * from collect where uid = ? and nid = ?";
        try {
            if (dbHelper.execQuery(sql, uid, nid).next()) { //收藏过
                //删除
                sql = "delete from collect where uid = ? and nid = ?";
                if (dbHelper.execOthers(sql, uid, nid) > 0) {
                    return 1;
                }
            } else {
                sql = "insert into collect values(null,?,?)";
                if (dbHelper.execOthers(sql, uid, nid) > 0) {
                    return 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;

    }

    @Override
    public List<News> queryNewsByUid(int uid) {
        List<News> list = new ArrayList<News>();
        //动态 ID 头像 ,介绍 ，内容
        sql = "select nid , head ,nickname,  intro , content  ,date from news , users where news.uid = users.uid and users.uid = ? order by date desc";
        rs = dbHelper.execQuery(sql, uid);
        try {
            while (rs.next()) {
                //每一条动态
                News news = new News();

                int nid = rs.getInt(1);
                //动态ID
                news.setNid(nid);
                //动态的用户头像
                news.setHead(rs.getString(2));
                //动态的用户昵称
                news.setNickname(rs.getString(3));
                //动态用户的介绍
                news.setIntro(rs.getString(4));
                //动态用户的介绍
                news.setContent(rs.getString(5));
                //动态时间
                news.setDate(ConvertTime.convert(rs.getString(6)));
                //点赞数 收藏数 评论数

                news.setColNum(getColNum(nid));
                news.setZanNum(getZanNum(nid));
                news.setComNum(getComNum(nid));

                news.setZanStatue(getZanStatue(uid, nid));
                news.setColStatue(getColStatue(uid, nid));

                //获取对应的图片
                List<String> imgs = new ArrayList<String>(); //图片列表

                sql = "select imgname from images where nid = ? limit 0,3 ";
                ResultSet _rs = dbHelper.execQuery(sql, nid);
                while (_rs.next()) {
                    imgs.add(_rs.getString(1));//将图片加入图片列表中
                }
                news.setImgs(imgs);
                list.add(news);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public List<News> queryColByUid(int uid) {
        List<News> list = new ArrayList<News>();
        //根据用户ID查出动态ID
        sql = "select nid from collect where  uid = ? order by colid desc";
        rs = dbHelper.execQuery(sql, uid);
        List<News> newsList = new ArrayList<News>();
        try {

            while (rs.next()) {
                //如果该用户收藏了
                int nid = rs.getInt(1); //获取动态ID
                //查出动态的相关信息 动态本身 和 发布人的信息
                String _sql = "select nid , head ,nickname,  intro , content  ,date from news , users where news.uid = users.uid and news.nid = ?";
                ResultSet _rs = dbHelper.execQuery(_sql, nid);
                if (_rs.next()) {
                    //去查询动态详情
                    //每一条动态
                    News news = new News();
                    //动态ID
                    news.setNid(_rs.getInt(1));
                    //动态的用户头像
                    news.setHead(_rs.getString(2));
                    //动态的用户昵称
                    news.setNickname(_rs.getString(3));
                    //动态用户的介绍
                    news.setIntro(_rs.getString(4));
                    //动态用户的介绍
                    news.setContent(_rs.getString(5));
                    //动态时间
                    news.setDate(ConvertTime.convert(_rs.getString(6)));
                    //点赞数 收藏数 评论数

                    news.setColNum(getColNum(nid));
                    news.setZanNum(getZanNum(nid));
                    news.setComNum(getComNum(nid));

                    news.setZanStatue(getZanStatue(uid, nid));
                    news.setColStatue(getColStatue(uid, nid));

                    //获取对应的图片
                    List<String> imgs = new ArrayList<String>(); //图片列表

                    String img_sql = "select imgname from images where nid = ? limit 0,3 ";
                    ResultSet img_rs = dbHelper.execQuery(img_sql, nid);
                    while (img_rs.next()) {
                        imgs.add(img_rs.getString(1));//将图片加入图片列表中
                    }
                    //动态相关的图片
                    news.setImgs(imgs);
                    newsList.add(news); //将动态加入动态列表中
                }
            }
            return newsList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }


    @Override
    public int updateComment(int uid, int nid, String content) {

        sql = "insert into comment values(null,?,?,?,sysdate())";
        if (dbHelper.execOthers(sql, uid, nid, content) > 0) {
            //评论成功
            return 1;
        }
        return 0;
    }

    private int getZanNum(int nid) {
                /*点赞数 通过查询点赞表获取点赞数*/
        String _sql = "select count(*) from zan where nid = ?";
        ResultSet _rs = dbHelper.execQuery(_sql, nid);
        try {
            if (_rs.next()) {
                //获取评论数
                return _rs.getInt(1);
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }


    }

    private boolean getZanStatue(int uid, int nid) {
        //获取是否已经赞
        String _sql = "select * from zan where uid = ? and nid = ?";
        try {
            if (dbHelper.execQuery(_sql, uid, nid).next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private int getColNum(int nid) {
         /*收藏数 通过查询收藏表获取收藏数*/
        String _sql = "select count(*) from collect where nid = ?";
        ResultSet _rs = dbHelper.execQuery(_sql, nid);
        try {
            if (_rs.next()) {
                //获取评论数
                return _rs.getInt(1);
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    private boolean getColStatue(int uid, int nid) {
        //获取是否已经收藏
        String _sql = "select * from collect where uid = ? and nid = ?";
        try {
            if (dbHelper.execQuery(_sql, uid, nid).next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private int getComNum(int nid) {
        //评论数 通过查询评论表获取 数量
        String _sql = "select count(*) from comment where nid = ?";
        ResultSet _rs = dbHelper.execQuery(_sql, nid);
        try {
            if (_rs.next()) {
                //获取评论数
                return _rs.getInt(1);
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
}
