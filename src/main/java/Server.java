import parser.Command;
import parser.ServerCommandParser;
import responder.ServerCommandResponder;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class Server {
    private final int port;
    private ServerEventsHandler serverEventsHandler;
    private ServerSocketChannel serverChannel;
    private Selector selector;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            // Create a Selector
            selector = Selector.open();
            serverEventsHandler = new ServerEventsHandler(selector);
            serverChannel = serverEventsHandler.initializeServerChannel(port);

            if (serverChannel == null) {
                System.out.println("Shutting down...");
                return;
            }

            startEventLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startEventLoop() throws IOException {
        ServerCommandParser serverCommandParser = new ServerCommandParser();
        ServerCommandResponder serverCommandResponder = new ServerCommandResponder();

        while (true) {
            // Wait for events
            selector.select();

            // Iterate through the selected keys
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();

                performAppropriateTask(key, serverCommandParser, serverCommandResponder);
            }
        }
    }

    private void performAppropriateTask(SelectionKey key, ServerCommandParser serverCommandParser,
                                        ServerCommandResponder serverCommandResponder){
        // Accept connection key
        if (key.isAcceptable()) {
            serverEventsHandler.acceptClientConnection(serverChannel);
        }
        // Read key
        else if (key.isReadable()) {
            String message = serverEventsHandler.readFromClient(key);
            Command command = serverCommandParser.parseCommand(message);

            if (command == null) return;

            performCommandAction(command, serverCommandResponder, key);
        }
        // Write key
//        else if (key.isWritable()) {
//            serverEventsHandler.writeToClient("+PONG\r\n", key);
//        }
    }

    private void performCommandAction(Command command, ServerCommandResponder serverCommandResponder,
                                      SelectionKey key) {
        String response = serverCommandResponder.respondToCommand(command);

        if (response == null) return;

        serverEventsHandler.writeToClient(response, key);
    }
}

