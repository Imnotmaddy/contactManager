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

<div class="container">
    <h2>Contacts</h2>


    <form action="/add" method="post" id="contactForm" role="form">
        <input type="hidden" id="contactId" name="contactId">
        <input type="hidden" id="action" name="action">
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
            <c:forEach var="contact" items="${contactList}">
                <tr>
                    <td><input type="checkbox" name="contactSelect" id="${contact.id}"></td>
                    <td>${contact.surname} ${contact.name} ${contact.familyName}</td>
                    <td>${contact.dateOfBirth}</td>
                    <td>${contact.jobAddress}</td>
                    <td>${contact.currentJob}</td>
                    <td><a href="#" id="remove"
                           onclick="document.getElementById('action').value = 'delete';
                                   document.getElementById('contactId').value = '${contact.id}';
                                   document.getElementById('contactForm').submit();">
                        <span class="glyphicon glyphicon-trash"></span>
                    </a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </form>
</div>
</body>
</html>
