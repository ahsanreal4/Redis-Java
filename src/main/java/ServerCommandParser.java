import enums.RedisCommands;

public class ServerCommandParser {

    public RedisCommand parseCommand(String message) {
        if (message == null || message.isEmpty()) {
            System.out.println("Empty command from client");
            return null;
        }

        String[] messageSplit = message.split(" ");

        if (messageSplit.length == 0) {
            System.out.println("Invalid Command");
            return null;
        }

        String command = messageSplit[0];
        RedisCommands commandType = getCommandType(command);

        if (commandType == null) return null;

        if (messageSplit.length == 1) {
            return new RedisCommand(commandType, "");
        }

        String payload = messageSplit[1];
        return new RedisCommand(commandType, payload);
    }

    private RedisCommands getCommandType(String command) {
        try {
            return RedisCommands.fromString(command);
        }
        catch (IllegalArgumentException argumentException) {
            System.out.println("Invalid command");
            return null;
        }
    }
}
