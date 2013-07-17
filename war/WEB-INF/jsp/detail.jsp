<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Task Detail</title>
</head>
<body>

	<h3>message</h3>
	<c:if test="${flashMsg != null}">
		<h2>${flashMsg}</h2>
	</c:if>

	<table>
			<tr>
				<td>${task.timeUnitUsed} / ${task.timeUnitPlaned}</td>
				<td>${task.content }</td>
				<td>${task.creationDate }</td>
				<td><button class="deleteTask" value="${task.key}">Delete</button></td>
			</tr>
	</table>

    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>

	<script>
		$(document).ready(function() {
			$(".deleteTask").click(function(event) {
				var key = $(this).val();
				var tr = $(this).parent().parent();
				alert(key);
				$.ajax({
					type : "DELETE",
					url : "/tasks/" + key + "/delete",
				}).done(function(msg) {
					if (msg == "OK") {
						tr.remove();
					} else {
						alert("DELETE failed");
					}
				});

			});
		});
	</script>

</body>
</html>