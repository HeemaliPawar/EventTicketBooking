<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register | EventHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="form-wrap">
    <h2>Create account</h2>
    <p class="muted">Book concerts, sports and more.</p>
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert error"><%= request.getAttribute("error") %></div>
    <% } %>
    <form method="post" action="${pageContext.request.contextPath}/register">
        <label>Full name</label>
        <input name="fullName" value="${fullName}" required>
        <label>Email</label>
        <input type="email" name="email" value="${email}" required>
        <label>Phone</label>
        <input name="phone" value="${phone}" required>
        <label>Password</label>
        <input type="password" name="password" minlength="6" required>
        <label>Confirm password</label>
        <input type="password" name="confirm" minlength="6" required>
        <br><br>
        <button class="btn" type="submit">Register</button>
    </form>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
