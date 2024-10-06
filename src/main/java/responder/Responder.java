package responder;

import constants.RedisParseSymbols;
import parser.Command;

public abstract class Responder {
    public abstract String respondToCommand(Command command);

    public String createArrayResponse(String[] payload) {
        if (payload.length == 0) return null;

        StringBuilder response = new StringBuilder(RedisParseSymbols.ARRAYS + payload.length + RedisParseSymbols.STRING_END_SYMBOL);

        for (int i = 0; i < payload.length; i++) {
            String currentPayload = payload[i];

            response.append(RedisParseSymbols.BULK_STRINGS).append(currentPayload.length()).append(RedisParseSymbols.STRING_END_SYMBOL);
            response.append(currentPayload).append(RedisParseSymbols.STRING_END_SYMBOL);
        }

        return response.toString();
    }
}
