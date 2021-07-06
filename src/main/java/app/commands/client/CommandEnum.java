package app.commands.client;

import app.commands.*;
import app.commands.DeleteContactsCommand;
import app.commands.SearchCommand;

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
            this.command = new ShowEditContactPageCommand();
        }
    },
    UPDATECONTACT {
        {
            this.command = new UpdateContactCommand();
        }
    },
    SHOWSENDEMAILS {
        {
            this.command = new ShowEmailPageCommand();
        }
    },
    SENDEMAILS {
        {
            this.command = new SendEmailCommand();
        }
    },
    SEARCHCONTACTS {
        {
            this.command = new SearchCommand();
        }
    },
    DOWNLOAD {
        {
            this.command = new DownloadCommand();
        }
    },
    CHANGEPAGE {
        {
            this.command = new ShowAllContactsCommand();
        }
    };


    ActionCommand command;

    public ActionCommand getCommand() {
        return command;
    }

}
