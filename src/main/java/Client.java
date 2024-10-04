import java.net.Socket;

public class Client {
    private static int CLIENT_ID = 1;

    private int id;
    private Socket socket;

    public Client (Socket socket) {
        this.socket = socket;
        this.id = CLIENT_ID;
        CLIENT_ID++;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public int getId() {
        return this.id;
    }
}
