package parser;

import constants.RedisParseSymbols;
import parser.aggregate.ArrayParser;
import parser.simple.SimpleStringParser;

import java.util.Arrays;

public class ServerCommandParser {

    public Command parseCommand(String message) {
        if (message == null || message.isEmpty()) {
            System.out.println("Empty command from client");
            return null;
        }

        String parseSymbol = "" + message.charAt(0);

        // If it is a valid symbol
        if (Arrays.stream(RedisParseSymbols.REDIS_PARSE_SYMBOLS_ARRAY).findFirst().isPresent()) {
            Parser parser = getParser(parseSymbol);

            Command command = parser.parse(message);

            if (command == null) {
                System.out.println("Invalid message. The parser failed to parsed the message.");
                System.out.println("Message => " + message);
                return null;
            }

            // This is done so command can match with the enum commands
            command.setCommand(command.getCommand().toUpperCase());

            parser = null;

            return command;
        }
        else {
            System.out.println("Invalid Parse Symbol");
            return null;
        }
    }

    private Parser getParser(String parseSymbol) {
        return switch (parseSymbol) {
            case RedisParseSymbols.ARRAYS -> new ArrayParser();
            case RedisParseSymbols.SIMPLE_STRING,
                    RedisParseSymbols.SIMPLE_ERROR,
                    RedisParseSymbols.INTEGERS -> new SimpleStringParser();
            default -> null;
        };
    }
}
