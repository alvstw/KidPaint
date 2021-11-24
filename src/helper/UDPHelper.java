package helper;

import model.MessageData;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class UDPHelper {
    DatagramSocket socket;

    public UDPHelper(DatagramSocket socket) throws SocketException {
        this.socket = socket;
    }

    public MessageData receive() throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
        socket.receive(packet);

        byte[] packetData = packet.getData();
        MessageData messageData = new MessageData();

        int messageTypeLength;
        int messageContentLength;
        byte[] messageTypeLengthBytes = new byte[4];
        byte[] packetSizeBytes = new byte[4];
        byte[] messageTypeBytes;
        byte[] messageContentBytes;

        // define segment location
        int messageTypeSegmentLoc = 0;
        int messageContentLengthBytes;
        System.arraycopy(packetData, 0, messageTypeLengthBytes, 0, 4);
        messageTypeLength = ByteBuffer.wrap(messageTypeLengthBytes).getInt();
        messageContentLengthBytes = 4 + messageTypeLength;
        ByteBuffer.wrap(messageContentLengthBytes).getInt();
        messageContentBytes = new byte[]
        System.arraycopy(packetData, messageContentLengthBytes, messageContentBytes, 0, 4);


        messageData.type = "";
        messageData.message = new String(packetData, 0, packet.getLength());
        messageData.sourceIPAddress = packet.getAddress().toString();
        messageData.sourcePort = packet.getPort();

        return messageData;
    }

    public void sendMessage(String messageType, String messageContent, String destIpAddress, int destPort) throws IOException {
        InetAddress destination = InetAddress.getByName(destIpAddress);

        byte[] packetContent = new byte[1024];

        // convert segment length to bytes
        byte[] messageTypeLengthBytes = ByteBuffer.allocate(4).putInt(messageType.length()).array();
        byte[] messageContentLengthBytes = ByteBuffer.allocate(4).putInt(messageContent.length()).array();

        // define segment location
        int messageTypeSegmentLoc = 0;
        int messageContentSegmentLoc = 4 + messageType.length();

        // assign data into byte array
        System.arraycopy(messageTypeLengthBytes, 0, packetContent, messageTypeSegmentLoc, 4);
        System.arraycopy(messageType.getBytes(), 0, packetContent, messageTypeSegmentLoc + 4, messageType.length());
        System.arraycopy(messageContentLengthBytes, 0, packetContent, messageContentSegmentLoc, 4);
        System.arraycopy(messageContent.getBytes(), 0, packetContent, messageContentSegmentLoc + 4, messageContent.length());

        // create DatagramPacket
        DatagramPacket packet =
                new DatagramPacket(messageContent.getBytes(), messageContent.length(), destination, destPort);

        socket.send(packet);
    }

    public void end() {
        socket.close();
    }
}
