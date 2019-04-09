function gatherEmails() {
    let contactTable = document.getElementById('allContactsTable');
    if (contactTable !== null) {
        let ids = [];
        let checkboxes = document.getElementsByName('id');
        checkboxes.forEach(box => {
            if (box.checked) {
                ids.push(box.value)
            }
        });
        let idTransportElement = document.createElement('input');
        idTransportElement.type = 'hidden';
        idTransportElement.name = 'idsForEmail';
        idTransportElement.value = ids;

        let submitForm = document.getElementById('contactForm');
        submitForm.action = '/contactManager?command=showSendEmails';

        submitForm.append(idTransportElement);
        submitForm.submit();
    }
}