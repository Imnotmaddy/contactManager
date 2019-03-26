package app.command;

import app.command.commandImpl.DisplayUsersCommand;

public enum CommandType {
CONTACT_LIST(new DisplayUsersCommand());

private Action action;
CommandType(Action action) { this.action = action;}

    public Action getAction() {
        return action;
    }
}
