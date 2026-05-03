package com.learninglog.controller;

import com.learninglog.dao.UserDao;
import com.learninglog.dao.UserDaoImp;
import com.learninglog.entity.User;
import com.learninglog.util.PasswordUtil;
import com.learninglog.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private  final UserDao userDao = new UserDaoImp();

    @Override
    protected void doGet(HttpServletRequest request , HttpServletResponse  response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request,response);
    }


    @Override
    protected  void doPost(HttpServletRequest request , HttpServletResponse response)
                    throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmpassword = request.getParameter("confirmed_password");
        String phone = request.getParameter("phone");

        ArrayList<String> error = new ArrayList<>();

//        aPPLYING Validation
//        user
        if (!ValidationUtil.isValidUsername(username)){
            error.add("User Name must Be Alphabetic at start  and Shoulde be atleast that 5 character");
        }

//        email
        if(!ValidationUtil.isValidEmail(email)){
            error.add("Invalid Email Format");
        }

//       password
        String passwordError = ValidationUtil.ValidatePassword(password);
        if (passwordError != null) {
            error.add(passwordError);
        }

//        conformed password
        if(!ValidationUtil.doPasswordMatch(password,confirmpassword)){
            error.add("Password and Confirmation password doesnt match");
        }

//      Phone numner validation
        if(!ValidationUtil.isValidPhoneNumber(phone)){
            error.add("Phone Number must be 10 digits and Numeric");
        }

//        forwarding to Home page if valid if not get back to register servlet


//        if forward back to register page
        if(!error.isEmpty()){
            request.setAttribute("error",error.toString().trim());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request,response);
            return;
        }

//        If no error Forward back to main home(index) page

        //        Password encoding
        String encrypted_pass = PasswordUtil.getHashpassword(password);
        User user = new User(username,email,encrypted_pass,phone);

//        inserting to User Database
        boolean sucess = userDao.insertUser(user);




//        checking if any user exist already of same given property
        if(!sucess){
            request.setAttribute("error","User Name or Email already Exist.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request,response);
        }

//        if no insert issue diver to login page
       response.sendRedirect(request.getContextPath() + "/login");

    }
}
