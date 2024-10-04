import constants.RedisParseSymbols;
import enums.RedisCommands;

import java.util.Arrays;

public class ServerCommandParser {

    public RedisCommand parseCommand(String message) {
        if (message == null || message.isEmpty()) {
            System.out.println("Empty command from client");
            return null;
        }

        String parseSymbol = "" + message.charAt(0);

        // If it is a valid symbol
        if (Arrays.stream(RedisParseSymbols.REDIS_PARSE_SYMBOLS_ARRAY).findFirst().isPresent()) {
            System.out.println("Valid symbol => " + parseSymbol);

            return null;
        }
        else {
            System.out.println("Invalid Parse Symbol");
            return null;
        }
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
