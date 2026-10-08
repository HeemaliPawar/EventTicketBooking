<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.eventticket.bean.User" %>
<%
    User authUser = (User) session.getAttribute("user");
    String uri = request.getRequestURI();
%>
<header class="nav">
    <a class="brand" href="${pageContext.request.contextPath}/home">EVENT<span>HUB</span></a>
    <nav class="nav-links">
        <a class="<%= uri.contains("/home") || uri.endsWith("/") ? "active" : "" %>" href="${pageContext.request.contextPath}/home">Home</a>
        <a class="<%= uri.contains("/events") || uri.contains("/event") ? "active" : "" %>" href="${pageContext.request.contextPath}/events">Events</a>
        <% if (authUser != null) { %>
            <a href="${pageContext.request.contextPath}/my-bookings">My Bookings</a>
            <% if (authUser.isAdmin()) { %>
                <a href="${pageContext.request.contextPath}/admin/dashboard">Admin</a>
            <% } %>
            <span class="muted"><%= authUser.getFullName() %></span>
            <a class="btn ghost small" href="${pageContext.request.contextPath}/logout">Logout</a>
        <% } else { %>
            <a href="${pageContext.request.contextPath}/login">Login</a>
            <a class="btn small" href="${pageContext.request.contextPath}/register">Register</a>
        <% } %>
    </nav>
</header>
