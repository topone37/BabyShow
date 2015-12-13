package com.tp.bsserver.dao.daoimpl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tp.bsserver.dao.AlbumDao;
import com.tp.bsserver.dbutil.DBHelper;
import com.tp.bsserver.util.ConvertTime;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2015/12/5.
 */
public class AlbumDaoImpl implements AlbumDao {
    DBHelper dbHelper = new DBHelper();
    String sql;
    ResultSet rs;

    @Override
    public int insertAlbum(String aname, int uid) {
        sql = "insert into album values(null,?,?)";
        if (dbHelper.execOthers(sql, aname, uid) > 0) {
            //插入相册成功
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int insertPhoto(int aid, String imgname) {

        sql = "insert into photo values(null,?,?,sysdate())";
        if (dbHelper.execOthers(sql, imgname, aid) > 0) {
            //插入相册成功
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int delAlbumById(int aid) {
        sql = "delete from album where aid = ?";
        if (dbHelper.execOthers(sql, aid) > 0) {
            //删除相册成功
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public String queryAll(int uid) {
        sql = "select * from album where uid = ?";
        rs = dbHelper.execQuery(sql, uid);
        JsonArray array = new JsonArray();

        try {
            //伪装一个相册 用于点击 新建相册
            JsonObject newAlbum = new JsonObject();
            newAlbum.addProperty("aid", 0);
            newAlbum.addProperty("aname", "新建相册");
            newAlbum.addProperty("aphoto", "addpic.png");
            array.add(newAlbum);
            while (rs.next()) {
                //有数据
                JsonObject object = new JsonObject();
                int aid = rs.getInt(1);
                object.addProperty("aid", aid);
                object.addProperty("aname", rs.getString(2));
                String _sql = "select pname from photo where aid = ? order by pdate desc limit 0,1";
                ResultSet _rs = dbHelper.execQuery(_sql, aid);
                if (_rs.next()) {
                    object.addProperty("aphoto", _rs.getString(1));
                }
                array.add(object);
            }
            return array.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String queryAlbumById(int id) {
        //通过相册ID查询所有的相册的图片
        sql = "select *  from  photo where aid = ? order by pdate desc";
        rs = dbHelper.execQuery(sql, id);

        JsonArray array = new JsonArray();
        try {

            while (rs.next()) {
                JsonObject object = new JsonObject();
                //一张张 图片
                object.addProperty("pid", rs.getInt(1));
                object.addProperty("pname", rs.getString(2));
                object.addProperty("aid", rs.getInt(3));
                object.addProperty("pdate", ConvertTime.formatTime(rs.getString(4)));

                array.add(object);
            }
            return array.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateAlbum(int aid, String name) {
        System.out.println(">>>>>>>>>updateAlbum");
        sql = "update album set aname = ? where aid = ?";
        if (dbHelper.execOthers(sql, name, aid) > 0) {
            return 1;
        }
        return 0;
    }
}
