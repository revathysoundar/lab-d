<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--
	On this page we have a form to edit a single user and a button to delete it.
	
	Model:
	- User user
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ include file="parts/head.jsp" %>
<title>Insert title here</title>
</head>
<body>
<h1> Login Page </h1>
<form method="post" action="<c:url value="/users/user"/>">
<div>
	<label>Email</label>
	<input type="text" name="email" >
</div>

<div>
	<label>Password</label>
	<input type="text" name="password">
</div>
<button type="submit">Login</button>
</form>

</body>
</html>