package com.tp.bsserver.servlet;

import com.tp.bsserver.biz.AlbumBiz;
import com.tp.bsserver.biz.bizimpl.AlbumBizImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
            System.out.println("Add>>>>>>>>>>>>");
            //新建相册
            String aname = request.getParameter("aname");
            int uid = Integer.valueOf(request.getParameter("uid"));

            if (albumBiz.addAlbum(aname, uid) > 0) {
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
            System.out.println("getAll");
            int uid = Integer.valueOf(request.getParameter("uid"));
            System.out.println("uid" + uid);
            //得到所有的相册
            out.print(albumBiz.getAll(uid));
        } else if ("getPhoto".equals(action)) {
            //通过相册ID得到相册的图片\
            int id = Integer.valueOf(request.getParameter("aid"));
            out.print(albumBiz.getPhotosById(id));
        } else {
            out.print("");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
