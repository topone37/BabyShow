package com.tp.bsserver.dao.daoimpl;

import com.tp.bsserver.common.Common;
import com.tp.bsserver.dao.UsersDao;
import com.tp.bsserver.dbutil.DBHelper;
import com.tp.bsserver.model.FormatType;
import com.tp.bsserver.model.Result;
import com.tp.bsserver.model.SdkHttpResult;
import com.tp.bsserver.po.Users;
import com.tp.bsserver.util.ApiHttpClient;
import com.tp.bsserver.util.GsonUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2015/11/15.
 */
public class UsersDaoImpl implements UsersDao {
    private DBHelper dbHelper = new DBHelper();
    private String sql;//sql语句
    private ResultSet rs; //sql执行的结果集
    private int affectRows = 0;//sql执行之后受影响的行数
    private Users user;

    @Override
    public Users query(String username, String password) {
        sql = "select * from users where uname = ? and upass = ?";
        rs = dbHelper.execQuery(sql, username, password);

        try {
            if (rs.next()) {
                user = new Users();
                user.setUid(rs.getInt("uid"));
                user.setUname(rs.getString("uname"));
                user.setUpass(rs.getString("upass"));
                user.setToken(rs.getString("token"));
                user.setAge(rs.getInt("age"));
                user.setSex(rs.getString("sex"));
                user.setHead(rs.getString("head"));
                user.setNickname(rs.getString("nickname"));
                user.setIntro(rs.getString("intro"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbHelper.closeAll();
        }
        return null;
    }

    @Override
    public int insert(String username, String password) {
        //在注册的时候取得token 一并插入数据库

        try {
            SdkHttpResult result = ApiHttpClient.getToken(Common.APP_KEY, Common.SECRET, username, username,
                    null, FormatType.json);
            Result rs = (Result) GsonUtil.fromJson(result.getResult(), Result.class);
            String token = rs.getToken();
            sql = "insert into users(uname,upass,token,sex,age,nickname,intro,head) values (?,?,?,'男',21,'无名氏','这家伙太懒了,什么也没有留下...', 'a2.png')";
            if (dbHelper.execOthers(sql, username, password, token) > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    @Override
    public int updateHead(int id, String imgname) {
        sql = "update users set head = ? where uid = ?";
        if (dbHelper.execOthers(sql, imgname, id) > 0) {
            //更新成功
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int updateInfo(int id, String nickname, String sex, String intro) {
        sql = "update users set nickname =? , sex = ? ,intro = ? where uid = ?";
        if (dbHelper.execOthers(sql, nickname, sex, intro, id) > 0) {
            //更新个人信息成功
            return 1;
        } else {
            return 0;
        }
    }
}
