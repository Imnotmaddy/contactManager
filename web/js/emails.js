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