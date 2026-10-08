package com.eventticket.servlet;

import com.eventticket.bean.User;
import com.eventticket.dao.BookingDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/my-bookings")
public class MyBookingsServlet extends HttpServlet {
    private final BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = currentUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            req.setAttribute("bookings", bookingDAO.findByUser(user.getUserId()));
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load bookings.");
        }
        req.getRequestDispatcher("/my-bookings.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = currentUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            int bookingId = Integer.parseInt(req.getParameter("bookingId"));
            bookingDAO.cancel(bookingId, user.getUserId(), false);
        } catch (Exception ignored) {
        }
        resp.sendRedirect(req.getContextPath() + "/my-bookings");
    }

    private User currentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (User) session.getAttribute("user");
    }
}
