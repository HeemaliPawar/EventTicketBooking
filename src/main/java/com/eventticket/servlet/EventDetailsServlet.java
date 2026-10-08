package com.eventticket.servlet;

import com.eventticket.bean.Event;
import com.eventticket.dao.EventDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/event")
public class EventDetailsServlet extends HttpServlet {
    private final EventDAO eventDAO = new EventDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Event event = eventDAO.findById(id);
            if (event == null) {
                resp.sendRedirect(req.getContextPath() + "/events");
                return;
            }
            req.setAttribute("event", event);
            req.getRequestDispatcher("/event-details.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/events");
        }
    }
}
