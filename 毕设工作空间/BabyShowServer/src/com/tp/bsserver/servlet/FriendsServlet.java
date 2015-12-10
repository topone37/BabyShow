package com.tp.bsserver.servlet;

import com.tp.bsserver.biz.FriendsBiz;
import com.tp.bsserver.biz.bizimpl.FriendsBizImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2015/11/29.
 */
@WebServlet(name = "FriendsServlet")
public class FriendsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FriendsBiz friendsBiz = new FriendsBizImpl();
        String action = request.getParameter("action");
        PrintWriter out = response.getWriter();
        if ("find".equals(action)) {
            //通过ID找到所有的朋友
            int id = Integer.valueOf(request.getParameter("id").trim());
            String strFriends = friendsBiz.findFriendsById(id);
            if (strFriends != null) {
                out.print(strFriends);
            } else {
                out.print("");
            }
            return;
        } else if ("add".equals(action)) {
            int id1 = Integer.parseInt(request.getParameter("id1").trim());
            int id2 = Integer.parseInt(request.getParameter("id2").trim());
            int rs = friendsBiz.addFriends(id1, id2);
            if (rs > 0) {
                out.print("1");
            } else if (rs == 0) {
                out.print("0");
            } else {
                out.print("-1");
            }
            return;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
