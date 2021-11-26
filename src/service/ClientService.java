package service;


import helper.TCPHelper;
import helper.UDPHelper;
import model.message.Message;
import model.constant.Constant;
import model.constant.MessageType;
import model.server.UserProfile;
import service.thread.ClientThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.UUID;

public class ClientService {
    private final UDPHelper udpHelper;
    private Socket clientSocket;
    private ClientThread clientThread;
    private UserProfile userProfile;
    private boolean hasServerConnection = false;

    public ClientService() throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();
        udpHelper = new UDPHelper(datagramSocket);
        userProfile = new UserProfile();
        userProfile.id = UUID.randomUUID().toString();
    }

    public void setUsername(String username) {
        userProfile.username = username;
    }

    public void findServer() throws IOException {
        udpHelper.sendMessage(MessageType.FIND_SERVER_BROADCAST.toString(), MessageType.FIND_SERVER_BROADCAST.toString(), Constant.broadcastAddress, Constant.serverUDPPort);
        System.out.println("Finding server on the network");
        System.out.printf("Sending broadcast message %s:%s\n", Constant.broadcastAddress, Constant.serverUDPPort);
        while (true) {
            System.out.println("Waiting for server on the network to respond");
            Message message = udpHelper.receive();
            if (message.type.equals(MessageType.SERVER_BROADCAST_RESPONSE.toString())) {
                establishConnection(message.sourceIPAddress, Constant.serverTCPPort);
                break;
            }
        }
    }

    private void establishConnection(String ipAddress, int tcpPort) {
        System.out.printf("Establishing connection with %s:%s\n", ipAddress, Constant.serverTCPPort);
        try {
            clientSocket = new Socket(ipAddress, tcpPort);
            System.out.println("Established connection with server");

            sendMessage(MessageType.SET_CLIENT_ID.toString(), userProfile.id);
            sendMessage(MessageType.SET_USERNAME.toString(), userProfile.username);

            clientThread = new ClientThread(clientSocket, userProfile);
            clientThread.start();
            hasServerConnection = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Failed to establish connection with %s:%s\n", ipAddress, tcpPort);
        }
    }

    public void sendChatMessage(String message) {
        if (!hasServerConnection) return;
        sendMessage(MessageType.CHAT_MESSAGE.toString(), message);
    }

    public void sendMessage(String type, String message) {
        Message messageObj = new Message();
        messageObj.type = type;
        messageObj.senderID = userProfile.id;
        messageObj.senderName = userProfile.username;
        messageObj.message = message;
        TCPHelper.sendMessage(clientSocket, messageObj);
    }

    public boolean stopConnection() {
        try {
            clientSocket.close();
            hasServerConnection = false;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
