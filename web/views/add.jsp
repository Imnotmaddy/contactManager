<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 07.03.2019
  Time: 21:58
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add users</title>
</head>
<body>
<table>
    <c:forEach items="${data}" var="name">
        <tr>
            <td>${name}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
