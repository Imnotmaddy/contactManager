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
    <link rel="stylesheet" href="../css/contactList.css">
    <link rel="stylesheet" href="../css/myTable.css">
    <title>Contacts</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <form action="/contactManager?command=deleteContacts" method="post" id="contactForm" role="form">
        <table class="table table-striped" id="allContactsTable">
            <thead>
            <tr>
                <td></td>
                <td>Name</td>
                <td>Birth date</td>
                <td>Address</td>
                <td>Job</td>
            </tr>
            </thead>
            <c:forEach var="contact" items='${contacts}'>
                <tr>
                    <td><input type="checkbox" name="id" value="${contact.id}"></td>
                    <td>
                        <a href="/contactManager?command=editContact&contactId=${contact.id}"
                        >${contact.surname} ${contact.name} ${contact.familyName}</a>
                    </td>
                    <td>${contact.dateOfBirth}</td>
                    <td>${contact.jobAddress}</td>
                    <td>${contact.currentJob}</td>
                </tr>
            </c:forEach>
        </table>

        <c:if test="${not empty requestScope.numberOfPages}">
            <ul class="pagination pagination-lg" id="paginationElement" style="margin-top: 0px; margin-bottom: 10px">
                <c:forEach begin="1" end="${requestScope.numberOfPages}" varStatus="status">
                    <li>
                        <a href="/contactManager?command=changePage&requestedPage=${status.index}"
                        >${status.index}</a>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
        <div>
            <button type="submit" class="btn btn-primary  btn-md" onclick="
                    document.getElementById('contactForm').submit();">Delete
            </button>
        </div>
    </form>
</div>
<script type="text/javascript" src="../js/contactList.js"></script>
</body>
</html>
