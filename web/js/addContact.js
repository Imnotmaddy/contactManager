let phonesForDelete = [];
let phonesForAdd = [];

function addNumber() {
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
        ckBox.name = "phoneId";
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
    let checkBoxes = document.getElementsByName('phoneId');
    let length = checkBoxes.length;
    for (let i = 0; i < length; i++) {
        let box = checkBoxes[i - amountOfDeleted];
        if (box.checked === true) {
            if (box.value === "new") {
                document.getElementById('phoneTable').deleteRow(i + 1 - amountOfDeleted);
                amountOfDeleted++;
            } else {
                if (!phonesForDelete.includes(box.value))
                    phonesForDelete.push(box.value);
            }
        }
    }
}

function submitAll() {

}


/*
function getPhoneData() {
    let phoneTable = document.getElementById('phoneTable');
    for (let i = 1; i < phoneTable.rows.length; i++) {
        let objCells = phoneTable.rows.item(i).cells;
        for (let j = 1; j < objCells.length; j++) {
            console.log(objCells.item(j).innerHTML)
        }
    }
}
*/