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
    <link rel="stylesheet" href="../css/header.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title></title>
</head>
<body>
<div class="navbar">
    <a class="active" onclick="headerModule.toContactList()"><i class="fa fa-fw fa-bars"></i> Contact List</a>
    <a onclick="headerModule.toPhoneNumbersList()"><i class="fa fa-fw fa-address-book"></i> Phone Numbers</a>
    <a onclick="headerModule.toCreateContact()"><i class="fa fa-fw fa-user"></i> Add Contact</a>
    <a href="#"><i class="fa fa-fw fa-search"></i> Search</a>
</div>
<%--
<nav class="navbar-fixed-top navbar-inverse ">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/contactManager">Contact Manager</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a onclick="headerModule.toContactList()">Home</a></li>
            <li><a onclick="headerModule.toCreateContact()"> Add Contact </a></li>
            <li><a onclick="headerModule.toPhoneNumbersList()"> Phone Numbers </a></li>
            <li><a>Search</a></li>
        </ul>
    </div>
</nav> --%>

<script type="text/javascript" src="../js/header.js?date=01.04.2019"></script>
</body>
</html>
