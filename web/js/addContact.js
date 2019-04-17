let phonesForDelete = [];
let attachmentsForDelete = [];
let attachmentsForEdit = [];

function isPhoneFormValid() {
    let countryCode = document.getElementById('countryCodeInput').value;
    let operatorCode = document.getElementById('operatorCodeInput').value;
    let phoneNumber = document.getElementById('phoneNumberInput').value;
    if (countryCode.length < 1 || countryCode.length > 3) {
        alert("Country Code requires 1-3 digits");
        return false;
    }
    if (phoneNumber.length < 6 || phoneNumber.length > 9) {
        alert("Phone number requires 6-9 digits");
        return false;
    }
    if (operatorCode.length < 1 || operatorCode.length > 3) {
        alert("Operator code requires 1-3 digits");
        return false;
    }

    return true;
}

function isEmailValid(mail) {
    let mailFormat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    return (mail.match(mailFormat));
}

function isFieldEmpty(text) {
    return (text === "");
}

function addNumber() {
    if (isPhoneFormValid()) {
        if (checkPhoneForUnique(document.getElementById('phoneNumberInput').value)) {
            let tbl = document.getElementById("phoneTable");
            let row = tbl.insertRow();
            row.style.backgroundColor = "#98aa92";
            let column1 = row.insertCell();
            let column2 = row.insertCell();
            let column3 = row.insertCell();
            let column4 = row.insertCell();
            let column5 = row.insertCell();

            let countryCode = document.getElementById('countryCodeInput').value;
            let operatorCode = document.getElementById('operatorCodeInput').value;
            let phoneNumber = document.getElementById('phoneNumberInput').value;
            let phoneType = document.getElementById('phoneTypeInput').value;
            let commentary = document.getElementById('commentaryInput').value;

            let button = document.createElement("input");
            button.type = "button";
            button.className = "btn btn-primary  btn-md";
            button.value = "Edit";
            button.name = "phoneEditButton";
            button.onclick = function () {
                editNumber(this);
            };

            let ckBox = document.createElement("input");
            ckBox.type = "checkbox";
            ckBox.name = "phoneIdForDelete";
            ckBox.value = "new";

            let countryCodeInput = document.createElement("input");
            countryCodeInput.type = "hidden";
            countryCodeInput.name = "countryCode";
            countryCodeInput.value = countryCode;

            let operatorCodeInput = document.createElement("input");
            operatorCodeInput.type = "hidden";
            operatorCodeInput.name = "operatorCode";
            operatorCodeInput.value = operatorCode;

            let phoneTypeInput = document.createElement("input");
            phoneTypeInput.type = "hidden";
            phoneTypeInput.name = "phoneType";
            phoneTypeInput.value = phoneType;

            let commentaryInput = document.createElement("input");
            commentaryInput.type = "hidden";
            commentaryInput.name = "commentary";
            commentaryInput.value = commentary;

            let phoneNumberInput = document.createElement("input");
            phoneNumberInput.type = "hidden";
            phoneNumberInput.name = "phoneNumber";
            phoneNumberInput.value = phoneNumber;

            column1.append(ckBox);
            column1.append(operatorCodeInput);
            column1.append(countryCodeInput);
            column1.append(phoneNumberInput);
            column1.append(phoneTypeInput);
            column1.append(commentaryInput);
            column2.innerHTML = countryCode.concat(operatorCode, phoneNumber);
            column3.innerHTML = phoneType;
            column4.innerHTML = commentary;
            column5.append(button);
        } else {
            alert("Phone exists");
        }
        document.getElementById('countryCodeInput').value = "";
        document.getElementById('operatorCodeInput').value = "";
        document.getElementById('phoneNumberInput').value = "";
        document.getElementById('phoneTypeInput').value = "";
        document.getElementById('commentaryInput').value = "";
    }
}

function checkPhoneForUnique(phone) {
    let allNumber = document.getElementsByName('phoneNumber');
    for (let i = 0; i < allNumber.length; i++) {
        if (allNumber[i].value === phone)
            return false;
    }
    return true;
}

