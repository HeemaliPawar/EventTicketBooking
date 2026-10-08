package com.eventticket.servlet;

import com.eventticket.bean.User;
import com.eventticket.dao.BookingDAO;
import com.eventticket.dao.EventDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private final EventDAO eventDAO = new EventDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAdmin(req, resp)) {
            return;
        }
        try {
            req.setAttribute("eventCount", eventDAO.findAll().size());
            req.setAttribute("bookingCount", bookingDAO.countBookings());
            req.setAttribute("userCount", bookingDAO.countUsers());
            req.setAttribute("events", eventDAO.findUpcoming(5));
            req.setAttribute("bookings", bookingDAO.findAll());
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load dashboard.");
        }
        req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
    }

    static boolean isAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        if (!user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/events");
            return false;
        }
        return true;
    }
}
