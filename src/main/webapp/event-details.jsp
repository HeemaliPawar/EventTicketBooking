<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.eventticket.bean.Event" %>
<%
    Event event = (Event) request.getAttribute("event");
    if (event == null) {
        response.sendRedirect(request.getContextPath() + "/events");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= event.getTitle() %> | EventHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<section class="section details">
    <div class="card">
        <div class="banner cat-<%= event.getCategory() %>"><%= event.getCategory() %></div>
        <div class="card-body">
            <h2><%= event.getTitle() %></h2>
            <p style="margin:12px 0"><%= event.getDescription() %></p>
            <p class="meta">Venue: <%= event.getVenue() %>, <%= event.getCity() %></p>
            <p class="meta">Date: <%= event.getEventDate() %> at <%= event.getEventTime() %></p>
            <p class="meta">Seats: <%= event.getAvailableSeats() %> / <%= event.getTotalSeats() %></p>
        </div>
    </div>
    <div class="card">
        <div class="card-body">
            <p class="price" style="font-size:28px">₹ <%= event.getPrice() %></p>
            <p class="muted">per ticket</p>
            <br>
            <% if (event.isSoldOut()) { %>
                <span class="badge no">Sold out</span>
            <% } else { %>
                <a class="btn" href="${pageContext.request.contextPath}/book?eventId=<%= event.getEventId() %>">Book tickets</a>
            <% } %>
        </div>
    </div>
</section>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
