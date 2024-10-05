package parser.aggregate;

import constants.RedisParseSymbols;
import parser.Command;
import parser.Parser;

import java.util.ArrayList;

public class ArrayParser extends Parser {
    @Override
    public Command parse(String message) {
        String[] splitString = message.split(RedisParseSymbols.STRING_END_SYMBOL);

        if (splitString.length == 0) return null;

        String entriesString = splitString[0].substring(1);
        int entries = Integer.parseInt(entriesString);

        // Verify if there are correct number of entries in the string
        if (splitString.length != (entries * 2) + 1) {
            System.out.println("Required " + entries + " entries but few or more were passed");
            return null;
        }

        int nextStringLength = 0;
        String command = null;
        ArrayList<String> payloads = new ArrayList<>();

        for (int i = 1; i < splitString.length; i++) {
            String currentString = splitString[i];
            String symbol = "" + currentString.charAt(0);

            // If it is bulk string command
            if (symbol.equals(RedisParseSymbols.BULK_STRINGS)) {
                String nextString = currentString.substring(1);
                nextStringLength = Integer.parseInt(nextString);
            }
            // If it is the actual string
            else {
                if (currentString.length() != nextStringLength) {
                    System.out.println("The string passed were not equal to the length provided by the bulk string input");
                    return null;
                }

                // First take command
                if (command == null) {
                    command = currentString;
                }
                // Then take payload
                else {
                    payloads.add(currentString);
                }
            }
        }

        String payloadJoin = String.join(" ", payloads);

        return new Command(command, payloadJoin);
    }
}
