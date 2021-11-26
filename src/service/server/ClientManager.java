package service.server;

import model.UserProfile;

import java.net.Socket;
import java.util.ArrayList;

public class ClientManager {
    private ArrayList<ServerThread> clients = new ArrayList<>();

    public synchronized void addClient(ServerThread serverThread) {
        clients.add(serverThread);
    }

    public synchronized void addClient(Socket socket) {
        ServerThread serverThread = new ServerThread(socket);
        addClient(serverThread);
        serverThread.start();
    }

    public synchronized void removeClient(ServerThread serverThread) {
        clients.remove(serverThread);
    }

    public synchronized ArrayList<ServerThread> getClients() {
        return clients;
    }

    public synchronized ArrayList<UserProfile> getUsers() {
        ArrayList<UserProfile> profiles = new ArrayList<>();
        for(ServerThread thatThread : clients) {
            profiles.add(thatThread.getUserProfile());
        }
        return profiles;
    }
}
