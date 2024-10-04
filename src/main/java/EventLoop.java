import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EventLoop {
    private ServerSocket socket;
    private final BlockingQueue<Client> queue;

    public EventLoop(ServerSocket socket) {
        this.socket = socket;
        this.queue = new LinkedBlockingQueue<>(10);
    }

    private void writeToClient(Client client, String message) {
        try {
            client.getSocket().getOutputStream().write(message.getBytes());
        }
        catch (IOException exception) {
            removeClient(client);
        }
    }

    public void runLoop() {

        while (true) {

            queue.forEach((client) -> {
                try {
                    Socket clientSocket = client.getSocket();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    if (clientSocket.getInputStream().available() > 0) {
                        System.out.println("Entered here => " + client.getId());

                        writeToClient(client, "+PONG\r\n");
                    }

                } catch (IOException e) {
                    removeClient(client);
                }
            });

        }

    }

    public void addClient(Client client) {
        queue.add(client);
    }

    public void removeClient(Client client) {
        queue.remove(client);
    }
}
