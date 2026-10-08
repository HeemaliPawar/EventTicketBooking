<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error | EventHub</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
<div class="form-wrap">
    <h2>Something went wrong</h2>
    <p class="muted">Please go back and try again. If this continues, check MySQL and GlassFish logs.</p>
    <br>
    <a class="btn" href="${pageContext.request.contextPath}/home">Home</a>
</div>
</body>
</html>
