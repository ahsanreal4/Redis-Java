package responder.store;

import constants.RedisParseSymbols;
import constants.RedisResponses;
import parser.Command;
import responder.Responder;
import store.RedisStore;

public class GetResponder extends Responder {
    @Override
    public String respondToCommand(Command command) {
        String payload = command.getPayload();

        String[] payloadSplit = payload.split(" ");

        if (payloadSplit.length != 1) {
            System.out.println("GET commands only receive a single key");
            return null;
        }

        String value = RedisStore.get(payload);

        if (value == null) return RedisResponses.NULL_BULK_RESPONSE;

        String response = RedisParseSymbols.BULK_STRINGS + value.length() + RedisParseSymbols.STRING_END_SYMBOL;
        response += value + RedisParseSymbols.STRING_END_SYMBOL;

        return response;
    }
}
