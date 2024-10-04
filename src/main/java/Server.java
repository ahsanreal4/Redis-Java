import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);

            EventLoop eventLoop = new EventLoop(serverSocket);

            Thread eventLoopThread = new Thread(eventLoop::runLoop);
            eventLoopThread.start();

            for (int i = 0; i < 2; i++) {
                clientSocket = serverSocket.accept();
                eventLoop.addClientSocket(clientSocket);
            }

        }

        catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        finally {

            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            }

            catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }
}
