<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Mail info</title>
<script>
	function hideshow(){
		state=document.getElementById("hideshow").value;
		if(state==="show"){
			document.getElementById("hideshow").value="hide";
			document.getElementById("database").style.display="block";
						}
		else{
			document.getElementById("hideshow").value="show";
			document.getElementById("database").style.display="none";
		}
	}
	function hideshown(){
	state=document.getElementById("hideshow1").value;
	if(state==="show"){
		document.getElementById("hideshow1").value="hide";
		document.getElementById("form1").style.display="block";
					}
	else{
		document.getElementById("hideshow1").value="show";
		document.getElementById("form1").style.display="none";
		}
	}
	</script>
	<style>
		tr:first-child{
			font-weight: bold;
			background-color: #C6C9C4;
		}
		body {background-color:lightgrey;}
h2   {color:blue;
}
p    {color:green;
}
td {color:red;
background-color: white;
}
form {color: blue;
border-style: dashed;
border-color: black;
border-width: 1px;
}
	</style>
	

</head>
<body>
	<h2>List of Employees</h2>	
	
	<table id="database" >
		<tr>
			<td>NAME</td><td>Position</td><td>Email</td><td></td>
		</tr>
		<c:forEach items="${employees}" var="employee">
			<tr>
			<td>${employee.name}</td>
			<td>${employee.position}</td>
			<td>${employee.email}</td>
			<td><a href="<c:url value='/delete-${employee.email}-employee' />">delete</a></td>
			</tr>
		</c:forEach>
	</table>
	<input onclick="hideshow()" type="button" value="hide" id="hideshow"></input>
	<br/>
	<form:form id="form1" method="post" action="Upload" 
		modelAttribute="uploadForm" enctype="multipart/form-data">
	<p>upload .pdf file with predefined pattern</p>
	<table id="fileTable">
		<tr>
			<td><input name="files" type="file" /></td>
		</tr>
	</table>
	<input type="submit" value="Upload" />
</form:form><input onclick="hideshown()" type="button" value="hide" id="hideshow1"></input>

<form:form action="mail" method="POST">
HOST(pop3.live.com pop.gmail.com): <input type="text" name="Host">

       PORT(995): <input type="text" name="Port" />
<br />
USERNAME(milospolarcape@outlook.com): <input type="text" name="Username" />

    PASSWORD: <input type="password" name="Password" />
<br />
MAIL from: <input type="text" name="From" />
    MAIL to: <input type="text" name="To" />
<input type="submit" value="mail" />
</form:form>
<h3>${message}</h3>
	<a href="<c:url value='/new' />">Add New Employee</a>
</body>
</html>