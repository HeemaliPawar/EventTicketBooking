package com.eventticket.servlet;

import com.eventticket.bean.User;
import com.eventticket.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = trim(req.getParameter("fullName"));
        String email = trim(req.getParameter("email"));
        String phone = trim(req.getParameter("phone"));
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password == null || password.length() < 6) {
            req.setAttribute("error", "Please fill all fields. Password must be at least 6 characters.");
            keep(req, name, email, phone);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }
        if (!password.equals(confirm)) {
            req.setAttribute("error", "Passwords do not match.");
            keep(req, name, email, phone);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }
        try {
            if (userDAO.emailExists(email)) {
                req.setAttribute("error", "This email is already registered.");
                keep(req, name, email, phone);
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
                return;
            }
            User user = new User();
            user.setFullName(name);
            user.setEmail(email);
            user.setPhone(phone);
            user.setPassword(password);
            userDAO.register(user);
            req.setAttribute("success", "Account created. Please login.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Registration failed. Check database connection.");
            keep(req, name, email, phone);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }

    private void keep(HttpServletRequest req, String name, String email, String phone) {
        req.setAttribute("fullName", name);
        req.setAttribute("email", email);
        req.setAttribute("phone", phone);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
