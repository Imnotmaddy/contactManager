package app.command;

public class CommandFactory {
    public static Action chooseCommand(String action) {
        CommandType commandType = CommandType.valueOf(action);
        return commandType.getAction();
    }
}
