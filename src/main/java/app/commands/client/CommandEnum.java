package app.commands.client;

import app.commands.*;
import app.commands.DeleteContactsCommand;

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
    },
    EDITCONTACT {
        {
            this.command = new EditContactCommand();
        }
    },
    UPDATECONTACT {
        {
            this.command = new UpdateContactCommand();
        }
    };


    ActionCommand command;

    public ActionCommand getCommand() {
        return command;
    }

}
