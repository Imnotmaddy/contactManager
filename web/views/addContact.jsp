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
    <link rel="stylesheet" type="text/css" href="../css/collapse.css?version=02.04.19"/>
    <link rel="stylesheet" type="text/css" href="../css/addContact.css?version=02.04.191"/>
    <link rel="stylesheet" type="text/css" href="../css/popup.css?version=02.04.19"/>
    <link rel="stylesheet" type="text/css" href="../css/myTable.css?version=02.04.19"/>
    <title>New Contact</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="myRow">
    <div class="column">
        <form action="/contactManager?command=${command}" method="post" role="form" data-toggle="validator">

            <h2>New Contact</h2>
            <div class="form-group">
                <label for="name" class="control-label col-xs-8">Name:</label>
                <input type="text" name="name" id="name" class="form-control" value="${contact.name}" required/>

                <label for="surname" class="control-label col-xs-8">Last name:</label>
                <input type="text" name="surname" id="surname" class="form-control" value="${contact.surname}"
                       required/>

                <label for="email" class="control-label col-xs-8">E-mail:</label>
                <input type="text" name="email" id="email" class="form-control"
                       placeholder="smith@aol.com" value="${contact.email}" required/>

                <br/>
                <button class="collapsible" type="button">Expand Additional Information</button>
                <div class="content">
                    <label for="familyName" class="control-label col-xs-8">Family name:</label>
                    <input type="text" name="familyName" id="familyName" class="form-control"
                           value="${contact.familyName}"/>

                    <label for="date" class="control-label col-xs-4">Birth date:</label>
                    <input type="date" name="date" id="date" class="form-control"
                           min="1940-01-02" value="${contact.dateOfBirth}"/>

                    <label for="sex" class="control-label col-xs-4">Sex:</label>
                    <input type="text" name="sex" id="sex" class="form-control"
                           value="${contact.sex}"/>

                    <label for="citizenship" class="control-label col-xs-4">Citizenship:</label>
                    <input type="text" name="citizenship" id="citizenship" class="form-control"
                           value="${contact.citizenship}"/>

                    <label for="relationship" class="control-label col-xs-4">Relationship:</label>
                    <input type="text" name="relationship" id="relationship" class="form-control"
                           value="${contact.relationship}"/>

                    <label for="webSite" class="control-label col-xs-8">Your webSite:</label>
                    <input type="text" name="webSite" id="webSite" class="form-control"
                           value="${contact.familyName}"/>

                    <label for="currentJob" class="control-label col-xs-8">Your current job:</label>
                    <input type="text" name="currentJob" id="currentJob" class="form-control"
                           value="${contact.currentJob}"/>

                    <label for="jobAddress" class="control-label col-xs-8">Your job's address:</label>
                    <input type="text" name="jobAddress" id="jobAddress" class="form-control"
                           value="${contact.jobAddress}"/>

                    <label for="residenceCountry" class="control-label col-xs-8">Your residence country:</label>
                    <input type="text" name="residenceCountry" id="residenceCountry" class="form-control"
                           value="${contact.residenceCountry}"/>

                    <label for="residenceCity" class="control-label col-xs-8">Your residence city:</label>
                    <input type="text" name="residenceCity" id="residenceCity" class="form-control"
                           value="${contact.residenceCity}"/>

                    <label for="residenceStreet" class="control-label col-xs-8">Your residence street:</label>
                    <input type="text" name="residenceStreet" id="residenceStreet" class="form-control"
                           value="${contact.residenceStreet}"/>

                    <label for="residenceHouseNumber" class="control-label col-xs-8">Your residence house
                        number:</label>
                    <input type="text" name="residenceHouseNumber" id="residenceHouseNumber" class="form-control"
                           value="${contact.residenceHouseNumber}"/>

                    <label for="residenceApartmentNumber" class="control-label col-xs-8">Your apartment's
                        number:</label>
                    <input type="text" name="residenceApartmentNumber" id="residenceApartmentNumber"
                           class="form-control"
                           value="${contact.residenceApartmentNumber}"/>

                    <label for="index" class="control-label col-xs-8">Your post index:</label>
                    <input type="text" minlength="7" name="index" id="index" class="form-control"
                           placeholder="0000000"
                           value="${contact.index}"/>
                    <br/>
                </div>
                <br/>
                <button type="submit" class="myButton">Submit
                </button>
            </div>
        </form>
    </div>
    <div class="column">
        <form action="/contactManager?command=deletePhoneNumbers" method="post" id="phoneNumberForm" role="form">
            <table class="table table-striped">
                <thead>
                <tr>
                    <td></td>
                    <td>Phone Number</td>
                    <td>Phone Type</td>
                    <td>Commentary</td>
                </tr>
                </thead>
                <c:forEach var="phoneNumber" items='${phoneNumbers}'>
                    <tr>
                        <td><input type="checkbox" name="phoneId" value="${phoneNumber.id}"></td>
                        <td>${phoneNumber.countryCode}${phoneNumber.operatorCode}${phoneNumber.phoneNumber}</td>
                        <td>${phoneNumber.phoneType}</td>
                        <td>${phoneNumber.commentary}</td>
                    </tr>
                </c:forEach>
            </table>
            <div>
                <button type="submit" class="btn btn-primary  btn-md" onclick="
                    document.getElementById('phoneNumberForm').submit();">Delete
                </button>
            </div>
        </form>
    </div>
</div>
<button class="open-button" onclick="openForm()">Add Phone Number</button>
<div class="form-popup" id="popupPhoneNumber">
    <form action="/contactManager?command=addPhoneNumber" method="post" role="form" id="addNumberForm"
          class="form-container">
        <input type="hidden" id="id" name="id" value="${contact.id}">
        <label for="phoneNumber"><b>Phone Number</b></label>
        <input type="text" id="phoneNumber" name="phoneNumber" required>

        <label for="countryCode"><b>Country Code</b></label>
        <input type="text" id="countryCode" name="countryCode" required>

        <label for="operatorCode"><b>Operator Code</b></label>
        <input type="text" id="operatorCode" name="operatorCode" required>

        <label for="phoneType"><b>Phone Type</b></label>
        <select id="phoneType" name="phoneType" required>
            <option value="cellular">Cellular</option>
            <option value="stationary">Stationary</option>
        </select>

        <label for="commentary"><b>Commentary</b></label>
        <textarea id="commentary" name="commentary" placeholder="Your commentary..."></textarea>

        <button type="submit" class="btn">Submit</button>

        <button type="button" class="btn cancel" onclick="closeForm()">Close</button>
    </form>
</div>
<script type="text/javascript" src="../js/collapse.js?version=01.04.19"></script>
<script type="text/javascript" src="../js/addContact.js?version=02.04.19"></script>
<script type="text/javascript" src="../js/popup.js?version=02.04.19"></script>
</body>
</html>
