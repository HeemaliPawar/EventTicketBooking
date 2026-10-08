<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.eventticket.bean.Booking" %>
<%
    Booking booking = (Booking) request.getAttribute("booking");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Booking Confirmed | EventHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="form-wrap">
    <h2>Booking confirmed</h2>
    <div class="alert ok">Your e-ticket is ready.</div>
    <% if (booking != null) { %>
        <p>Booking ID: <b>#<%= booking.getBookingId() %></b></p>
        <p><%= booking.getEventTitle() %></p>
        <p class="muted"><%= booking.getVenue() %>, <%= booking.getCity() %></p>
        <p class="muted"><%= booking.getEventDate() %> · <%= booking.getEventTime() %></p>
        <p>Tickets: <%= booking.getTickets() %></p>
        <p class="price">Paid ₹ <%= booking.getTotalAmount() %></p>
    <% } %>
    <br>
    <a class="btn" href="${pageContext.request.contextPath}/my-bookings">My bookings</a>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
