<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 24.03.2019
  Time: 17:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <title>New Contact</title>
</head>
<body>
<div class="container">
    <form action="/add" method="post" role="form" data-toggle="validator">
        <input type="hidden" id="action" name="action" value="${action}">
        <input type="hidden" id="contactId" name="contactId" value="${contact.id}">
        <h2>New Contact</h2>
        <div class="form-group col-xs-4">
            <label for="name" class="control-label col-xs-4">Name:</label>
            <input type="text" name="name" id="name" class="form-control" value="${contact.name}" required="true"/>

            <label for="surname" class="control-label col-xs-4">Last name:</label>
            <input type="text" name="surname" id="surname" class="form-control" value="${contact.surname}"
                   required="true"/>

            <label for="familyName" class="control-label col-xs-4">Family name:</label>
            <input type="text" name="familyName" id="familyName" class="form-control" value="${contact.familyName}"
                   required="false"/>

            <label for="date" class="control-label col-xs-4">Birth date</label>
            <input type="date"  name="date" id="date" class="form-control"
                   value="${contact.dateOfBirth}" required="false" min="1940-01-02"/>

            <label for="email" class="control-label col-xs-4">E-mail:</label>
            <input type="text" name="email" id="email" class="form-control" value="${contact.email}"
                   placeholder="smith@aol.com" required="true"/>

            <br></br>
            <button type="submit" class="btn btn-primary  btn-md" onclick="document.getElementById('action').value = 'add';
                    document.getElementById('contactForm').submit();">Accept
            </button>
        </div>
    </form>
</div>
</body>
</html>
