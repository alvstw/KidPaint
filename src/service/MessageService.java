package service;

import helper.TCPHelper;
import model.PaintBoard;
import model.PaintData;
import model.client.ClientData;
import model.constant.MessageType;
import model.message.Message;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class MessageService {
    protected Socket socket;
    protected String senderID;
    protected String senderName;

    public MessageService(Socket socket){
        this.socket = socket;
    }

    public void sendChatMessage(String message) {
        sendMessage(senderID, senderName, MessageType.CHAT_MESSAGE.toString(), message);
    }

    public void sendChatMessage(String senderID, String senderName, String message) {
        sendMessage(senderID, senderName, MessageType.CHAT_MESSAGE.toString(), message);
    }

    public void sendMessage(String type, String message) {
        sendMessage(senderID, senderName, type, message);
    }

    public void sendMessage(String type) {
        sendMessage(senderID, senderName, type, "");
    }

    public void sendMessage(String senderID, String senderName, String type, String message) {
        if (socket.isClosed()) return;

        Message messageObj = new Message();
        messageObj.type = type;
        messageObj.senderID = senderID;
        messageObj.senderName = senderName;
        messageObj.message = message;
        TCPHelper.sendMessage(socket, messageObj);
    }

    public void sendPaintData(int col, int row, int color) {
        if (socket.isClosed()) return;

        sendObject(MessageType.PAINT_DATA.toString(), new PaintData(col, row, color));
        System.out.printf("Sent PaintData %d,%d (%d)\n", col, row, color);
    }

    public void sendPaintBoard(PaintBoard paintBoard) {
        if (socket.isClosed()) return;
        sendObject(MessageType.PAINT_BOARD.toString(), paintBoard);
        System.out.println("Sent PaintBoard");
    }

    public void sendObject(String type, Object object) {
        if (socket.isClosed()) return;

        Message messageObj = new Message();
        messageObj.type = type;
        messageObj.senderID = senderID;
        messageObj.senderName = senderName;
        messageObj.payload = object;
        TCPHelper.sendMessage(socket, messageObj);
    }
}
