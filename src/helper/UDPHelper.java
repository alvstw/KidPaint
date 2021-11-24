package helper;

import model.MessageData;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class UDPHelper {
    DatagramSocket socket;

    public UDPHelper(DatagramSocket socket) {
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
        byte[] messageContentLengthBytes = new byte[4];
        String messageType;
        String messageContent;

        // define segment location
        int messageTypeLoc = 0;
        int messageContentLoc;

        // extract messageType
        System.arraycopy(packetData, messageTypeLoc, messageTypeLengthBytes, 0, 4);
        messageTypeLength = ByteBuffer.wrap(messageTypeLengthBytes).getInt();
        byte[] messageTypeBytes = new byte[messageTypeLength];
        System.arraycopy(packetData, messageTypeLoc + 4, messageTypeBytes, 0, messageTypeLength);
        messageType = Arrays.toString(messageTypeBytes);

        messageContentLoc = 4 + messageTypeLength;

        // extract messageContent
        System.arraycopy(packetData, messageContentLoc, messageContentLengthBytes, 0, 4);
        messageContentLength = ByteBuffer.wrap(messageContentLengthBytes).getInt();
        byte[] messageContentBytes = new byte[messageContentLength];
        System.arraycopy(packetData, messageContentLoc + 4, messageContentBytes, 0, messageContentLength);
        messageContent = Arrays.toString(messageContentBytes);

        // prepare payload
        messageData.type = messageType;
        messageData.message = messageContent;
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
                new DatagramPacket(packetContent, packetContent.length, destination, destPort);

        socket.send(packet);
    }

    public void end() {
        socket.close();
    }
}
