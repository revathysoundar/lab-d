<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%--
	On this page we show the current weather.
	
	Model:
	- Sun sun
 --%>
<html>
<head>
<%@ include file="parts/head.jsp"%>
</head>
<body>
	<div class="nav">
		<a href="<c:url value="/"/>">Home</a>
	</div>
	<h1>Sun API</h1>

	<p>
		<label>Sunrise Time: </label>
		<c:out value="${ sun.sunrise }" />
		<br />
		<label>Sunset Time:</label> 
		<c:out value="${ sun.sunset }" />
	</p>

</body>
</html>