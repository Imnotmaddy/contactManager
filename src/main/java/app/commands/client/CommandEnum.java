package app.commands.client;

import app.commands.ActionCommand;
import app.commands.AddContactCommand;
import app.commands.RedirectCommand;
import app.commands.ShowAllContactsCommand;
import app.commands.factory.DeleteContactsCommand;

public enum CommandEnum {
    LISTOFCONTACTS {
        {
            this.command = new ShowAllContactsCommand();
        }
    },
    DELETECONTACTS {
        {
            this.command = new DeleteContactsCommand();
        }
    },
    REDIRECT {
        {
            this.command = new RedirectCommand();
        }
    },
    ADDCONTACT {
        {
            this.command = new AddContactCommand();
        }
    };


    ActionCommand command;

    public ActionCommand getCommand() {
        return command;
    }

}
