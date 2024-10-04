import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {
    private int port;
    private Selector selector;

    public Server(int port) {
        this.port = port;
    }

    private ServerSocketChannel initializeServerChannel() {
        // Open a ServerSocketChannel
        try {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false); // Set to non-blocking mode
            serverChannel.register(selector, SelectionKey.OP_ACCEPT); // Register for accept events

            return serverChannel;
        } catch (IOException e) {
            System.out.println("Error in initializing server socket channel");
            e.printStackTrace();
            return null;
        }
    }

    public void start() {
        try {
            // Create a Selector
            selector = Selector.open();

            ServerSocketChannel serverChannel = initializeServerChannel();

            if (serverChannel == null) {
                System.out.println("Shutting down...");
                return;
            }

            while (true) {
                // Wait for events
                selector.select();

                // Iterate through the selected keys
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    // Accept connection key
                    if (key.isAcceptable()) {
                        acceptClientConnection(serverChannel);
                    }
                    // Read key
                    else if (key.isReadable()) {
                        String message = readFromClient(key);
                    }
                    // Write key
                    else if (key.isWritable()) {
                        writeToClient("+PONG\r\n", key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptClientConnection(ServerSocketChannel serverChannel) {
        try {
            // Accept the connection
            SocketChannel clientChannel = serverChannel.accept();
            clientChannel.configureBlocking(false); // Set client to non-blocking mode
            clientChannel.register(selector, SelectionKey.OP_READ); // Register for read events
        }
        catch (IOException exception) {
            System.out.println("Error in accepting connection");
            exception.printStackTrace();
        }
    }

    private String readFromClient(SelectionKey key)  {
        try {
            // Read data from the client
            SocketChannel clientChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            // Read the incoming data
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead == -1) {
                // Client has closed the connection
                clientChannel.close();
                System.out.println("Client disconnected");
                return null;
            }
            else {
                buffer.flip();
                byte[] bytes = new byte[bytesRead];
                buffer.get(bytes);
                String message = new String(bytes);

                String response = "Server received: " + message;
                System.out.println(response);
                clientChannel.register(selector, SelectionKey.OP_WRITE); // Register for write events

                return message;
            }
        }
        catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private void writeToClient(String message, SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();

        try {
            clientChannel.write(ByteBuffer.wrap(message.getBytes()));
            clientChannel.register(selector, SelectionKey.OP_READ); // Switch back to read events
        } catch (IOException exception) {
            try {
                System.out.println("Error while writing to client. Closing connection.");
                clientChannel.close();
            }
            catch (IOException exception1) {
                System.out.println("Error while closing client");
            }
        }

    }

}

