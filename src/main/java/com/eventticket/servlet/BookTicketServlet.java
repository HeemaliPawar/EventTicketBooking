package com.eventticket.servlet;

import com.eventticket.bean.Event;
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

@WebServlet("/book")
public class BookTicketServlet extends HttpServlet {
    private final EventDAO eventDAO = new EventDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = currentUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            Event event = eventDAO.findById(Integer.parseInt(req.getParameter("eventId")));
            if (event == null) {
                resp.sendRedirect(req.getContextPath() + "/events");
                return;
            }
            req.setAttribute("event", event);
            req.getRequestDispatcher("/book.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/events");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = currentUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            int eventId = Integer.parseInt(req.getParameter("eventId"));
            int tickets = Integer.parseInt(req.getParameter("tickets"));
            Event event = eventDAO.findById(eventId);
            req.setAttribute("event", event);
            if (event == null) {
                resp.sendRedirect(req.getContextPath() + "/events");
                return;
            }
            if (tickets < 1 || tickets > 10) {
                req.setAttribute("error", "You can book between 1 and 10 tickets.");
                req.getRequestDispatcher("/book.jsp").forward(req, resp);
                return;
            }
            int bookingId = bookingDAO.create(user.getUserId(), eventId, tickets);
            if (bookingId <= 0) {
                req.setAttribute("error", "Not enough seats available.");
                req.getRequestDispatcher("/book.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("booking", bookingDAO.findById(bookingId));
            req.getRequestDispatcher("/booking-success.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Booking failed. Please try again.");
            req.getRequestDispatcher("/book.jsp").forward(req, resp);
        }
    }

    private User currentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (User) session.getAttribute("user");
    }
}
