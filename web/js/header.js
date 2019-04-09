var headerModule = (function () {
    return {
        toCreateContact: function () {
            window.location = "http://localhost:8080/contactManager?command=redirect&page=addContact";
        },
        toContactList: function () {
            window.location = "http://localhost:8080/contactManager?command=redirect&page=contactList";
        },
        toEmails: function () {
            window.location = "http://localhost:8080/contactManager?command=redirect&page=sendEmail";
        }
    }
}());

