<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventticket.bean.Event" %>
<%@ page import="com.eventticket.bean.Booking" %>
<%
    Integer eventCount = (Integer) request.getAttribute("eventCount");
    Integer bookingCount = (Integer) request.getAttribute("bookingCount");
    Integer userCount = (Integer) request.getAttribute("userCount");
    List<Event> events = (List<Event>) request.getAttribute("events");
    List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard | EventHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<section class="section">
    <h2>Admin dashboard</h2>
    <p class="muted" style="margin-bottom:16px">
        <a href="${pageContext.request.contextPath}/admin/events">Manage events</a> ·
        <a href="${pageContext.request.contextPath}/admin/bookings">All bookings</a>
    </p>
    <div class="stats">
        <div class="stat"><b><%= eventCount == null ? 0 : eventCount %></b>Events</div>
        <div class="stat"><b><%= bookingCount == null ? 0 : bookingCount %></b>Bookings</div>
        <div class="stat"><b><%= userCount == null ? 0 : userCount %></b>Users</div>
    </div>
    <h3 style="margin:18px 0">Recent bookings</h3>
    <div class="table-wrap">
        <table>
            <tr><th>ID</th><th>User</th><th>Event</th><th>Status</th></tr>
            <% if (bookings != null) {
                int n = 0;
                for (Booking b : bookings) {
                    if (n++ == 8) break; %>
            <tr>
                <td>#<%= b.getBookingId() %></td>
                <td><%= b.getUserName() %></td>
                <td><%= b.getEventTitle() %></td>
                <td><span class="badge <%= b.isConfirmed() ? "ok" : "no" %>"><%= b.getStatus() %></span></td>
            </tr>
            <%  }
               } %>
        </table>
    </div>
</section>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
