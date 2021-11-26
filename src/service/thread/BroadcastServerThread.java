package service.thread;

import helper.UDPHelper;
import model.message.Message;
import model.constant.Constant;
import model.constant.MessageType;

public class BroadcastServerThread extends Thread {
    public boolean running = true;
    private final UDPHelper udpHelper;

    public BroadcastServerThread() {
        udpHelper = new UDPHelper(Constant.serverUDPPort);
    }

    @Override
    public void run() {
        System.out.printf("Listening udp/%s client on the network\n", Constant.serverUDPPort);
        while (running) {
            Message message = udpHelper.receive();
            System.out.println(Constant.consoleSeparator);
            System.out.printf("Receiving package from %s:%s\n", message.sourceIPAddress, message.sourcePort);
            System.out.printf("Message Type: %s\n", message.type);
            System.out.printf("Message Content: %s\n", message.message);
            if (message.message.equals(MessageType.FIND_SERVER_BROADCAST.toString())) {
                System.out.printf("Responding to client %s:%s\n", message.sourceIPAddress, message.sourcePort);
                udpHelper.sendMessage(MessageType.SERVER_BROADCAST_RESPONSE.toString(), "", message.sourceIPAddress, message.sourcePort);
            }
            System.out.println(Constant.consoleSeparator);
        }
    }
}
