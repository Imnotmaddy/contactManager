package app.command;

public enum PagePath {
    ADD_CONTACT_PAGE("views/addContact.jsp"),
    INDEX_PAGE("/index.jsp"),
    CONTACT_LIST_PAGE("/views/contactList.jsp");


    String jspPath;

    PagePath(String jspPath) {
        this.jspPath = jspPath;
    }

    public String getJspPath() {
        return jspPath;
    }
}
