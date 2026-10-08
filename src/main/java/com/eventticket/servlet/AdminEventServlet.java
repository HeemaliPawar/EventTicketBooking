package com.eventticket.servlet;

import com.eventticket.bean.Event;
import com.eventticket.dao.EventDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

@WebServlet("/admin/events")
public class AdminEventServlet extends HttpServlet {
    private final EventDAO eventDAO = new EventDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AdminDashboardServlet.isAdmin(req, resp)) {
            return;
        }
        String action = req.getParameter("action");
        try {
            if ("edit".equals(action)) {
                Event event = eventDAO.findById(Integer.parseInt(req.getParameter("id")));
                req.setAttribute("event", event);
                req.getRequestDispatcher("/admin/event-form.jsp").forward(req, resp);
                return;
            }
            if ("new".equals(action)) {
                req.getRequestDispatcher("/admin/event-form.jsp").forward(req, resp);
                return;
            }
            if ("delete".equals(action)) {
                eventDAO.delete(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(req.getContextPath() + "/admin/events");
                return;
            }
            req.setAttribute("events", eventDAO.findAll());
            req.getRequestDispatcher("/admin/events.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Could not complete event operation. Delete related bookings first if needed.");
            try {
                req.setAttribute("events", eventDAO.findAll());
            } catch (Exception ignored) {
            }
            req.getRequestDispatcher("/admin/events.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AdminDashboardServlet.isAdmin(req, resp)) {
            return;
        }
        try {
            Event event = new Event();
            String id = req.getParameter("eventId");
            event.setTitle(req.getParameter("title"));
            event.setCategory(req.getParameter("category"));
            event.setDescription(req.getParameter("description"));
            event.setVenue(req.getParameter("venue"));
            event.setCity(req.getParameter("city"));
            event.setEventDate(Date.valueOf(req.getParameter("eventDate")));
            String time = req.getParameter("eventTime");
            if (time != null && time.length() == 5) {
                time = time + ":00";
            }
            event.setEventTime(Time.valueOf(time));
            event.setPrice(new BigDecimal(req.getParameter("price")));
            event.setTotalSeats(Integer.parseInt(req.getParameter("totalSeats")));
            String available = req.getParameter("availableSeats");
            event.setAvailableSeats(available == null || available.isEmpty()
                    ? event.getTotalSeats()
                    : Integer.parseInt(available));

            if (id == null || id.isEmpty()) {
                eventDAO.insert(event);
            } else {
                event.setEventId(Integer.parseInt(id));
                eventDAO.update(event);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/events");
        } catch (Exception e) {
            req.setAttribute("error", "Please check all event fields.");
            req.getRequestDispatcher("/admin/event-form.jsp").forward(req, resp);
        }
    }
}
