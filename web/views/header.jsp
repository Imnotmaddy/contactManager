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
    <a onclick="headerModule.toContactList()"><i class="fa fa-fw fa-bars"></i> Contact List</a>
    <a onclick="headerModule.toPhoneNumbersList()"><i class="fa fa-fw fa-address-book"></i> Phone Numbers</a>
    <a onclick="headerModule.toCreateContact()"><i class="fa fa-fw fa-user"></i> Add Contact</a>
    <a href="#"><i class="fa fa-fw fa-search"></i> Search</a>
</div>
<script type="text/javascript" src="../js/header.js?date=01.04.2019"></script>
</body>
</html>
