package service;

import helper.TCPHelper;
import model.PaintBoard;
import model.PaintData;
import model.constant.MessageType;
import model.message.Message;

import java.net.Socket;

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

        System.out.printf("Sending PaintData %d,%d (%d)\n", col, row, color);
        sendObject(MessageType.PAINT_DATA.toString(), new PaintData(col, row, color));
    }

    public void sendPaintBoard(PaintBoard paintBoard) {
        if (socket.isClosed()) return;
        System.out.println("Sending PaintBoard");
        sendObject(MessageType.PAINT_BOARD.toString(), paintBoard);
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
