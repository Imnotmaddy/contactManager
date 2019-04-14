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
    <link rel="stylesheet" href="../css/myTable.css">
    <link rel="stylesheet" href="../css/addContact.css">


    <title>Contact Manager</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<form action="/contactManager?command=sendEmails" method="post" role="form" id="emailForm">
    <div class="myRow">
        <div class="column">
            <h2>Send Email</h2>
            <div class="form-group">

                <label for="templateInput" class="control-label col-xs-6"><b>Select Email Template:</b></label>
                <select id="templateInput" class="form-control">
                    <option value="none">None</option>
                    <option value="businessTemplate">Business</option>
                    <option value="casualTemplate">Casual</option>
                </select>

                <label for="subjectInput" class="control-label col-xs-6"><b>Subject:</b></label>
                <input id="subjectInput" placeholder="Subject is subjective!" class="form-control" name="msgSubject" required>

                <label for="msgInput" class="control-label col-xs-6"><b>Message:</b></label>
                <textarea id="msgInput" placeholder="Hi, this is the message!" class="form-control"
                          name="msgText" required></textarea>

                <br/>
                <button type="button" class="myButton" onclick="sendEmail();">Send email</button>
            </div>
        </div>
        <div class="column">
            <table class="table table-striped" id="recipientsTable">
                <thead>
                <tr>
                    <td></td>
                    <td>Recipient Email</td>
                </tr>
                </thead>
                <c:forEach var="recipient" items='${recipients}'>
                    <input type="hidden" value="${recipient}" name="originSetOfRecipients">
                    <tr>
                        <td><input type="checkbox"></td>
                        <td>${recipient}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</form>
<script type="text/javascript" src="../js/emails.js"></script>
</body>
</html>
