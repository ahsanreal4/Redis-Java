import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            // Create a non-blocking ServerSocketChannel
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false); // Set non-blocking mode

            System.out.println("Server is listening on port 8080");

            while (true) {
                // Accept a client connection in non-blocking mode
                SocketChannel clientChannel = serverSocketChannel.accept();

                if (clientChannel != null) {
                    System.out.println("Client connected from: " + clientChannel.getRemoteAddress());
                    clientChannel.configureBlocking(false);

                    // Read data from the client in non-blocking mode
                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    int bytesRead = clientChannel.read(buffer);
                    if (bytesRead > 0) {
                        buffer.flip();
                        byte[] data = new byte[buffer.remaining()];
                        buffer.get(data);

                        String message = new String(data);
                        if (message != null && message.equalsIgnoreCase("ping")) {
                            System.out.println("here baby");
                            writeToClient("+PONG\r\n", clientChannel);
                        }

                    } else if (bytesRead == -1) {
                        // The client has closed the connection
                        System.out.println("Client disconnected");
                        clientChannel.close();
                    }
                }

                // Do other work here, the server isn't blocked while waiting for data
            }
        }
        catch (IOException exception) {
            System.out.println("Shutting down server...");
            exception.printStackTrace();
        }
    }

    private void writeToClient(String message, SocketChannel client) {
        try {
            client.write(ByteBuffer.wrap(message.getBytes()));
        } catch (IOException e) {
            System.out.println("Connection closed with client");
        }
    }
}
