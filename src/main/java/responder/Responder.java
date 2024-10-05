package responder;

import parser.Command;

public abstract class Responder {
    public abstract String respondToCommand(Command command);
}
