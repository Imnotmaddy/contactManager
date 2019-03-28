package app.commands.factory;

import app.commands.ActionCommand;
import app.commands.EmptyCommand;
import app.commands.client.CommandEnum;

import javax.servlet.http.HttpServletRequest;

public class ActionFactory {
    public ActionCommand defineCommand(HttpServletRequest request) {
        ActionCommand current = new EmptyCommand();
        String action = request.getParameter("command");
        if (action == null || action.isEmpty()) {
            return current;
        }
        CommandEnum currentEnum = CommandEnum.valueOf(action.toUpperCase());
        current = currentEnum.getCommand();
        return current;
    }
}
