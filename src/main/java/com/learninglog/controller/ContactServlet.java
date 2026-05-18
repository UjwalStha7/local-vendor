package com.learninglog.controller;

import com.learninglog.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

    private static final int MAX_LEN = 4000;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("1".equals(request.getParameter("sent"))) {
            request.setAttribute("messageSent", Boolean.TRUE);
        }
        request.getRequestDispatcher("/WEB-INF/views/customer/contact.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = trimToNull(request.getParameter("name"));
        String email = trimToNull(request.getParameter("email"));
        String subject = trimToNull(request.getParameter("subject"));
        String message = trimToNull(request.getParameter("message"));

        if (name == null || email == null || subject == null || message == null) {
            request.setAttribute("error", "Please fill in all fields.");
            repopulate(request, name, email, subject, message);
            request.getRequestDispatcher("/WEB-INF/views/customer/contact.jsp")
                    .forward(request, response);
            return;
        }

        if (name.length() > 200 || subject.length() > 300 || message.length() > MAX_LEN) {
            request.setAttribute("error", "One or more fields are too long.");
            repopulate(request, name, email, subject, message);
            request.getRequestDispatcher("/WEB-INF/views/customer/contact.jsp")
                    .forward(request, response);
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Please enter a valid email address.");
            repopulate(request, name, email, subject, message);
            request.getRequestDispatcher("/WEB-INF/views/customer/contact.jsp")
                    .forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/contact?sent=1");
    }

    private static void repopulate(HttpServletRequest request, String name, String email,
                                   String subject, String message) {
        request.setAttribute("fieldName", name != null ? name : "");
        request.setAttribute("fieldEmail", email != null ? email : "");
        request.setAttribute("fieldSubject", subject != null ? subject : "");
        request.setAttribute("fieldMessage", message != null ? message : "");
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String t = value.trim();
        return t.isEmpty() ? null : t;
    }
}
