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
    <link rel="stylesheet" type="text/css" href="../css/collapse.css?version=12.04.19"/>
    <link rel="stylesheet" type="text/css" href="../css/addContact.css?version16.04.19"/>
    <link rel="stylesheet" type="text/css" href="../css/popup.css?version=12.04.19"/>
    <link rel="stylesheet" type="text/css" href="../css/myTable.css?version=12.04.19"/>
    <link rel="stylesheet" type="text/css" href="../css/modalWindow.css?version=16.04.19"/>
    <link rel="stylesheet" type="text/css" href="../css/centralModal.css?version=20"/>
    <title>New Contact</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<form action="/contactManager?command=${command}&id=${contact.id}" method="post" id="contactForm"
      enctype="multipart/form-data">
    <div class="myRow">
        <div class="column">
            <c:if test="${command == 'addContact'}">
                <h2>New Contact</h2>
            </c:if>
            <c:if test="${command == 'updateContact'}">
                <h2>Edit Contact</h2>
            </c:if>
            <div class="form-group">
                <div id="photoBlock">
                    <input type="hidden" value="${photo}" name="oldPhoto">
                    <c:if test="${not empty photo}">
                        <img src="data:image/jpeg;base64,${photo}" style="cursor: pointer" onclick="showPhotoModal()"/>
                    </c:if>
                    <br/>
                    <br/>
                </div>

                <button type="button" class="myButton" style="background-color: #91a6c1"
                        onclick="showAttachmentModal();">
                    Attachments
                </button>
                <br/>
                <br/>

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
                    <select id="sex" name="sex" class="form-control">
                        <option value="Male" ${contact.sex == 'Male' ? 'selected' : ''}>Male</option>
                        <option value="Female" ${contact.sex == 'Female' ? 'selected' : ''}>Female</option>
                    </select>

                    <label for="citizenship" class="control-label col-xs-4">Citizenship:</label>
                    <input type="text" id="citizenship" name="citizenship" class="form-control"
                           value="${contact.citizenship}"/>

                    <label for="relationship" class="control-label col-xs-4">Relationship:</label>
                    <input type="text" id="relationship" name="relationship" class="form-control"
                           value="${contact.relationship}"/>

                    <label for="webSite" class="control-label col-xs-8">Your webSite:</label>
                    <input type="text" id="webSite" name="webSite" class="form-control"
                           value="${contact.webSite}"/>

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
                <button type="button" class="myButton" onclick="submitAll();">Submit
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
                    <td>Edit</td>
                </tr>
                </thead>
                <c:forEach var="phoneNumber" items='${phoneNumbers}'>
                    <input type="hidden" name="countryCode" value="${phoneNumber.countryCode}">
                    <input type="hidden" name="operatorCode" value="${phoneNumber.operatorCode}">
                    <input type="hidden" name="commentary" value="${phoneNumber.commentary}">
                    <input type="hidden" name="phoneNumber" value="${phoneNumber.phoneNumber}">
                    <input type="hidden" name="phoneType" value="${phoneNumber.phoneType}">
                    <input type="hidden" name="phoneId" value="${phoneNumber.id}">
                    <tr>
                        <td><input type="checkbox" name="phoneIdForDelete" id="phoneId" value="${phoneNumber.id}"></td>
                        <td>${phoneNumber.countryCode}${phoneNumber.operatorCode}${phoneNumber.phoneNumber}</td>
                        <td>${phoneNumber.phoneType}</td>
                        <td>${phoneNumber.commentary}</td>
                        <td>
                            <button type="button" class="btn btn-primary  btn-md" name="phoneEditButton" onclick="
                     editNumber(this);">Edit
                            </button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <div>
                <button type="button" class="btn btn-primary  btn-md" onclick="
                     deleteNumbers();">Delete
                </button>
                <button type="button" id="undoButton" class="btn btn-primary  btn-md" onclick="
                     undoDelete();" style="visibility: hidden">Undo
                </button>
            </div>
        </div>
    </div>

    <div id="phoneModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <span class="close" id="closePhoneModal">&times;</span>
                <h2>Photo</h2>
            </div>
            <div class="modal-body">
                <input type="file" id="photo" name="photo" accept="image/*">
                <button type="button" class="btn" onclick="document.getElementById('phoneModal').style.display='none'">
                    Submit
                </button>
                <button type="button" class="btn cancel"
                        onclick="document.getElementById('photo').value=''; document.getElementById('phoneModal').style.display='none'">
                    Cancel
                </button>
            </div>
        </div>
    </div>

    <!-- The Modal -->
    <div id="attachmentModal" class="modalCenter">

        <!-- Modal content -->
        <div class="modalCenter-content">
            <div class="modalCenter-header">
                <h2>Attachments</h2>
            </div>
            <div id="hiddenAttachments" hidden>

            </div>
            <div class="modalCenter-body myRow">
                <br/>
                <div class="column">
                    <div id="attachmentField">
                        <input type="file" id="submittedFile"
                               onchange="document.getElementById('fileNameInput').value = this.files[0].name;">
                    </div>
                    <label for="fileNameInput"><b>File Name:</b></label>
                    <input type="text" name="fileNameInput" id="fileNameInput">

                    <label for="fileCommentaryInput"><b>Commentary</b></label>
                    <textarea id="fileCommentaryInput" name="fileCommentaryInput"
                              placeholder="Your commentary..."></textarea>
                </div>

                <div class="column">
                    <table class="table table-striped" id="attachmentTable">
                        <thead>
                        <tr>
                            <td></td>
                            <td>File Name</td>
                            <td>Date of Creation</td>
                            <td>Commentary</td>
                            <td>Edit</td>
                        </tr>
                        </thead>
                        <c:forEach var="attachment" items='${attachments}'>
                            <tr>
                                <td>
                                    <div>
                                            <%--FileName must be firstChild of Div--%>
                                        <input type="hidden" name="fileName" value="${attachment.fileName}">
                                        <input type="hidden" name="fileCommentary" value="${attachment.commentary}">
                                        <input type="hidden" name="dateOfCreation" value="${attachment.dateOfCreation}">
                                        <input type="file" name="attachment" style="display: none">
                                        <input type="hidden" name="attachmentId" value="${attachment.id}">
                                        <input type="hidden" name="fileExtension">
                                        <input type="checkbox" name="attachmentIdForDelete" value="${attachment.id}">
                                    </div>
                                </td>
                                <td>
                                    <a href="/contactManager?command=download&downloadAttachmentId=${attachment.id}"
                                    >${attachment.fileName}</a>
                                </td>
                                <td>${attachment.dateOfCreation}</td>
                                <td>${attachment.commentary}</td>
                                <td>
                                    <button type="button" class="btn btn-primary  btn-md"
                                            name="attachmentEditButton" onclick="editExistingAttachment(this)">Edit
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                    <button type="button" class="btn btn-primary  btn-md" id="attachmentDelete" onclick="
                     deleteAttachments();">Delete
                    </button>
                    <button type="button" id="attachmentUndo" class="btn btn-primary  btn-md" onclick="
                     undoAttachmentDelete();" style="visibility: hidden">Undo
                    </button>
                </div>
            </div>
            <br/>
            <br/>
            <button type="button" class="btnModal" id="attachmentSubmitButton"
                    onclick="addAttachment();">
                Submit Attachment
            </button>
            <button type="button" id="attachmentCancelButton" class="btnModal cancel"
                    onclick="document.getElementById('attachmentModal').style.display='none'">
                Close
            </button>
        </div>
    </div>
