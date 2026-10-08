<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventticket.bean.Event" %>
<%
    if (request.getAttribute("events") == null) {
        request.getRequestDispatcher("/home").forward(request, response);
        return;
    }
    List<Event> events = (List<Event>) request.getAttribute("events");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>EventHub | Book Live Events</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<section class="hero">
    <div>
        <h1>Book unforgettable events in one click.</h1>
        <p>Concerts, sports, theatre and festivals. Search events, pick seats, and get instant booking confirmation with full CRUD on the admin side.</p>
        <a class="btn" href="${pageContext.request.contextPath}/events">Browse Events</a>
        <a class="btn ghost" href="${pageContext.request.contextPath}/register">Create Account</a>
    </div>
    <div class="ticket-card">
        <h3>E-TICKET</h3>
        <p class="muted">Admit One · EventHub</p>
        <p style="margin-top:16px">Date: Your night, your seat</p>
        <p>Venue: Across India</p>
        <p>Status: Instant confirmation</p>
    </div>
</section>
<section class="section">
    <h2>Upcoming events</h2>
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert error"><%= request.getAttribute("error") %></div>
    <% } %>
    <div class="grid">
        <% if (events != null) {
            for (Event event : events) { %>
        <article class="card">
            <div class="banner cat-<%= event.getCategory() %>"><%= event.getCategory() %></div>
            <div class="card-body">
                <h3><%= event.getTitle() %></h3>
                <p class="meta"><%= event.getVenue() %>, <%= event.getCity() %></p>
                <p class="meta"><%= event.getEventDate() %> · <%= event.getEventTime() %></p>
                <p class="price">₹ <%= event.getPrice() %></p>
                <a class="btn small" href="${pageContext.request.contextPath}/event?id=<%= event.getEventId() %>">View &amp; Book</a>
            </div>
        </article>
        <%  }
           } %>
    </div>
</section>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
