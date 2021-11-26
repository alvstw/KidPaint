package service.client;

import model.client.ClientData;
import model.constant.MessageType;
import service.MessageService;

import java.net.Socket;

public class ClientMessageService extends MessageService {
    public ClientMessageService(Socket socket) {
        super(socket);
        senderID = ClientData.id;
        senderName = ClientData.username;
    }

    public void requestPaintBoard() {
        sendMessage(MessageType.REQUEST_PAINT_BOARD.toString(), "");
    }
}
