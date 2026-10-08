package com.eventticket.servlet;

import com.eventticket.dao.EventDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private final EventDAO eventDAO = new EventDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("events", eventDAO.findUpcoming(6));
        } catch (Exception e) {
            req.setAttribute("events", new java.util.ArrayList<com.eventticket.bean.Event>());
            req.setAttribute("error", "Could not load events. Import database/event_ticket.sql and check DBConnection.java.");
        }
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
