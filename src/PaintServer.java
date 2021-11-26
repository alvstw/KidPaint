import model.constant.Constant;
import service.server.ClientManager;
import service.server.BroadcastServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PaintServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Constant.serverTCPPort);

        // Accept UDP broadcast signal
        BroadcastServerThread broadcastServerThread = new BroadcastServerThread();
        broadcastServerThread.start();

        // Accept TCP connection
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ClientManager.addClient(socket);
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
        }
    }
}
