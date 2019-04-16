let phonesForDelete = [];

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
    return !(text === "");
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
            }
        }
    }
}

function undoDelete() {
    let checkBoxes = document.getElementsByName('phoneIdForDelete');

    for (let i = 0; i < checkBoxes.length; i++) {
        let box = checkBoxes[i];
        if (box.checked === true) {
            let id = box.value;
            const index = phonesForDelete.indexOf(id);
            if (index !== -1) {
                phonesForDelete.splice(index, 1);
                unCrossTableRow(i + 1);
            }
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
    if (!isFieldEmpty(document.getElementById('name').value)) {
        alert("Input your name");
        return false;
    }
    if (!isFieldEmpty(document.getElementById('surname').value)) {
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
    form.append(phoneNumberInput);
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
    editPhoneNumber(index - 1)
}


function editPhoneNumber(index) {
    let table = document.getElementById('phoneTable');
    let row = table.rows[index + 1];
    let button = document.getElementById('phoneSubmitButton');

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

    button.onclick = function () {
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

                button.onclick = function () {
                    addNumber();
                }
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