</form>

<button class="open-button" onclick="openForm()">Add Phone Number</button>
<div class="form-popup" id="popupPhoneNumber">
    <div class="form-container">
        <label for="phoneNumberInput"><b>Phone Number</b></label>
        <input type="number" id="phoneNumberInput" min="0" required>

        <label for="countryCodeInput"><b>Country Code</b></label>
        <input type="number" id="countryCodeInput" min="0" required>

        <label for="operatorCodeInput"><b>Operator Code</b></label>
        <input type="number" id="operatorCodeInput" min="0" required>

        <label for="phoneTypeInput"><b>Phone Type</b></label>
        <select id="phoneTypeInput" required>
            <option value="cellular" selected="selected">Cellular</option>
            <option value="stationary">Stationary</option>
        </select>

        <label for="commentaryInput"><b>Commentary</b></label>
        <textarea id="commentaryInput" placeholder="Your commentary..."></textarea>

        <button type="button" class="btn" onclick="addNumber();" id="phoneSubmitButton">Submit</button>

        <button type="button" class="btn cancel" onclick="closeForm()" id="phoneCancelButton">Close</button>
    </div>
</div>


<script type="text/javascript" src="../js/collapse.js?v=3"></script>
<script type="text/javascript" src="../js/addContact.js?versionNow123"></script>
<script type="text/javascript" src="../js/popup.js?v=3"></script>
</body>
</html>
