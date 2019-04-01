<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 01.04.2019
  Time: 21:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../css/popup.css"/>
    <title>Contact Manager</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<button class="open-button" onclick="openForm()">Open Form</button>
<div class="form-popup" id="popupPhoneNumber">
    <form action="/contactManager?command=addPhoneNumber" class="form-container">
        <h1>Add Phone Number</h1>

        <label for="phoneNumber"><b>Phone Number</b></label>
        <input type="text" placeholder="Enter Email" id="phoneNumber" name="phoneNumber" required>

        <button type="submit" class="btn">Submit</button>
        <button type="button" class="btn cancel" onclick="closeForm()">Close</button>
    </form>
</div>

<script type="text/javascript" src="../js/popup.js"></script>
</body>
</html>
