import constants.RedisResponses;
import enums.RedisCommands;

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

        while (true) {
            // Wait for events
            selector.select();

            // Iterate through the selected keys
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();

                performAppropriateTask(key, serverCommandParser);
            }
        }
    }

    private void performAppropriateTask(SelectionKey key, ServerCommandParser serverCommandParser){
        // Accept connection key
        if (key.isAcceptable()) {
            serverEventsHandler.acceptClientConnection(serverChannel);
        }
        // Read key
        else if (key.isReadable()) {
            String message = serverEventsHandler.readFromClient(key);
            RedisCommand command = serverCommandParser.parseCommand(message);

            if (command == null) return;

            RedisCommands redisCommand = command.getType();
            String payload = command.getPayload();

            System.out.println(redisCommand.toString());
            System.out.println(payload);
            command = null;

            performCommandAction(redisCommand, payload, key);
        }
        // Write key
        else if (key.isWritable()) {
            serverEventsHandler.writeToClient("+PONG\r\n", key);
        }
    }

    private void performCommandAction(RedisCommands command, String payload, SelectionKey key) {
        switch (command) {
            case ECHO:
                System.out.println("command => " + command);
                System.out.println("payload => " + payload);
                serverEventsHandler.writeToClient(payload, key);
                break;
            case PING:
                serverEventsHandler.writeToClient(RedisResponses.PONG_RESPONSE, key);
                break;
            default:
                System.out.println("No commands matched");
        }
    }
}

