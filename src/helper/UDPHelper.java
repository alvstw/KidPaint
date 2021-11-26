package helper;

import model.message.Message;

import java.io.IOException;
import java.net.*;

public class UDPHelper {
    DatagramSocket socket;

    public UDPHelper(DatagramSocket socket) {
        this.socket = socket;
    }

    public UDPHelper(int socketPort) {
        try {
            this.socket = new DatagramSocket(socketPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public Message receive(){
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            return null;
        }

        byte[] packetData = packet.getData();
        Message message = new Message();
        message.decode(packetData);
        message.sourceIPAddress = packet.getAddress().getHostAddress();
        message.sourcePort = packet.getPort();

        return message;
    }

    public void sendMessage(String messageType, String messageContent, String destIpAddress, int destPort){
        InetAddress destination = null;
        try {
            destination = InetAddress.getByName(destIpAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        }

        Message message = new Message();
        message.type = messageType;
        message.message = messageContent;
        byte[] packetContent = message.encode();
        if (packetContent.length > 1024) {
            System.out.println("UDP packet size is too large");
            return;
        }

        // create DatagramPacket
        DatagramPacket packet =
                new DatagramPacket(packetContent, packetContent.length, destination, destPort);

        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void end() {
        socket.close();
    }
}
