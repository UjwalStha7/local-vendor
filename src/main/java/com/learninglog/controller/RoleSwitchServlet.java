package com.learninglog.controller;

import com.learninglog.dao.UserDao;
import com.learninglog.dao.UserDaoImp;
import com.learninglog.entity.User;
import com.learninglog.util.LoginAuthUtil;
import com.learninglog.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Switch active role between customer (shop) and vendor (sell) for approved farmers.
 */
@WebServlet("/account/switch-role")
public class RoleSwitchServlet extends HttpServlet {

    private final UserDao userDao = new UserDaoImp();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object userObj = SessionUtil.getAttribute(req, "user");
        if (!(userObj instanceof User sessionUser)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String targetRole = LoginAuthUtil.normalizeRole(req.getParameter("role"));
        if (!"customer".equals(targetRole) && !"vendor".equals(targetRole)) {
            resp.sendRedirect(req.getContextPath() + "/unauthorized");
            return;
        }

        if ("vendor".equals(targetRole) && !userDao.hasVendorProfile(sessionUser.getId())) {
            resp.sendRedirect(req.getContextPath() + "/farmer/apply");
            return;
        }

        if (!userDao.updateRole(sessionUser.getId(), targetRole)) {
            resp.sendRedirect(req.getContextPath() + LoginAuthUtil.redirectPathForRole(
                    LoginAuthUtil.normalizeRole(sessionUser.getRole())));
            return;
        }

        User refreshed = userDao.findById(sessionUser.getId());
        if (refreshed == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        SessionUtil.setAttribute(req, "user", refreshed);
        SessionUtil.setAttribute(req, "role", targetRole);
        SessionUtil.setAttribute(req, "hasVendorProfile", userDao.hasVendorProfile(refreshed.getId()));

        resp.sendRedirect(req.getContextPath() + LoginAuthUtil.redirectPathForRole(targetRole));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
