package service.client;


import helper.TCPHelper;
import helper.UDPHelper;
import model.PaintData;
import model.Studio;
import model.UserProfile;
import model.client.ClientData;
import model.message.Message;
import model.constant.Constant;
import model.constant.MessageType;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientService {
    private final UDPHelper udpHelper;
    private Socket socket;
    private ClientThread clientThread;
    private ClientMessageService clientMessageService;

    public ClientService() throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();
        udpHelper = new UDPHelper(datagramSocket);
        ClientData.id = UUID.randomUUID().toString();
    }

    public void setUsername(String username) {
        ClientData.username = username;
    }

    public String getUsername() {
        return ClientData.username;
    }

    public void findServer() throws IOException {
        udpHelper.sendMessage(MessageType.FIND_SERVER_BROADCAST.toString(), "", Constant.broadcastAddress, Constant.serverUDPPort);
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
            socket = new Socket(ipAddress, tcpPort);
            clientMessageService = new ClientMessageService(socket);
            ClientData.clientMessageService = clientMessageService;
            System.out.println("Established connection with server");

            clientMessageService.sendMessage(MessageType.SET_CLIENT_ID.toString(), ClientData.id);
            clientMessageService.sendMessage(MessageType.SET_USERNAME.toString(), ClientData.username);

            clientThread = new ClientThread(socket, ClientData.id);
            clientThread.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Failed to establish connection with %s:%s\n", ipAddress, tcpPort);
        }
    }

    public boolean stopConnection() {
        try {
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public ClientMessageService getClientMessageService() {
        return clientMessageService;
    }

}
