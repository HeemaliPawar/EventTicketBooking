<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.eventticket.bean.Event" %>

<%
    Event event = (Event) request.getAttribute("event");
    boolean edit = event != null;
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= edit ? "Edit" : "Add" %> Event | EventHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>

<jsp:include page="/includes/header.jsp"/>

<div class="form-wrap" style="max-width:640px">

    <h2><%= edit ? "Update Event" : "Create Event" %></h2>

    <% if(request.getAttribute("error") != null){ %>
        <div class="alert error">
            <%= request.getAttribute("error") %>
        </div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/admin/events">

        <% if(edit){ %>
            <input type="hidden" name="eventId" value="<%= event.getEventId() %>">
        <% } %>

        <label>Title</label>
        <input type="text" name="title" required
               value="<%= edit ? event.getTitle() : "" %>">

        <label>Category</label>
        <select name="category" required>

            <%
                String[] cats = {
                    "Music",
                    "Technology",
                    "Sports",
                    "Cultural",
                    "Comedy",
                    "Business",
                    "Education",
                    "Food"
                };

                String current = edit ? event.getCategory() : "Music";

                for(String c : cats){
            %>

            <option value="<%= c %>"
                <%= c.equals(current) ? "selected" : "" %>>
                <%= c %>
            </option>

            <% } %>

        </select>

        <label>Description</label>
        <textarea name="description"><%= edit && event.getDescription()!=null ? event.getDescription() : "" %></textarea>

        <label>Venue</label>
        <input type="text" name="venue" required
               value="<%= edit ? event.getVenue() : "" %>">

        <label>City</label>
        <input type="text" name="city" required
               value="<%= edit ? event.getCity() : "" %>">

        <label>Date</label>
        <input type="date" name="eventDate" required
               value="<%= edit ? event.getEventDate() : "" %>">

        <label>Time</label>
        <input type="time" name="eventTime" required
               value="<%= edit && event.getEventTime()!=null ? event.getEventTime().toString().substring(0,5) : "19:00" %>">

        <label>Price (₹)</label>
        <input type="number" step="0.01" name="price" required
               value="<%= edit ? event.getPrice() : "" %>">

        <label>Total Seats</label>
        <input type="number" name="totalSeats" min="1" required
               value="<%= edit ? event.getTotalSeats() : "100" %>">

        <% if(edit){ %>

            <label>Available Seats</label>
            <input type="number" name="availableSeats" min="0" required
                   value="<%= event.getAvailableSeats() %>">

        <% } %>

        <br><br>

        <button class="btn" type="submit">
            <%= edit ? "Save Changes" : "Create Event" %>
        </button>

        <a class="btn ghost"
           href="${pageContext.request.contextPath}/admin/events">
            Back
        </a>

    </form>

</div>

<jsp:include page="/includes/footer.jsp"/>

</body>
</html>
