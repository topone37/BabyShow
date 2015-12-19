package com.tp.bsserver.servlet;

import com.jspsmart.upload.SmartFile;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.tp.bsserver.biz.AlbumBiz;
import com.tp.bsserver.biz.bizimpl.AlbumBizImpl;

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
 * Created by Administrator on 2015/12/5.
 */
@WebServlet(name = "AlbumServlet")
public class AlbumServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        AlbumBiz albumBiz = new AlbumBizImpl();
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            //新建相册
            String aname = request.getParameter("aname");
            int uid = Integer.valueOf(request.getParameter("uid"));

            if (albumBiz.addAlbum(aname, uid) > 0) {
                out.print("1");
            } else {
                out.print("0");
            }
        } else if ("rename".equals(action)) {
            //删除相册
            int aid = Integer.valueOf(request.getParameter("aid").trim());
            String aname = request.getParameter("aname").trim();
            if (albumBiz.renameAlbum(aid, aname) > 0) {
                out.print("1");
            } else {
                out.print("0");
            }
        } else if ("del".equals(action)) {
            //删除相册
            int aid = Integer.valueOf(request.getParameter("aid").trim());
            if (albumBiz.removeAlbumById(aid) > 0) {
                out.print("1");
            } else {
                out.print("0");
            }
        } else if ("getAll".equals(action)) {
            int uid = Integer.valueOf(request.getParameter("uid"));
            System.out.println("uid" + uid);
            //得到所有的相册
            out.print(albumBiz.getAll(uid));
        } else if ("getPhoto".equals(action)) {
            //通过相册ID得到相册的图片
            int id = Integer.valueOf(request.getParameter("aid"));
            System.out.println(">>>>>>>>>getPhoto" + id);
            out.print(albumBiz.getPhotosById(id));
            System.out.println("通过相册ID得到相册的图片" + albumBiz.getPhotosById(id));
        } else if ("addPhoto".equals(action)) {
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
                String imgname = ""; // 准备一个相应长度的String数组存放图片地址
                SmartFile file = su.getFiles().getFile(0);
                // 保存到对应位置
                imgname = file.getFileName();
                file.saveAs(path + "/" + imgname);

                //上传成功
                // 插入数据库
                //获取相册ID
                int aid = Integer.parseInt(su.getRequest().getParameter("aid"));
                String pcontent = su.getRequest().getParameter("pcontent");
                //获取动态内容
                if (albumBiz.addPhoto(imgname, aid, pcontent) > 0) {
                    //插入数据库成功
                    out.print(1);
                    return;
                } else {
                    out.print(-1);
                    return;
                }


            } catch (SQLException e1) {
                out.print(-1);
                e1.printStackTrace();
            } catch (SmartUploadException e1) {
                out.print(-1);
                e1.printStackTrace();

            }

        }


        out.flush();
        out.close();
        return;

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
