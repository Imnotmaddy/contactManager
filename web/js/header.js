let headerModule = (function () {
    return {
        toCreateContact: function () {
            window.location = `/contactManager?command=redirect&page=addContact`;
        },
        toContactList: function () {
            window.location = `/contactManager?command=redirect&page=contactList`;
        },
        sendEmail: function () {
            document.getElementById('allContactsTable') ? gatherEmails()
                : headerModule.toContactList();
        },
        toSearchPage: function () {
            window.location = `/contactManager?command=redirect&page=searchPage`
        }
    }
}());

