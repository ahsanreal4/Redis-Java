import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            // Create a Selector
            Selector selector = Selector.open();

            // Open a ServerSocketChannel
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false); // Set to non-blocking mode
            serverChannel.register(selector, SelectionKey.OP_ACCEPT); // Register for accept events

            System.out.println("Non-blocking server is listening on port 5000...");

            while (true) {
                // Wait for events
                selector.select();

                // Iterate through the selected keys
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isAcceptable()) {
                        // Accept the connection
                        SocketChannel clientChannel = serverChannel.accept();
                        clientChannel.configureBlocking(false); // Set client to non-blocking mode
                        clientChannel.register(selector, SelectionKey.OP_READ); // Register for read events
                        System.out.println("Connection established with client!");

                    } else if (key.isReadable()) {
                        // Read data from the client
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);

                        // Read the incoming data
                        int bytesRead = clientChannel.read(buffer);
                        if (bytesRead == -1) {
                            // Client has closed the connection
                            clientChannel.close();
                            System.out.println("Client disconnected.");
                        } else {
                            // Process the incoming data
                            buffer.flip(); // Prepare buffer for reading
                            byte[] bytes = new byte[bytesRead];
                            buffer.get(bytes);
                            String message = new String(bytes);

                            // Optionally, write a response back to the client
                            String response = "Server received: " + message;
                            System.out.println(response);
                            clientChannel.register(selector, SelectionKey.OP_WRITE); // Register for write events
                        }
                    } else if (key.isWritable()) {
                        // Write data to the client
                        SocketChannel clientChannel = (SocketChannel) key.channel();

                        clientChannel.write(ByteBuffer.wrap("+PONG\r\n".getBytes()));

                        // After writing, remove the attachment and unregister for write
                        key.attach(null); // Clear the attachment
                        clientChannel.register(selector, SelectionKey.OP_READ); // Switch back to read events
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

