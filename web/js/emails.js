function resizeArea(area) {
    let height = area.scrollHeight;
    area.style.height = `${height}` + "px";
}

function sendEmail() {
    let table = document.getElementById('recipientsTable');
    let recipients = [];
    for (let i = 1; i < table.rows.length; i++) {
        recipients.push(table.rows[i].cells[1].innerText);
    }
    let currentEmails = document.createElement('input');
    currentEmails.type = "hidden";
    currentEmails.name = "currentRecipients";
    currentEmails.value = recipients;

    let form = document.getElementById('emailForm');
    form.append(currentEmails);
    form.submit();
}

function selectTemplate() {
    let names = document.getElementsByName('names');
    let selectedBox = document.getElementById('templateInput');
    let selectedTemplate = selectedBox.options[selectedBox.selectedIndex].value;

    if (selectedTemplate === 'businessTemplate') {
        document.getElementById('msgInput').value = myBusinessTemplate(names);
    }

    if (selectedTemplate === 'casualTemplate') {
        document.getElementById('msgInput').value = myCasualTemplate(names);
    }
}

function myBusinessTemplate(names) {
    let message = `Greetings `;
    for (let i = 0; i < names.length; i++) {
        message += names[i].value + ', ';
    }
    message += '\n\n\n\n Sincerely yours, \n';
    return message;
}

function myCasualTemplate(names) {
    let message = `Hey `;
    for (let i = 0; i < names.length; i++) {
        message += names[i].value + ', ';
    }
    message += '\n\nI hope you are doing great. ';
    message += '\n\n\n\n Yours truly, \n';
    return message;
}