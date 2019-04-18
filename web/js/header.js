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
            document.getElementById('allContactsTable') ? gatherEmails()
                : headerModule.toContactList("Select recipients for email");
        },
        toSearchPage: function () {
            window.location = "http://localhost:8080/contactManager?command=redirect&page=searchPage"
        }
    }
}());

