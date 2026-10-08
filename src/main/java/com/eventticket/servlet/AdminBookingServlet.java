package com.eventticket.servlet;

import com.eventticket.dao.BookingDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/bookings")
public class AdminBookingServlet extends HttpServlet {
    private final BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AdminDashboardServlet.isAdmin(req, resp)) {
            return;
        }
        try {
            req.setAttribute("bookings", bookingDAO.findAll());
        } catch (Exception e) {
            req.setAttribute("error", "Unable to load bookings.");
        }
        req.getRequestDispatcher("/admin/bookings.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AdminDashboardServlet.isAdmin(req, resp)) {
            return;
        }
        try {
            int bookingId = Integer.parseInt(req.getParameter("bookingId"));
            bookingDAO.cancel(bookingId, 0, true);
        } catch (Exception ignored) {
        }
        resp.sendRedirect(req.getContextPath() + "/admin/bookings");
    }
}
