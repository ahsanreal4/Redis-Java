package responder;

import constants.RedisResponses;
import parser.Command;

public class PingResponder extends Responder{
    @Override
    public String respondToCommand(Command command) {
        return RedisResponses.PONG_RESPONSE;
    }
}
