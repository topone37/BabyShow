package com.tp.bsserver.dao.daoimpl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tp.bsserver.dao.FriendsDao;
import com.tp.bsserver.dbutil.DBHelper;
import com.tp.bsserver.model.Result;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2015/11/29.
 */
public class FriendsDaoImpl implements FriendsDao {
    private DBHelper dbHelper = new DBHelper();
    private String sql;//sql语句
    private ResultSet rs; //sql执行的结果集

    @Override
    public String queryById(int id) {
        JsonArray array = new JsonArray();
        sql = "select uid1 ,uid2  from friends where uid1 = ? or uid2 = ?";
        rs = dbHelper.execQuery(sql, id, id);
        try {
            while (rs.next()) {
                JsonObject object = null; //每一个朋友的信息
                //拿出朋友的ID
                int friendid = (rs.getInt(1) == id) ? rs.getInt(2) : rs.getInt(1); //逻辑不错哦
                String _sql = "select uid , uname ,head , nickname , intro from users where uid = ?";
                ResultSet _rs = dbHelper.execQuery(_sql, friendid);
                if (_rs.next()) {
                    object = new JsonObject();
                    object.addProperty("uid", _rs.getInt("uid"));

                    object.addProperty("uname", _rs.getString("uname"));
                    //根据用户id (融云中 uid uname 为同一个) 查询用户是否在线

                    object.addProperty("head", _rs.getString("head"));
                    object.addProperty("nickname", _rs.getString("nickname"));
                    object.addProperty("intro", _rs.getString("intro"));
                }

                array.add(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return array.toString();
    }

    @Override
    public int insert(int id1, int id2) { //id1 自己 id2 朋友
        //先判断该帐号是否已经存在
        sql = "select * from users where uid = ?";
        try {
            if (dbHelper.execQuery(sql, id2).next()) { //好友存在
                //再判断好友关系是否已经存在
                int tmp;
                if (id1 < id2) {
                    tmp = id1;
                    id1 = id2;
                    id2 = tmp;
                }
                sql = "select * from friends where uid1 = ? and uid2 = ?";
                if (!dbHelper.execQuery(sql, id1, id2).next()) {
                    sql = "insert into friends values (null,?,?)";
                    if (dbHelper.execOthers(sql, id1, id2) > 0) {
                        //添加成功
                        return 1;
                    } else {
                        return -1;
                    }

                } else {
                    //好友关系已经存在
                    return 0;
                }


            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
