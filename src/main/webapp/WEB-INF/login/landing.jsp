<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- c:out ; c:forEach etc. -->
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Formatting (dates) -->
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!-- form:form -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- for rendering errors on PUT routes -->
<%@ page isErrorPage="true"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
	crossorigin="anonymous">
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="container" style="
	background-color: lightgray;
	 margin-top: 100px;
	 padding: 50px;">
		<div class="row">
			<h1>Hello, ${name}</h1>
			<div style="margin-left: 680px;">
				<a href="/logout">Logout</a>
			</div>
			<div class="row">
				<div style="margin-left: 20px;">
					<p>Books from everyones shelves!</p>
				</div>
				<div style="margin-left: 890px;">
					<a href="/books/new">+ Add a book to my shelf!</a>
				</div>
			</div>
		</div>
		<hr>
		<div class="container">
			<table>
				<thead style="border: 1px solid gray;">
					<tr style="border: 1px solid gray;">
						<th>ID</th>
						<th>Title</th>
						<th>Author Name</th>
						<th>Posted By</th>
					<tr>
				</thead>
					<tbody style="border: 1px solid gray;">
						<c:forEach items="${books}" var="b">
							<tr style="border: 1px solid gray;">
								<td>${b.id} |</td>
								<td><a href="/books/${b.id}">${b.getTitle()}</a></td>
								<td>|${b.author}</td>
								<td>|${b.getUser().getUserName()}</td>
							</tr>
						</c:forEach>
					</tbody>
			</table>
		</div>
	</div>
</body>
</html>