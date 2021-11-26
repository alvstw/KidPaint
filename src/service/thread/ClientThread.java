package service.thread;

import helper.TCPHelper;
import model.constant.MessageType;
import model.message.Message;
import model.server.UserProfile;

import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread{
    private final Socket socket;

    private final UserProfile userProfile;

    public ClientThread(Socket socket, UserProfile userProfile) {
        this.socket = socket;
        this.userProfile = userProfile;
    }

    public void run() {
        while (!socket.isClosed()) {
            Message message = TCPHelper.receiveMessage(socket);
            if (message == null) continue;

            if (message.type.equals(MessageType.SET_CLIENT_ID.toString())) {
                userProfile.id = message.message;
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIpAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    public String getClientID() {
        return userProfile.id;
    }
}
