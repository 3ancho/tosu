<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tasks</title>
</head>
<body>

    <c:if test="${username != null}">
        <b><a href="${logoutURL}">Logout ${username} </a></b>
    </c:if>

    <c:if test="${flashMsg != null}">
        <h2>${flashMsg}</h2>
    </c:if>

    <table>
        <c:forEach var="ta" items="${tasks}">
            <tr>
                <td><button class="startTask" value="${ta.key}">Start</button></td>
                <td>0 / ${ta.timeUnitPlaned}</td>
                <td>${ta.content }</td>
                <td><button class="deleteTask" value="${ta.key}">Delete</button></td>
            </tr>
        </c:forEach>
    </table>

    <c:url value="/tasks/create" var="submitEndpoint" />
    <form:form id="input_form" method="post" commandName="taskform"
        action="${submitEndpoint}">
        <table>
            <tr>
                <td><form:label path="timeUnitPlaned">Planning Time Unit</form:label></td>
                <td><form:input id="timeUnitPlaned"
                        path="timeUnitPlaned" type="int" /></td>
            </tr>
            <tr>
                <td><form:label path="content">Task Content</form:label></td>
                <td><form:input id="content" path="content" /></td>
            </tr>
            <tr>
                <td><input type="submit" value="Add task" /></td>
            </tr>
        </table>
    </form:form>

    <script
        src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>

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
            
            $(".startTask").click(function(event) {
                var key = $(this).val();
                var tr = $(this).parent().parent();
                alert(key);
                $.ajax({
                    type : "GET",
                    url : "/tasks/" + key + "/start",
                }).done(function(msg) {
                    if (msg == "ERROR") {
                        alert("start failed");
                    } else {
                        startTask();
                    }
                });

            });
            
            $(".pauseTask").click(function(event) {
                var key = $(this).val();
                var tr = $(this).parent().parent();
                alert(key);
                $.ajax({
                    type : "GET",
                    url : "/tasks/" + key + "/pause",
                }).done(function(msg) {
                    if (msg == "ERROR") {
                        alert("pause failed");
                    } else {
                        pauseTask();
                    }
                });

            });
            
            function startTask() {
                alert("task started");  
            }
        });
    </script>

</body>
</html>