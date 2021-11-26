package service.server;

import model.constant.Constant;
import service.MessageService;

import java.net.Socket;

public class ServerMessageService extends MessageService {
    public ServerMessageService(Socket socket) {
        super(socket);
        senderID = Constant.serverID;
        senderName = Constant.serverName;
    }
}
