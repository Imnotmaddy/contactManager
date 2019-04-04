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
    <link rel="stylesheet" type="text/css" href="../css/addContact.css?version=02.04.19"/>
    <link rel="stylesheet" type="text/css" href="../css/popup.css?version=03.04.19"/>
    <link rel="stylesheet" type="text/css" href="../css/myTable.css?version=02.04.19"/>
    <title>New Contact</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<form action="/contactManager?command=${command}&id=${contact.id}" method="post" role="form"
      data-toggle="validator" id="contactForm">
    <div class="myRow">
        <div class="column">
            <h2>New Contact</h2>
            <div class="form-group">
                <label for="name" class="control-label col-xs-8">Name:</label>
                <input type="text" id="name" name="name" class="form-control" value="${contact.name}" required/>

                <label for="surname" class="control-label col-xs-8">Last name:</label>
                <input type="text" id="surname" name="surname" class="form-control" value="${contact.surname}"
                       required/>

                <label for="email" class="control-label col-xs-8">E-mail:</label>
                <input type="text" id="email" name="email" class="form-control"
                       placeholder="smith@aol.com" value="${contact.email}" required/>

                <br/>
                <button class="collapsible" type="button">Expand Additional Information</button>
                <div class="content">
                    <label for="familyName" class="control-label col-xs-8">Family name:</label>
                    <input type="text" id="familyName" name="familyName" class="form-control"
                           value="${contact.familyName}"/>

                    <label for="date" class="control-label col-xs-4">Birth date:</label>
                    <input type="date" id="date" name="date" class="form-control"
                           min="1940-01-02" value="${contact.dateOfBirth}"/>

                    <label for="sex" class="control-label col-xs-4">Sex:</label>
                    <input type="text" id="sex" name="sex" class="form-control"
                           value="${contact.sex}"/>

                    <label for="citizenship" class="control-label col-xs-4">Citizenship:</label>
                    <input type="text" id="citizenship" name="citizenship" class="form-control"
                           value="${contact.citizenship}"/>

                    <label for="relationship" class="control-label col-xs-4">Relationship:</label>
                    <input type="text" id="relationship" name="relationship" class="form-control"
                           value="${contact.relationship}"/>

                    <label for="webSite" class="control-label col-xs-8">Your webSite:</label>
                    <input type="text" id="webSite" name="webSite" class="form-control"
                           value="${contact.familyName}"/>

                    <label for="currentJob" class="control-label col-xs-8">Your current job:</label>
                    <input type="text" id="currentJob" name="currentJob" class="form-control"
                           value="${contact.currentJob}"/>

                    <label for="jobAddress" class="control-label col-xs-8">Your job's address:</label>
                    <input type="text" id="jobAddress" name="jobAddress" class="form-control"
                           value="${contact.jobAddress}"/>

                    <label for="residenceCountry" class="control-label col-xs-8">Your residence country:</label>
                    <input type="text" id="residenceCountry" name="residenceCountry" class="form-control"
                           value="${contact.residenceCountry}"/>

                    <label for="residenceCity" class="control-label col-xs-8">Your residence city:</label>
                    <input type="text" id="residenceCity" class="form-control" name="residenceCity"
                           value="${contact.residenceCity}"/>

                    <label for="residenceStreet" class="control-label col-xs-8">Your residence street:</label>
                    <input type="text" id="residenceStreet" class="form-control" name="residenceStreet"
                           value="${contact.residenceStreet}"/>

                    <label for="residenceHouseNumber" class="control-label col-xs-8">Your residence house
                        number:</label>
                    <input type="text" id="residenceHouseNumber" class="form-control" name="residenceHouseNumber"
                           value="${contact.residenceHouseNumber}"/>

                    <label for="residenceApartmentNumber" class="control-label col-xs-8">Your apartment's
                        number:</label>
                    <input type="text" id="residenceApartmentNumber" name="residenceApartmentNumber"
                           class="form-control"
                           value="${contact.residenceApartmentNumber}"/>

                    <label for="index" class="control-label col-xs-8">Your post index:</label>
                    <input type="text" minlength="7" id="index" class="form-control" name="index"
                           placeholder="0000000"
                           value="${contact.index}"/>
                    <br/>
                </div>
                <br/>
                <button type="submit" class="myButton">Submit
                </button>
            </div>
        </div>
        <div class="column" id="phoneForm">
            <table class="table table-striped" id="phoneTable">
                <thead>
                <tr>
                    <td></td>
                    <td>Phone Number</td>
                    <td>Phone Type</td>
                    <td>Commentary</td>
                </tr>
                </thead>
                <c:forEach var="phoneNumber" items='${phoneNumbers}'>
                    <input type="hidden" name="countryCode" value="$${phoneNumber.countryCode}">
                    <input type="hidden" name="operatorCode" value="${phoneNumber.operatorCode}">
                    <input type="hidden" name="commentary" value="${phoneNumber.commentary}">
                    <input type="hidden" name="phoneNumber" value="${phoneNumber.phoneNumber}">
                    <input type="hidden" name="phoneType" value="${phoneNumber.phoneType}">
                    <tr>
                        <td><input type="checkbox" name="phoneId" id="phoneId" value="${phoneNumber.id}"></td>
                        <td>${phoneNumber.countryCode}${phoneNumber.countryCode}${phoneNumber.phoneNumber}</td>
                        <td>${phoneNumber.phoneType}</td>
                        <td>${phoneNumber.commentary}</td>
                    </tr>
                </c:forEach>
            </table>
            <div>
                <button type="button" class="btn btn-primary  btn-md" onclick="
                     deleteNumbers();">Delete
                </button>
            </div>
        </div>
    </div>
</form>
<button class="open-button" onclick="openForm()">Add Phone Number</button>
<div class="form-popup" id="popupPhoneNumber">
    <div class="form-container">
        <label for="phoneNumberInput"><b>Phone Number</b></label>
        <input type="number" id="phoneNumberInput" required>

        <label for="countryCodeInput"><b>Country Code</b></label>
        <input type="number" id="countryCodeInput" required>

        <label for="operatorCodeInput"><b>Operator Code</b></label>
        <input type="number" id="operatorCodeInput" required>

        <label for="phoneTypeInput"><b>Phone Type</b></label>
        <select id="phoneTypeInput" required>
            <option value="cellular">Cellular</option>
            <option value="stationary">Stationary</option>
        </select>

        <label for="commentaryInput"><b>Commentary</b></label>
        <textarea id="commentaryInput" placeholder="Your commentary..."></textarea>

        <button type="button" class="btn" onclick="addNumber();">Submit</button>

        <button type="button" class="btn cancel" onclick="closeForm()">Close</button>
    </div>
</div>
<script type="text/javascript" src="../js/collapse.js?version=01.04.19"></script>
<script type="text/javascript" src="../js/addContact.js?version=04.04.19"></script>
<script type="text/javascript" src="../js/popup.js?version=02.04.19"></script>
</body>
</html>
