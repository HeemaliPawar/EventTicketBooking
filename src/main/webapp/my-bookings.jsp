<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventticket.bean.Booking" %>
<%
    List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Bookings | EventHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<section class="section">
    <h2>My bookings</h2>
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert error"><%= request.getAttribute("error") %></div>
    <% } %>
    <div class="table-wrap">
        <table>
            <tr>
                <th>ID</th>
                <th>Event</th>
                <th>When</th>
                <th>Tickets</th>
                <th>Amount</th>
                <th>Status</th>
                <th></th>
            </tr>
            <% if (bookings != null && !bookings.isEmpty()) {
                for (Booking b : bookings) { %>
            <tr>
                <td>#<%= b.getBookingId() %></td>
                <td><%= b.getEventTitle() %><br><span class="muted"><%= b.getVenue() %>, <%= b.getCity() %></span></td>
                <td><%= b.getEventDate() %> <%= b.getEventTime() %></td>
                <td><%= b.getTickets() %></td>
                <td>₹ <%= b.getTotalAmount() %></td>
                <td>
                    <span class="badge <%= b.isConfirmed() ? "ok" : "no" %>"><%= b.getStatus() %></span>
                </td>
                <td>
                    <% if (b.isConfirmed()) { %>
                    <form method="post" action="${pageContext.request.contextPath}/my-bookings" onsubmit="return confirm('Cancel this booking?');">
                        <input type="hidden" name="bookingId" value="<%= b.getBookingId() %>">
                        <button class="btn danger small" type="submit">Cancel</button>
                    </form>
                    <% } %>
                </td>
            </tr>
            <%  }
               } else { %>
            <tr><td colspan="7" class="muted">No bookings yet.</td></tr>
            <% } %>
        </table>
    </div>
</section>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
