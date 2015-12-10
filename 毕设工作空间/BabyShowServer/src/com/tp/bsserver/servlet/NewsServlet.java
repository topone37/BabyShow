package com.tp.bsserver.servlet;

import com.google.gson.reflect.TypeToken;
import com.jspsmart.upload.SmartFile;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.tp.bsserver.biz.NewsBiz;
import com.tp.bsserver.biz.bizimpl.NewsBizImpl;
import com.tp.bsserver.util.GsonUtil;
import com.tp.bsserver.vo.News;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2015/11/26.
 */
@WebServlet(name = "NewsServlet")
public class NewsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        NewsBiz newsBiz = new NewsBizImpl();
        if ("add".equals(action)) {
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
                int num = su.getFiles().getCount();
                String imgnames[] = new String[num]; // 准备一个相应长度的String数组存放图片地址
                for (int i = 0; i < num; i++) {
                    SmartFile file = su.getFiles().getFile(i);
                    if (file != null) {
                        // 保存到对应位置
                        imgnames[i] = file.getFileName();
                        file.saveAs(path + "/" + imgnames[i]);
                    } else {
                    }
                }
                //上传成功
                // 插入数据库
                //获取用户ID
                int uid = Integer.parseInt(su.getRequest().getParameter("uid"));
                //获取动态内容
                String content = su.getRequest().getParameter("content");

                if (newsBiz.add(uid, content, imgnames) > 0) {
                    //插入数据库成功
                    out.print(1);
                    return;
                } else {
                    out.print(-1);
                    return;
                }
            } catch (SecurityException e) {
                out.print("-1");
                e.printStackTrace();
            } catch (SmartUploadException e) {
                out.print("-1");
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            out.flush();
            out.close();
            return;
        } else if ("getAll".equals(action)) {
            //当前页
            int currPage = Integer.valueOf(request.getParameter("currPage").trim());
            List<News> news = newsBiz.getAll(currPage); //获取到动态数据
            Type type = new TypeToken<List<News>>() {
            }.getType(); //获取列表数据对应类型
            if (news != null) {
                String rs = GsonUtil.toJson(news, type);
                out.print(rs);
            } else {
                out.print("");
            }

        } else if ("getNewsById".equals(action)) {
            String id = request.getParameter("id");
            String uid = request.getParameter("uid");
            out.print(newsBiz.getNewsDetailById(Integer.valueOf(id), Integer.valueOf(uid)));
        } else if ("zan".equals(action)) {
            int uid = Integer.valueOf(request.getParameter("uid").trim());
            int nid = Integer.valueOf(request.getParameter("nid").trim());

            if (newsBiz.zan(uid, nid) > 0) {
                out.print("1");
            } else {
                out.print("");
            }

        } else if ("col".equals(action)) {
            int uid = Integer.valueOf(request.getParameter("uid").trim());
            int nid = Integer.valueOf(request.getParameter("nid").trim());

            if (newsBiz.col(uid, nid) > 0) {
                out.print("1");
            } else {
                out.print("");
            }

        } else if ("comment".equals(action)) {
            int uid = Integer.valueOf(request.getParameter("uid").trim());
            int nid = Integer.valueOf(request.getParameter("nid").trim());
            String content = request.getParameter("content");
            if (newsBiz.comment(uid, nid, content) > 0) {
                out.print("1");
            } else {
                out.print("");
            }


        } else if ("getNewsByUid".equals(action)) {

            int uid = Integer.valueOf(request.getParameter("uid").trim());
            List<News> news = newsBiz.getNewsByUid(uid);
            if (news != null) {
                Type type = new TypeToken<List<News>>() {
                }.getType(); //获取列表数据对应类型
                out.print(GsonUtil.toJson(news, type));
            } else {
                out.print("");
            }
        } else if ("getColByUid".equals(action)) {
            System.out.println(">>>>>我的收藏>>>>");

            int uid = Integer.valueOf(request.getParameter("uid").trim());
            List<News> news = newsBiz.getColByUid(uid);
            if (news != null) {
                Type type = new TypeToken<List<News>>() {
                }.getType(); //获取列表数据对应类型
                out.print(GsonUtil.toJson(news, type));
            } else {
                out.print("");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
