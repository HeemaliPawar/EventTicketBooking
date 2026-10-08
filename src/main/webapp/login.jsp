<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login | EventHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="form-wrap">
    <h2>Welcome back</h2>
    <p class="muted">Login to book tickets or manage events.</p>
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert error"><%= request.getAttribute("error") %></div>
    <% } %>
    <% if (request.getAttribute("success") != null) { %>
        <div class="alert ok"><%= request.getAttribute("success") %></div>
    <% } %>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <label>Email</label>
        <input type="email" name="email" required>
        <label>Password</label>
        <input type="password" name="password" required>
        <br><br>
        <button class="btn" type="submit">Login</button>
    </form>
    <p class="muted" style="margin-top:16px">User: demo@eventhub.com / demo123<br>Admin: admin@eventhub.com / admin123</p>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
