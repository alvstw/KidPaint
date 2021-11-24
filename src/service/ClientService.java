package service;


import helper.UDPHelper;
import model.MessageData;
import model.constant.Constant;
import model.constant.MessageType;

import java.io.IOException;
import java.net.DatagramSocket;

public class ClientService {
    UDPHelper udpHelper;

    public ClientService() throws IOException {
        DatagramSocket socket = new DatagramSocket(Constant.clientUDPPort);
        udpHelper = new UDPHelper(socket);
    }

    public void pingServer() throws IOException {
        udpHelper.sendMessage(MessageType.FIND_SERVER_BROADCAST.toString(), "", Constant.broadcastAddress, Constant.serverUDPPort);
        while (true) {
            MessageData messageData = udpHelper.receive();
            if (messageData.type.equals(MessageType.SERVER_BROADCAST_RESPONSE.toString())) {
                System.out.printf("Establishing connection with %s:%s\n", messageData.sourceIPAddress, Constant.serverTCPPort);
            }
        }
    }

}
