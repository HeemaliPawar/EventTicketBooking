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
    <title>All Bookings | EventHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<section class="section">
    <h2>All bookings</h2>
    <p style="margin-bottom:16px"><a class="btn ghost" href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></p>
    <div class="table-wrap">
        <table>
            <tr>
                <th>ID</th>
                <th>User</th>
                <th>Event</th>
                <th>Tickets</th>
                <th>Amount</th>
                <th>Status</th>
                <th></th>
            </tr>
            <% if (bookings != null) {
                for (Booking b : bookings) { %>
            <tr>
                <td>#<%= b.getBookingId() %></td>
                <td><%= b.getUserName() %><br><span class="muted"><%= b.getUserEmail() %></span></td>
                <td><%= b.getEventTitle() %></td>
                <td><%= b.getTickets() %></td>
                <td>₹ <%= b.getTotalAmount() %></td>
                <td><span class="badge <%= b.isConfirmed() ? "ok" : "no" %>"><%= b.getStatus() %></span></td>
                <td>
                    <% if (b.isConfirmed()) { %>
                    <form method="post" action="${pageContext.request.contextPath}/admin/bookings">
                        <input type="hidden" name="bookingId" value="<%= b.getBookingId() %>">
                        <button class="btn danger small" type="submit">Cancel</button>
                    </form>
                    <% } %>
                </td>
            </tr>
            <%  }
               } %>
        </table>
    </div>
</section>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