function deleteNumbers() {
    let amountOfDeleted = 0;
    let checkBoxes = document.getElementsByName('phoneIdForDelete');
    let length = checkBoxes.length;
    for (let i = 0; i < length; i++) {
        let box = checkBoxes[i - amountOfDeleted];
        if (box.checked === true) {
            if (box.value === "new") {
                document.getElementById('phoneTable').deleteRow(i + 1 - amountOfDeleted);
                amountOfDeleted++;
            } else {
                let table = document.getElementById('phoneTable');
                let row = table.rows[i - amountOfDeleted + 1];
                document.getElementById('undoButton').style.visibility = "visible";
                for (let j = 1; j < row.cells.length; j++) {
                    row.cells[j].style.setProperty("text-decoration", "line-through");
                }
                if (!phonesForDelete.includes(box.value))
                    phonesForDelete.push(box.value);
                box.checked = false;

                let tr = box.parentNode.parentNode;
                let index = tr.rowIndex - 1;
                let button = document.getElementsByName("phoneEditButton")[index];
                button.disabled = true;
            }
        }
    }
}

function undoDelete() {
    let checkBoxes = document.getElementsByName('phoneIdForDelete');

    for (let i = 0; i < checkBoxes.length; i++) {
        let box = checkBoxes[i];
        if (box.checked === true) {
            const id = box.value;
            const index = phonesForDelete.indexOf(id);
            if (index !== -1) {
                phonesForDelete.splice(index, 1);
                unCrossTableRow(i + 1);
            }

            let tr = box.parentNode.parentNode;
            let butIndex = tr.rowIndex - 1;
            let button = document.getElementsByName("phoneEditButton")[butIndex];
            button.disabled = false;
        }
        box.checked = false;
    }
}

function unCrossTableRow(rowIndex) {
    let table = document.getElementById('phoneTable');
    let row = table.rows[rowIndex];
    for (let j = 1; j < row.cells.length; j++) {
        row.cells[j].style.setProperty("text-decoration", "none");
    }
}

function submitAll() {
    if (isFieldEmpty(document.getElementById('name').value)) {
        alert("Input your name");
        return false;
    }
    if (isFieldEmpty(document.getElementById('surname').value)) {
        alert("Input your surname");
        return false;
    }
    if (!isEmailValid(document.getElementById('email').value)) {
        alert("Email Is Invalid");
        return false;
    }

    let form = document.getElementById('contactForm');
    let phoneNumberInput = document.createElement("input");
    phoneNumberInput.type = "hidden";
    phoneNumberInput.name = "numbersForDelete";
    phoneNumberInput.value = phonesForDelete;

    let attachmentInput = document.createElement("input");
    attachmentInput.type = "hidden";
    attachmentInput.name = "attachmentsForEdit";
    attachmentInput.value = attachmentsForEdit;

    form.append(phoneNumberInput);
    form.append(attachmentInput);
    form.submit();

}

function showPhotoModal() {
// Get the modal
    let modal = document.getElementById('phoneModal');

// Get the <span> element that closes the modal
    let span = document.getElementById("closePhoneModal");

// When the user clicks on the button, open the modal
    modal.style.display = "block";

// When the user clicks on <span> (x), close the modal
    span.onclick = function () {
        modal.style.display = "none";
    };

// When the user clicks anywhere outside of the modal, close it
    window.onclick = function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }
}

function editNumber(button) {
    let tr = button.parentNode.parentNode;
    let index = tr.rowIndex;
    editPhoneNumber(index - 1);
}


function editPhoneNumber(index) {
    let table = document.getElementById('phoneTable');
    let row = table.rows[index + 1];
    let submitButton = document.getElementById('phoneSubmitButton');

    let countryCode = document.getElementsByName('countryCode')[index];
    let operatorCode = document.getElementsByName('operatorCode')[index];
    let phoneNumber = document.getElementsByName('phoneNumber')[index];
    let phoneType = document.getElementsByName('phoneType')[index];
    let commentary = document.getElementsByName('commentary')[index];

    let countryCodeInput = document.getElementById('countryCodeInput');
    countryCodeInput.value = countryCode.value;

    let operatorCodeInput = document.getElementById('operatorCodeInput');
    operatorCodeInput.value = operatorCode.value;

    let phoneNumberInput = document.getElementById('phoneNumberInput');
    phoneNumberInput.value = phoneNumber.value;

    let phoneTypeInput = document.getElementById('phoneTypeInput');
    phoneTypeInput.value = phoneType.value;

    let commentaryInput = document.getElementById('commentaryInput');
    commentaryInput.value = commentary.value;
    openForm();

    submitButton.onclick = function () {
        if (isPhoneFormValid()) {
            phoneNumber.value = "";
            if (checkPhoneForUnique(phoneNumberInput.value)) {
                countryCode.value = countryCodeInput.value;
                operatorCode.value = operatorCodeInput.value;
                phoneNumber.value = phoneNumberInput.value;
                phoneType.value = phoneTypeInput.value;
                commentary.value = commentaryInput.value;
                row.cells[1].innerText = countryCode.value.concat(operatorCode.value, phoneNumber.value);
                row.cells[2].innerText = phoneType.value;
                row.cells[3].innerText = commentary.value;

                countryCodeInput.value = "";
                operatorCodeInput.value = "";
                phoneNumberInput.value = "";
                phoneTypeInput.value = "";
                commentaryInput.value = "";

                submitButton.onclick = function () {
                    addNumber();
                };
            } else {
                alert("Phone number exists");
            }
        }
    };
}

