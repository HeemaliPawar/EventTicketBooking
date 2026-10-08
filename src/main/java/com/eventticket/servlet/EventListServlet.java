package com.eventticket.servlet;

import com.eventticket.dao.EventDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/events")
public class EventListServlet extends HttpServlet {
    private final EventDAO eventDAO = new EventDAO();

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String q = req.getParameter("q");
//        String category = req.getParameter("category");
//        try {
//            req.setAttribute("events", eventDAO.search(q, category));
//        } catch (Exception e) {
//            req.setAttribute("error", "Unable to load events.");
//        }
//        req.setAttribute("q", q);
//        req.setAttribute("category", category);
//        req.getRequestDispatcher("/events.jsp").forward(req, resp);
//    }
    
    @Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    String q = req.getParameter("q");
    String category = req.getParameter("category");

    try {
        req.setAttribute("events", eventDAO.search(q, category));
    } catch (Exception e) {
        e.printStackTrace();
        req.setAttribute("error", "Unable to load events: " + e.getMessage());
    }

    req.setAttribute("q", q);
    req.setAttribute("category", category);

    req.getRequestDispatcher("/events.jsp").forward(req, resp);
}
}
