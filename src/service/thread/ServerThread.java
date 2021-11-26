package service.thread;

import helper.TCPHelper;
import model.constant.Constant;
import model.message.Message;
import model.constant.MessageType;
import model.server.UserProfile;
import service.ServerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class ServerThread extends Thread {
    private final Socket socket;
    private final UserProfile userProfile;

    public ServerThread(Socket socket) {
        this.socket = socket;
        userProfile = new UserProfile();
        userProfile.id = UUID.randomUUID().toString();

        sendMessage(MessageType.SET_CLIENT_ID.toString(), userProfile.id);
    }

    public void run() {
        System.out.printf("Established connection with %s\n", getIpAddress());

        while (!socket.isClosed()) {
            Message message = TCPHelper.receiveMessage(socket);
            if (message == null) break;

            if (message.type.equals(MessageType.SET_CLIENT_ID.toString())) {
                userProfile.id = message.message;
            }

            if (message.type.equals(MessageType.SET_USERNAME.toString())) {
                userProfile.username = message.message;
            }

            if (message.type.equals(MessageType.CHAT_MESSAGE.toString())) {
                for (ServerThread thatThread : ServerService.getClients()) {
                    System.out.printf("Sending message to %s:%d\n", thatThread.getIpAddress(), thatThread.getPort());
                    sendChatMessage(message.message);
                }
            }

        }

        System.out.printf("Disconnected with client %s\n", getIpAddress());
        try {
            socket.close();
            ServerService.removeClient(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendChatMessage(String message) {
        if (socket.isClosed()) return;
        sendMessage(MessageType.CHAT_MESSAGE.toString(), message);
    }

    public void sendMessage(String type, String message) {
        if (socket.isClosed()) return;
        Message messageObj = new Message();
        messageObj.type = type;
        messageObj.senderID = Constant.serverID;
        messageObj.senderName = Constant.serverName;
        messageObj.message = message;
        TCPHelper.sendMessage(socket, messageObj);
    }

    public String getIpAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    public int getPort() {
        return socket.getPort();
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }
}
