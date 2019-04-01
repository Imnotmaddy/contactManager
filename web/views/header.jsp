<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 24.03.2019
  Time: 16:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <title>Title</title>
</head>
<body>
<nav class="navbar-fixed-top navbar-inverse ">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/contactManager">Contact Manager</a>
        </div>
        <ul class="nav navbar-nav">
            <li style="cursor: pointer"><a onclick="headerModule.toContactList()">Home</a></li>
            <li style="cursor: pointer"> <a onclick="headerModule.toCreateContact()"> Add Contact </a></li>
            <li style="cursor: pointer"><a>Search</a></li>
        </ul>
    </div>
</nav>
<script type="text/javascript" src = "../js/header.js"></script>
</body>
</html>
