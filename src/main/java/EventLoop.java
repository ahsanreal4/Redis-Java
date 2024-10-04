import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventLoop {
    private ServerSocket socket;
    private volatile List<Socket> clientSockets;


    public EventLoop(ServerSocket socket) {
        this.socket = socket;
        this.clientSockets = Collections.synchronizedList(new ArrayList<>());
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

            clientSockets.forEach((client) -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    if (client.getInputStream().available() > 0) {
                        System.out.println("Entered here");
                        System.out.println("bytes => " + client.getInputStream().available());
                        String message = in.readLine();

                        writeToClient(client, "+PONG\r\n");
                    }

                } catch (IOException e) {
                    removeClientSocket(client);
                }
            });
        }

    }

    public void addClientSocket(Socket clientSocket) {
        clientSockets.add(clientSocket);
    }

    public void removeClientSocket(Socket clientSocket) {
        clientSockets.remove(clientSocket);
    }
}
