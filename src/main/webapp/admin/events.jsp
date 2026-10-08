<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventticket.bean.Event" %>
<%
    List<Event> events = (List<Event>) request.getAttribute("events");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Events | EventHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<section class="section">
    <h2>Event CRUD</h2>
    <p style="margin-bottom:16px">
        <a class="btn" href="${pageContext.request.contextPath}/admin/events?action=new">Add event</a>
        <a class="btn ghost" href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
    </p>
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert error"><%= request.getAttribute("error") %></div>
    <% } %>
    <div class="table-wrap">
        <table>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Category</th>
                <th>Date</th>
                <th>Price</th>
                <th>Seats</th>
                <th>Actions</th>
            </tr>
            <% if (events != null) {
                for (Event e : events) { %>
            <tr>
                <td><%= e.getEventId() %></td>
                <td><%= e.getTitle() %></td>
                <td><%= e.getCategory() %></td>
                <td><%= e.getEventDate() %></td>
                <td>₹ <%= e.getPrice() %></td>
                <td><%= e.getAvailableSeats() %>/<%= e.getTotalSeats() %></td>
                <td>
                    <a class="btn small ghost" href="${pageContext.request.contextPath}/admin/events?action=edit&id=<%= e.getEventId() %>">Edit</a>
                    <a class="btn small danger" href="${pageContext.request.contextPath}/admin/events?action=delete&id=<%= e.getEventId() %>"
                       onclick="return confirm('Delete this event?');">Delete</a>
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
