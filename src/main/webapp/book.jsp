<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.eventticket.bean.Event" %>
<%
    Event event = (Event) request.getAttribute("event");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book Tickets | EventHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="form-wrap">
    <h2>Confirm booking</h2>
    <% if (event != null) { %>
        <p><b><%= event.getTitle() %></b></p>
        <p class="muted"><%= event.getVenue() %>, <%= event.getCity() %></p>
        <p class="muted"><%= event.getEventDate() %> · ₹ <%= event.getPrice() %> each</p>
        <p class="muted"><%= event.getAvailableSeats() %> seats available</p>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert error"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" action="${pageContext.request.contextPath}/book">
            <input type="hidden" name="eventId" value="<%= event.getEventId() %>">
            <label>Number of tickets</label>
            <input type="number" name="tickets" min="1" max="10" value="1" required>
            <br><br>
            <button class="btn" type="submit">Pay &amp; Confirm</button>
        </form>
    <% } else { %>
        <p class="muted">Event not found.</p>
    <% } %>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
