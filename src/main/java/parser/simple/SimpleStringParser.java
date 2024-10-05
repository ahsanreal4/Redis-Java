package parser.simple;

import constants.RedisParseSymbols;
import parser.Command;
import parser.Parser;

public class SimpleStringParser extends Parser {
    @Override
    public Command parse(String message) {
        if (message == null || message.isEmpty()) {
            System.out.println("Empty message sent to parser");
            return null;
        }

        return useAppropriateParser(message);
    }

    private Command parseSimpleString(String message) {
        String[] splitString = message.split(RedisParseSymbols.STRING_END_SYMBOL);

        if (splitString.length != 1) return null;

        String commandString = splitString[0];
        String command = commandString.substring(1);

        return new Command(command, "");
    }

    private Command parseIntegerString(String message) {
        return  null;
    }

    private Command useAppropriateParser(String message) {
        String parseSymbol = "" + message.charAt(0);

        switch (parseSymbol) {
            case RedisParseSymbols.SIMPLE_STRING, RedisParseSymbols.SIMPLE_ERROR -> {
                return parseSimpleString(message);
            }
            case RedisParseSymbols.INTEGERS -> {
                return parseIntegerString(message);
            }
            default -> {
                return null;
            }
        }
    }
}
