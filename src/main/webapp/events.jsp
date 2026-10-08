<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.eventticket.bean.Event" %>
<%
    List<Event> events = (List<Event>) request.getAttribute("events");
    String q = (String) request.getAttribute("q");
    String category = (String) request.getAttribute("category");
    if (q == null) q = "";
    if (category == null) category = "ALL";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Events | EventHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<section class="section">
    <h2>All events</h2>
    <form class="search-bar" method="get" action="${pageContext.request.contextPath}/events">
        <input name="q" placeholder="Search title, city or venue" value="<%= q %>">
        <select name="category">
            <option value="ALL" <%= "ALL".equals(category) ? "selected" : "" %>>All categories</option>
            <option <%= "Concert".equals(category) ? "selected" : "" %>>Concert</option>
            <option <%= "Sports".equals(category) ? "selected" : "" %>>Sports</option>
            <option <%= "Theatre".equals(category) ? "selected" : "" %>>Theatre</option>
            <option <%= "Conference".equals(category) ? "selected" : "" %>>Conference</option>
            <option <%= "Festival".equals(category) ? "selected" : "" %>>Festival</option>
            <option <%= "Comedy".equals(category) ? "selected" : "" %>>Comedy</option>
        </select>
        <button class="btn" type="submit">Search</button>
    </form>
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert error"><%= request.getAttribute("error") %></div>
    <% } %>
    <div class="grid">
        <% if (events != null && !events.isEmpty()) {
            for (Event event : events) { %>
        <article class="card">
            <div class="banner cat-<%= event.getCategory() %>"><%= event.getCategory() %></div>
            <div class="card-body">
                <h3><%= event.getTitle() %></h3>
                <p class="meta"><%= event.getVenue() %>, <%= event.getCity() %></p>
                <p class="meta"><%= event.getEventDate() %> · <%= event.getEventTime() %></p>
                <p class="meta"><%= event.getAvailableSeats() %> seats left</p>
                <p class="price">₹ <%= event.getPrice() %></p>
                <a class="btn small" href="${pageContext.request.contextPath}/event?id=<%= event.getEventId() %>">Details</a>
            </div>
        </article>
        <%  }
           } else { %>
        <p class="muted">No events found.</p>
        <% } %>
    </div>
</section>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
