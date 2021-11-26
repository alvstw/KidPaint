package service.server;

import model.UserProfile;

import java.net.Socket;
import java.util.ArrayList;

public class ClientManager {
    private static ArrayList<ServerThread> clients = new ArrayList<>();

    public static synchronized void addClient(ServerThread serverThread) {
        clients.add(serverThread);
        serverThread.start();
    }

    public static synchronized void addClient(Socket socket) {
        ServerThread serverThread = new ServerThread(socket);
        ClientManager.addClient(serverThread);
    }

    public static synchronized void removeClient(ServerThread serverThread) {
        clients.remove(serverThread);
    }

    public static synchronized ArrayList<ServerThread> getClients() {
        return clients;
    }

    public static synchronized ArrayList<UserProfile> getUsers() {
        ArrayList<UserProfile> profiles = new ArrayList<>();
        for(ServerThread thatThread : clients) {
            profiles.add(thatThread.getUserProfile());
        }
        return profiles;
    }
}
