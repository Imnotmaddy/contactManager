package app.commands.client;

import app.commands.ActionCommand;
import app.commands.ShowAllContactsCommand;

public enum CommandEnum {
    LISTOFCONTACTS {
        {
            this.command = new ShowAllContactsCommand();
        }
    };


    ActionCommand command;

    public ActionCommand getCommand() {
        return command;
    }

}
