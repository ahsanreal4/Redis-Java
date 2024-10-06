package responder;

import parser.Command;
import store.RedisStore;

public class ConfigResponder extends Responder{
    @Override
    public String respondToCommand(Command command) {
        if (command == null || command.getPayload() == null) return null;

        String[] payloadSplit = command.getPayload().split(" ");

        if (payloadSplit.length < 1) return null;

        String key = payloadSplit[1];
        String value = RedisStore.get(key);

        if (value == null) return null;

        String[] payload = {key, value};

        return super.createArrayResponse(payload);
    }
}
