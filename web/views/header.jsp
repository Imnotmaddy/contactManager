<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 24.03.2019
  Time: 16:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <link rel="stylesheet" href="../css/header.css?version=070419">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title></title>
</head>
<body>
<div class="navbar">
    <a onclick="headerModule.toContactList(null)"><i class="fa fa-fw fa-bars"></i> Contact List</a>
    <a onclick="headerModule.toCreateContact()"><i class="fa fa-fw fa-user"></i> Add Contact</a>
    <a onclick="headerModule.sendEmail();"><i class="fa fa-fw fa-envelope"></i> Send Email</a>
    <a href="#"><i class="fa fa-fw fa-search"></i> Search</a>
</div>

<c:if test="${not empty error}">
    <div class="alert">
        <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
        <strong>Error!</strong> ${error}
    </div>
</c:if>
<script type="text/javascript" src="../js/header.js?date=09.04.2019"></script>
</body>
</html>
