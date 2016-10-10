<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%--
	On this page we show the current weather.
	
	Model:
	- Weather weather
 --%>
<html>
<head>
<%@ include file="parts/head.jsp"%>
</head>
<body>
	<div class="nav">
		<a href="<c:url value="/"/>">Home</a>
	</div>
	<h1>Grand Circus Weather</h1>

	<p>
		<label>Current Temperature: </label>
		<c:out value="${ weather.temperature }" />&deg;F
		<br /> <img src="${ weather.image }" />
	</p>

</body>
</html>