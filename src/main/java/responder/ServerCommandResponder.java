package responder;

import enums.RedisCommands;
import parser.Command;
import responder.store.GetResponder;
import responder.store.SetResponder;

public class ServerCommandResponder {

    private final String INVALID_COMMAND_RESPONSE = "Invalid command passed to Responder";
    private final String NULL_COMMAND_RESPONSE = "Null command passed to Responder";

    public String respondToCommand(Command command) {
        if (command == null) {
            System.out.println(NULL_COMMAND_RESPONSE);
            return null;
        }

        Responder responder = getResponder(command);

        if (responder == null) {
            System.out.println(INVALID_COMMAND_RESPONSE);
            return null;
        }

        String response = responder.respondToCommand(command);

        responder = null;
        command = null;

        return response;
    }

    private Responder getResponder(Command command) {
        RedisCommands redisCommand;

        try {
            redisCommand = RedisCommands.valueOf(command.getCommand());
        }
        catch (IllegalArgumentException exception) {
            return null;
        }

        return switch (redisCommand) {
            case ECHO -> new EchoResponder();
            case PING -> new PingResponder();
            case SET -> new SetResponder();
            case GET -> new GetResponder();
            case CONFIG -> new ConfigResponder();
            default -> null;
        };
    }
}
