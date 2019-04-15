function openForm() {
    document.getElementById("popupPhoneNumber").style.display = "block";
}

function closeForm() {
    document.getElementById("popupPhoneNumber").style.display = "none";
    document.getElementById('countryCodeInput').value = "";
    document.getElementById('operatorCodeInput').value = "";
    document.getElementById('phoneNumberInput').value = "";
    document.getElementById('phoneTypeInput').value = "";
    document.getElementById('commentaryInput').value = "";
    document.getElementById('phoneSubmitButton').onclick = function () {
        addNumber();
    }
}