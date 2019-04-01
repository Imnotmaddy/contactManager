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
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <title>Contacts</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <form action="/contactManager?command=deleteContacts" method="post" id="contactForm" role="form">
        <div>
            <button type="submit" class="btn btn-primary  btn-md" onclick="
                    document.getElementById('contactForm').submit();">Delete
            </button>
        </div>
        <table class="table table-striped">
            <thead>
            <tr>
                <td></td>
                <td>Name</td>
                <td>Birth date</td>
                <td>Address</td>
                <td>Job</td>
                <td></td>
            </tr>
            </thead>
            <c:forEach var="contact" items='${contacts}'>
                <tr>
                    <td><input type="checkbox" name="id" value="${contact.id}"></td>
                    <td>${contact.surname} ${contact.name} ${contact.familyName}</td>
                    <td>${contact.dateOfBirth}</td>
                    <td>${contact.jobAddress}</td>
                    <td>${contact.currentJob}</td>
                </tr>
            </c:forEach>
        </table>
    </form>
</div>
</body>
</html>
