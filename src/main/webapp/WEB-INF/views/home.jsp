<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%--
	On this page the user can choose to view movies or categories.
	
	Model: none
 --%>
<html>
<head>
	<%@ include file="parts/head.jsp" %>
</head>
<body>
<h1>
	Welcome.
</h1>

<a href="<c:url value="/movies"/>">Movies</a>
<a href="<c:url value="/categories"/>">Categories</a>
<a href="<c:url value="/users"/>">Users</a>
<a href="<c:url value="/login"/>">Login</a>
<a href="<c:url value="/weather"/>">Weather</a>
<a href="<c:url value="/sun"/>">Sun API</a>
</body>
</html>
