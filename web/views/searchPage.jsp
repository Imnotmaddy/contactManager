<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 15.04.2019
  Time: 14:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search</title>
    <link rel="stylesheet" type="text/css" href="../css/addContact.css?version15.04.19"/>
    <link rel="stylesheet" href="../css/bootstrap.min.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<form action="/contactManager?command=searchContacts" method="post">
    <div class="form-group">
        <label for="name" class="control-label col-xs-8">Name:</label>
        <input type="text" id="name" name="name" class="form-control"/>

        <label for="surname" class="control-label col-xs-8">Last name:</label>
        <input type="text" id="surname" name="surname" class="form-control"
        />

        <label for="familyName" class="control-label col-xs-8">Family name:</label>
        <input type="text" id="familyName" name="familyName" class="form-control"
        />

        <label for="dateAfter" class="control-label col-xs-4">Born after:</label>
        <input type="date" id="dateAfter" name="dateAfter" class="form-control"
               min="1940-01-02"/>

        <label for="dateBefore" class="control-label col-xs-4">Born before:</label>
        <input type="date" id="dateBefore" name="dateBefore" class="form-control"
               min="1940-01-02"/>

        <label for="sex" class="control-label col-xs-4">Sex:</label>
        <select id="sex" name="sex" class="form-control">
            <option disabled selected value=" "> -- select an option --</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
        </select>

        <label for="relationship" class="control-label col-xs-4">Relationship:</label>
        <input type="text" id="relationship" name="relationship" class="form-control"
        />

        <label for="citizenship" class="control-label col-xs-4">Citizenship:</label>
        <input type="text" id="citizenship" name="citizenship" class="form-control"
        />

        <label for="residenceCountry" class="control-label col-xs-8">Your residence country:</label>
        <input type="text" id="residenceCountry" name="residenceCountry" class="form-control"
        />

        <label for="residenceCity" class="control-label col-xs-8">Your residence city:</label>
        <input type="text" id="residenceCity" class="form-control" name="residenceCity"
        />

        <label for="residenceStreet" class="control-label col-xs-8">Your residence street:</label>
        <input type="text" id="residenceStreet" class="form-control" name="residenceStreet"
        />

        <label for="residenceHouseNumber" class="control-label col-xs-8">Your residence house
            number:</label>
        <input type="text" id="residenceHouseNumber" class="form-control" name="residenceHouseNumber"
        />

        <label for="residenceApartmentNumber" class="control-label col-xs-8">Your apartment's
            number:</label>
        <input type="text" id="residenceApartmentNumber" name="residenceApartmentNumber"
               class="form-control"
        />

        <br/>
        <button type="submit" class="myButton">Search
        </button>
    </div>
</form>
</body>
</html>
