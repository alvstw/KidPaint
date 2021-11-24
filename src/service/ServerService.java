package service;

import helper.UDPHelper;
import model.constant.Constant;
import model.constant.MessageType;
import model.MessageData;

import java.io.IOException;
import java.net.DatagramSocket;

public class ServerService {
    UDPHelper udpHelper;

    public ServerService() throws IOException {
        DatagramSocket socket = new DatagramSocket(Constant.serverUDPPort);
        udpHelper = new UDPHelper(socket);
    }

    public void connectClient() throws IOException, InterruptedException {
        while (true) {
            MessageData messageData = udpHelper.receive();
            if (messageData.message.equals(MessageType.FIND_SERVER_BROADCAST.toString())) {
                udpHelper.sendMessage(MessageType.SERVER_BROADCAST_RESPONSE.toString(), "", messageData.sourceIPAddress, messageData.sourcePort);
            }
            Thread.sleep(1000);

        }
    }
}
