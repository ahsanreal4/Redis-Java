import enums.RedisCommands;

public class ServerCommandParser {

    public RedisCommand parseCommand(String message) {
        if (message == null || message.isEmpty()) {
            System.out.println("Empty message from client");
            return null;
        }

        String[] messageSplit = message.split(" ");

        if (message.length() < 2) {
            System.out.println("Invalid message from client");
            return null;
        }

        String command = messageSplit[0];
        String payload = messageSplit[1];

        RedisCommands commandType = getCommandType(command);

        if (commandType == null) return null;

        return new RedisCommand(commandType, payload);
    }

    private RedisCommands getCommandType(String command) {
        try {
            return RedisCommands.valueOf(command);
        }
        catch (IllegalArgumentException argumentException) {
            System.out.println("Invalid command");
            return null;
        }
    }
}
