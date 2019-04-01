package app.commands.factory;

import app.commands.ActionCommand;
import app.commands.EmptyCommand;
import app.commands.client.CommandEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class ActionFactory {
    private static final Logger LOGGER = LogManager.getLogger(ActionFactory.class);

    public ActionCommand defineCommand(HttpServletRequest request) {
        ActionCommand current = new EmptyCommand();
        String action = request.getParameter("command");
        LOGGER.debug(action);
        if (action == null || action.isEmpty()) {
            return current;
        }
        CommandEnum currentEnum = CommandEnum.valueOf(action.toUpperCase());
        current = currentEnum.getCommand();
        return current;
    }
}
