<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Registration Confirmation Page</title>
</head>
<body>
	message : ${success}
	<br/>
	<h1>Success</h1>
	<p>Following file is uploaded successfully.</p>
	<ol>
		
			<li>${files}</li>
	</ol>
	<p>Following parameters extracted</p>
	<ul>
	
	<li>${name}</li>
		<li>${email}</li>
			<li>${position}</li>
	</ul>
	<br/>
	Go back to <a href="<c:url value='/list' />">List of All Employees</a>
	
</body>

</html>