package service;

import service.thread.ServerThread;

import java.net.Socket;
import java.util.ArrayList;

public class ServerService {
    private static ArrayList<ServerThread> clients = new ArrayList<>();

    public static synchronized void addClient(ServerThread serverThread) {
        clients.add(serverThread);
        serverThread.start();
    }

    public static synchronized void addClient(Socket socket) {
        ServerThread serverThread = new ServerThread(socket);
        ServerService.addClient(serverThread);
    }

    public static synchronized void removeClient(ServerThread serverThread) {
        clients.remove(serverThread);
    }

    public static synchronized ArrayList<ServerThread> getClients() {
        return clients;
    }
}
