package app.commands;

public enum PagePaths {

    ADD_CONTACT("/views/addContact.jsp"),
    CONTACT_LIST("/views/contactList.jsp"),
    EMAILS("/views/emails.jsp"),
    SEARCH_PAGE("/views/searchPage.jsp");
    String jspPath;

    PagePaths(String jspPath) {
        this.jspPath = jspPath;
    }

    public String getJspPath() {
        return jspPath;
    }
}
