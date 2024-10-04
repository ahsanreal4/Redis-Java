import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EventLoop {
    private ServerSocket socket;
    private final BlockingQueue<Socket> queue;



    public EventLoop(ServerSocket socket) {
        this.socket = socket;
        this.queue = new LinkedBlockingQueue<>(10);
    }

    private void writeToClient(Socket client, String message) {
        try {
            client.getOutputStream().write(message.getBytes());
        }
        catch (IOException exception) {
            removeClientSocket(client);
        }
    }

    public void runLoop() {

        while (true) {

            queue.forEach((client) -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    if (client.getInputStream().available() > 0) {
                        System.out.println("Entered here");
                        System.out.println("bytes => " + client.getInputStream().available());
                        String message = in.readLine();

                        writeToClient(client, "+PONG\r\n");
                        removeClientSocket(client);
                    }

                } catch (IOException e) {
                    removeClientSocket(client);
                }
            });

        }

    }

    public void addClientSocket(Socket clientSocket) {
        queue.add(clientSocket);
    }

    public void removeClientSocket(Socket clientSocket) {
        queue.remove(clientSocket);
    }
}
