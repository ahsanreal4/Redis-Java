package responder.store;

import constants.RedisResponses;
import parser.Command;
import responder.Responder;
import store.RedisStore;

public class SetResponder extends Responder {
    @Override
    public String respondToCommand(Command command) {
        String payload = command.getPayload();

        String[] payloadSplit = payload.split(" ");

        if (payloadSplit.length != 2) {
            System.out.println("SET command needs a key and value to work");
            return null;
        }

        RedisStore.set(payloadSplit[0], payloadSplit[1]);

        return RedisResponses.OK_RESPONSE;
    }
}
