package com.tp.bsserver.servlet;

import com.jspsmart.upload.SmartFile;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.tp.bsserver.biz.UsersBiz;
import com.tp.bsserver.biz.bizimpl.UserBizImpl;
import com.tp.bsserver.po.Users;
import com.tp.bsserver.util.GsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by Administrator on 2015/11/6.
 */
@WebServlet(name = "UserServlet")
public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsersBiz usersBiz = new UserBizImpl();
        PrintWriter out = response.getWriter();
        //获取客户端传递的参数
        String action = request.getParameter("action"); //获取动作 登录、注册、修改等
        if ("login".equals(action)) {
            String username = request.getParameter("uname");
            String password = request.getParameter("uname");
            Users user = usersBiz.login(username, password);
            if (user == null) {
                out.print("");//该用户不存在
            } else {
                String strUser = GsonUtil.toJson(user, Users.class);
                out.print(strUser);
            }
        } else if ("regist".equals(action)) {
            //注册操作
            String username = request.getParameter("name");
            String password = request.getParameter("pass");
            //获取token异常 注册失败
            if (usersBiz.regist(username, password) > 0)
                out.print("1");
            else {
                out.print("0");
            }
            return;
        } else if ("updateHead".equals(action)) {
            //更新头像
            // 存储目录
            String path = this.getServletContext().getRealPath("photo");
            File fpath = new File(path);
            if (!fpath.exists()) {
                fpath.mkdirs();
            }

            // 上传文件
            SmartUpload su = new SmartUpload("utf-8");
            su.initialize(getServletConfig(), request, response);
            try {
                su.setDeniedFilesList(""); // 允许的上传类型
                su.setMaxFileSize(5000 * 1024); // 限定大小不能超过5M
                su.upload();
                // 获取上传的文件
                String imgname = "";
                SmartFile file = su.getFiles().getFile(0);
                if (file != null) {
                    // 保存到对应位置
                    imgname = file.getFileName();
                    file.saveAs(path + "/" + imgname);
                } else {
                }

                //上传成功
                // 插入数据库
                //获取用户ID
                int uid = Integer.parseInt(su.getRequest().getParameter("uid"));

                if (usersBiz.modifyHead(uid, imgname) > 0) {
                    //插入数据库成功
                    out.print(1);
                    return;
                } else {
                    out.print(-1);
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (SmartUploadException e) {
                e.printStackTrace();
            }

        } else if ("updateInfo".equals(action)) {
            //更新信息
            int uid = Integer.valueOf(request.getParameter("uid"));
            String nickname = request.getParameter("nickname");
            String sex = request.getParameter("sex");
            String intro = request.getParameter("intro");

            if (usersBiz.modifyInfo(uid, nickname, sex, intro) > 0) {
                out.print("1");
            } else {
                out.print("");
            }

        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
