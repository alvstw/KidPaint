package model.message;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Date;

public class Message implements Serializable {
    public String type;
    public String senderID = "";
    public String senderName = "";
    public String message = "";
    public Object payload;

    public String sourceIPAddress;
    public int sourcePort;

    public Date createdAt = new Date();

    public byte[] encode() {
        int length = 4 + 4 + type.length() + 4 + senderID.length() + 4 + senderName.length() + 4 + message.length() + 4 + createdAt.toString().length();
        byte[] packetContent = new byte[length];

        // convert  length to bytes
        byte[] messageLengthBytes = ByteBuffer.allocate(4).putInt(length).array();
        byte[] messageTypeLengthBytes = ByteBuffer.allocate(4).putInt(type.length()).array();
        byte[] messageSenderIDLengthBytes = ByteBuffer.allocate(4).putInt(senderID.length()).array();
        byte[] messageSenderNameLengthBytes = ByteBuffer.allocate(4).putInt(senderName.length()).array();
        byte[] messageContentLengthBytes = ByteBuffer.allocate(4).putInt(message.length()).array();
        byte[] messageCreatedAtLengthBytes = ByteBuffer.allocate(4).putInt(createdAt.toString().length()).array();

        // define  location
        int messageLengthLoc = 0;
        int messageTypeLoc = messageLengthLoc + 4;
        int messageSenderIDLoc = messageTypeLoc + 4 + type.length();
        int messageSenderNameLoc = messageSenderIDLoc + 4 + senderID.length();
        int messageContentLoc = messageSenderNameLoc + 4 + senderName.length();
        int messageCreatedAtLoc = messageContentLoc + 4 + message.length();

        // assign data into byte array
        System.arraycopy(messageLengthBytes, 0, packetContent, messageLengthLoc, 4);
        System.arraycopy(messageTypeLengthBytes, 0, packetContent, messageTypeLoc, 4);
        System.arraycopy(type.getBytes(), 0, packetContent, messageTypeLoc + 4, type.length());
        System.arraycopy(messageSenderIDLengthBytes, 0, packetContent, messageSenderIDLoc, 4);
        System.arraycopy(senderID.getBytes(), 0, packetContent, messageSenderIDLoc + 4, senderID.length());
        System.arraycopy(messageSenderNameLengthBytes, 0, packetContent, messageSenderNameLoc, 4);
        System.arraycopy(senderName.getBytes(), 0, packetContent, messageSenderNameLoc + 4, senderName.length());
        System.arraycopy(messageContentLengthBytes, 0, packetContent, messageContentLoc, 4);
        System.arraycopy(message.getBytes(), 0, packetContent, messageContentLoc + 4, message.length());
        System.arraycopy(messageCreatedAtLengthBytes, 0, packetContent, messageCreatedAtLoc, 4);
        System.arraycopy(createdAt.toString().getBytes(), 0, packetContent, messageCreatedAtLoc + 4, createdAt.toString().length());

        return packetContent;
    }

    public Message decode(byte[] data) {
        int segmentLength;
        byte[] lengthBytes = new byte[4];
        int nextLoc = 4;

        if (data.length < 20) {
            System.out.printf("Invalid packet: the size is %s\n", data.length);
            return null;
        }

        // extract type
        System.arraycopy(data, nextLoc, lengthBytes, 0, 4);
        segmentLength = ByteBuffer.wrap(lengthBytes).getInt();
        byte[] bytes = new byte[segmentLength];
        System.arraycopy(data, nextLoc + 4, bytes, 0, segmentLength);
        type = new String(bytes);
        nextLoc = nextLoc + 4 + segmentLength;

        // extract senderID
        System.arraycopy(data, nextLoc, lengthBytes, 0, 4);
        segmentLength = ByteBuffer.wrap(lengthBytes).getInt();
        bytes = new byte[segmentLength];
        System.arraycopy(data, nextLoc + 4, bytes, 0, segmentLength);
        senderID = new String(bytes);
        nextLoc = nextLoc + 4 + segmentLength;

        // extract senderName
        System.arraycopy(data, nextLoc, lengthBytes, 0, 4);
        segmentLength = ByteBuffer.wrap(lengthBytes).getInt();
        bytes = new byte[segmentLength];
        System.arraycopy(data, nextLoc + 4, bytes, 0, segmentLength);
        senderName = new String(bytes);
        nextLoc = nextLoc + 4 + segmentLength;

        // extract messageContent
        System.arraycopy(data, nextLoc, lengthBytes, 0, 4);
        segmentLength = ByteBuffer.wrap(lengthBytes).getInt();
        bytes = new byte[segmentLength];
        System.arraycopy(data, nextLoc + 4, bytes, 0, segmentLength);
        message = new String(bytes);
        nextLoc = nextLoc + 4 + segmentLength;

        // extract createdAt
        System.arraycopy(data, nextLoc, lengthBytes, 0, 4);
        segmentLength = ByteBuffer.wrap(lengthBytes).getInt();
        bytes = new byte[segmentLength];
        System.arraycopy(data, nextLoc + 4, bytes, 0, segmentLength);
        createdAt = new Date();

        return this;
    }
}