function showAttachmentModal() {
    let modal = document.getElementById('attachmentModal');
    let span = document.getElementById("closeAttachmentModal");

    modal.style.display = "block";
    span.onclick = function () {
        modal.style.display = "none";
    };
}

function replaceSubmittedAttachment(submittedFile) {
    let cln = submittedFile.cloneNode(false);
    cln.name = "attachment";
    cln.id = "";
    cln.style.display = "none";
    submittedFile.parentNode.removeChild(submittedFile);
    return cln;
}

function addAttachment() {
    let submittedFile = document.getElementById('submittedFile');

    if (submittedFile.value) {
        addAttachmentToTable(submittedFile);

        let newFileInput = document.createElement('input');
        newFileInput.type = 'file';
        newFileInput.id = "submittedFile";

        document.getElementById('attachmentField').append(newFileInput);
    }

    document.getElementById('fileNameInput').value = "";
    document.getElementById('fileCommentaryInput').value = "";
}

function addAttachmentToTable(submittedFile) {
    let fileName;
    let submittedFileName = submittedFile.files[0].name;
    let fileNameInput = document.getElementById('fileNameInput');
    let fileCommentaryInput = document.getElementById('fileCommentaryInput');

    if (isFieldEmpty(fileNameInput.value)) {
        fileName = submittedFileName;
    } else {
        fileName = fileNameInput.value;
    }

    let table = document.getElementById('attachmentTable');
    let row = table.insertRow();
    row.style.backgroundColor = "#98aa92";
    let column1 = row.insertCell();
    let column2 = row.insertCell();
    let column3 = row.insertCell();
    let column4 = row.insertCell();
    let column5 = row.insertCell();

    let button = document.createElement("input");
    button.type = "button";
    button.className = "btn btn-primary  btn-md";
    button.value = "Edit";
    button.name = "attachmentEditButton";
    button.onclick = function () {
        editNewAttachment(this)
    };

    let ckBox = document.createElement("input");
    ckBox.type = "checkbox";
    ckBox.name = "attachmentIdForDelete";
    ckBox.value = "new";

    let fileNameForSubmit = document.createElement('input');
    fileNameForSubmit.type = 'hidden';
    fileNameForSubmit.name = 'fileName';
    fileNameForSubmit.value = fileName;

    let fileCommentaryForSubmit = document.createElement('input');
    fileCommentaryForSubmit.type = 'hidden';
    fileCommentaryForSubmit.name = 'fileCommentary';
    fileCommentaryForSubmit.value = fileCommentaryInput.value;

    let fileExtension = document.createElement('input');
    fileExtension.type = 'hidden';
    fileExtension.name = 'fileExtension';
    fileExtension.value = submittedFileName.split('.').pop();

    let dateOfCreation = document.createElement('input');
    dateOfCreation.type = 'hidden';
    dateOfCreation.name = 'dateOfCreation';
    let date = getCurrentDate();

    dateOfCreation.value = date;

    column1.append(ckBox);
    column1.append(fileCommentaryForSubmit);
    column1.append(fileNameForSubmit);
    column1.append(fileExtension);
    column1.append(dateOfCreation);
    column1.append(replaceSubmittedAttachment(submittedFile));
    column2.innerHTML = fileName;
    column3.innerHTML = date;
    column4.innerHTML = fileCommentaryInput.value;
    column5.append(button);
}

function getCurrentDate() {
    let today = new Date();
    let dd = today.getDate();
    let mm = today.getMonth() + 1; //January is 0!
    let yyyy = today.getFullYear();
    if (dd < 10) {
        dd = '0' + dd
    }

    if (mm < 10) {
        mm = '0' + mm
    }
    return yyyy + '-' + mm + '-' + dd;
}

function editNewAttachment(button) {
    let tr = button.parentNode.parentNode;
    let index = tr.rowIndex;
    changeAttachment(index - 1)
}

