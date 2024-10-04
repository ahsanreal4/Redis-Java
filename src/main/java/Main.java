import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args){
      int PORT = 6379;

      Server server = new Server(PORT);
      server.start();
  }
}
