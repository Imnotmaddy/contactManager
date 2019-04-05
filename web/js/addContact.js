let phonesForDelete = [];

function isPhoneFormValid() {
    if (!document.getElementById('countryCodeInput').value ||
        !document.getElementById('operatorCodeInput').value ||
        !document.getElementById('phoneNumberInput').value) {
        alert("Enter full phone number");
        return false;
    }
    return true;
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

            let countryCode = document.getElementById('countryCodeInput').value;
            let operatorCode = document.getElementById('operatorCodeInput').value;
            let phoneNumber = document.getElementById('phoneNumberInput').value;
            let phoneType = document.getElementById('phoneTypeInput').value;
            let commentary = document.getElementById('commentaryInput').value;

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
        } else {
            alert("Phone exists");
        }
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
    let form = document.getElementById('contactForm');
    let phoneNumberInput = document.createElement("input");
    phoneNumberInput.type = "hidden";
    phoneNumberInput.name = "numbersForDelete";
    phoneNumberInput.value = phonesForDelete;
    form.append(phoneNumberInput);
    form.submit();
}

/*
function editPhoneNumber(phone) {
    let countryCode = document.getElementById('countryCodeInput');
    let operatorCode = document.getElementById('operatorCodeInput');
    let phoneNumber = document.getElementById('phoneNumberInput');
    let phoneType = document.getElementById('phoneTypeInput');
    let commentary = document.getElementById('commentaryInput');

    let table = document.getElementById('phoneTable');
    for (let i = 1; i < table.rows.length; i++) {
        if (table.rows[i].cells[1].innerText === phone){
            countryCode.value = table.rows[i].cells[].value;
            operatorCode.value = table.rows[i].cells[].value;
            phoneNumber.value = table.rows[i].cells[1].value;
            phoneType.value = table.rows[i].cells[2].value;
            commentary.value = table.rows[i].cells[3].value;
            break;
        }
    }
}*/