let headerModule = (function () {
    return {
        toCreateContact: function () {
            window.location = "http://localhost:8080/contactManager?command=redirect&page=addContact";
        },
        toContactList: function (error) {
            if (error !== null) {
                window.location = `http://localhost:8080/contactManager?command=redirect&page=contactList&error=${error}`;
            } else {
                window.location = `http://localhost:8080/contactManager?command=redirect&page=contactList`;
            }
        },
        sendEmail: function () {
            let contactTable = document.getElementById('allContactsTable');
            if (contactTable !== null) {
                gatherEmails();
            } else {
                headerModule.toContactList("Select recipients for email");
            }
        }
    }
}());