function changeAttachment(index) {
    let table = document.getElementById('attachmentTable');
    let row = table.rows[index + 1];
    let button = document.getElementById('attachmentSubmitButton');

    let commentary = document.getElementsByName('fileCommentary')[index];
    let fileName = document.getElementsByName('fileName')[index];
    let file = document.getElementsByName('attachment')[index];
    let filePlacement = file.parentNode;
    let fileExtension = document.getElementsByName('fileExtension')[index];

    let oldFileInput = document.getElementById('submittedFile');
    let oldFileInputPlacement = oldFileInput.parentNode;
    oldFileInputPlacement.removeChild(oldFileInput);
    file.style.display = 'block';
    oldFileInputPlacement.append(file);
    file.onchange = function () {
        fileNameInput.value = file.files[0].name;
    };

    let commentaryInput = document.getElementById('fileCommentaryInput');
    commentaryInput.value = commentary.value;

    let fileNameInput = document.getElementById('fileNameInput');
    fileNameInput.value = fileName.value;

    button.innerText = "Confirm Edit";
    button.onclick = function () {
        if (!isFieldEmpty(fileNameInput.value)) {
            fileName.value = fileNameInput.value;
        }
        commentary.value = commentaryInput.value;
        fileExtension.value = file.files[0].name.split('.').pop();
        row.cells[1].innerText = fileName.value;
        row.cells[2].innerText = getCurrentDate();
        row.cells[3].innerText = commentary.value;
        filePlacement.append(replaceSubmittedAttachment(file));

        let newFileInput = document.createElement('input');
        newFileInput.type = 'file';
        newFileInput.id = 'submittedFile';
        newFileInput.onchange = function () {
            fileNameInput.value = newFileInput.files[0].name;
        };
        oldFileInputPlacement.append(newFileInput);

        commentaryInput.value = "";
        fileNameInput.value = "";

        button.innerText = "Submit Attachment";
        button.onclick = function () {
            addAttachment();
        }
    }
}

function editExistingAttachment(caller) {
    let tr = caller.parentNode.parentNode;
    let index = tr.rowIndex - 1; // input index
    let attachmentId = document.getElementsByName('attachmentId')[index].value;

    let table = document.getElementById('attachmentTable');
    let row = table.rows[index + 1];
    let button = document.getElementById('attachmentSubmitButton');

    let commentary = document.getElementsByName('fileCommentary')[index];
    let fileExtension = document.getElementsByName('fileExtension')[index];
    let fileName = document.getElementsByName('fileName')[index];
    let dateOfCreation = document.getElementsByName('dateOfCreation')[index];
    let file = document.getElementsByName('oldFile')[index];
    let filePlacement = file.parentNode;

    dateOfCreation.value = getCurrentDate();

    let fileInput = document.getElementById('submittedFile');
    fileInput.value = "";
    let oldFileInputPlacement = fileInput.parentNode;

    let commentaryInput = document.getElementById('fileCommentaryInput');
    commentaryInput.value = commentary.value;

    let fileNameInput = document.getElementById('fileNameInput');
    fileNameInput.value = fileName.value;

    button.innerText = "Confirm Edit";
    file.onchange = function () {
        fileNameInput.value = file.files[0].name;
    };
    button.onclick = function () {
        if (!isFieldEmpty(fileNameInput.value)) {
            fileName.value = fileNameInput.value;
        }
        commentary.value = commentaryInput.value;
        if (fileInput.value !== "") {
            fileExtension.value = fileInput.files[0].name.split('.').pop();
            filePlacement.append(replaceSubmittedAttachment(fileInput));
            if (!phonesForDelete.includes(attachmentId)) {
                attachmentsForEdit.push(attachmentId);
            }
        } else {
            filePlacement.append(replaceSubmittedAttachment(fileInput));
        }
        row.cells[1].innerText = fileName.value;
        row.cells[2].innerText = getCurrentDate();
        row.cells[3].innerText = commentary.value;

        let newFileInput = document.createElement('input');
        newFileInput.type = 'file';
        newFileInput.id = 'submittedFile';
        newFileInput.onchange = function () {
            fileNameInput.value = newFileInput.files[0].name;
        };
        oldFileInputPlacement.append(newFileInput);

        commentaryInput.value = "";
        fileNameInput.value = "";

        button.innerText = "Submit Attachment";
        button.onclick = function () {
            addAttachment();
        }
    }

}
