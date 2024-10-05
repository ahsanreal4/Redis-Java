package responder;

import constants.RedisParseSymbols;
import parser.Command;

public class EchoResponder extends Responder{
    @Override
    public String respondToCommand(Command command) {
        String payload = command.getPayload();

        String response = RedisParseSymbols.BULK_STRINGS + payload.length() + RedisParseSymbols.STRING_END_SYMBOL;
        response += payload + RedisParseSymbols.STRING_END_SYMBOL;

        return response;
    }
}
