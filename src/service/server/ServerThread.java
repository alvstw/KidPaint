package service.server;

import helper.TCPHelper;
import model.PaintBoard;
import model.PaintData;
import model.constant.MessageType;
import model.message.Message;
import model.server.ServerData;
import model.UserProfile;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class ServerThread extends Thread {
    private final Socket socket;
    ServerMessageService serverMessageService;

    private final UserProfile userProfile;

    public ServerThread(Socket socket) {
        this.socket = socket;
        serverMessageService = new ServerMessageService(socket);

        userProfile = new UserProfile();
        userProfile.id = UUID.randomUUID().toString();
    }

    public void run() {
        System.out.printf("Established connection with %s\n", getIpAddress());

        while (!socket.isClosed()) {
            Message message = TCPHelper.receiveMessage(socket);
            if (message == null) break;

            if (message.type.equals(MessageType.SET_CLIENT_ID.toString())) {
                userProfile.id = message.message;
            }

            if (message.type.equals(MessageType.SET_USERNAME.toString())) {
                userProfile.username = message.message;
            }

            if (message.type.equals(MessageType.REQUEST_PAINT_BOARD.toString())) {
                serverMessageService.sendPaintBoard(ServerData.paintBoard);
                System.out.printf("Sending PaintData to %s(%s)\n", getClientID(), getUsername());
            }

            if (message.type.equals(MessageType.CHAT_MESSAGE.toString())) {
                for (ServerThread thatThread : ClientManager.getClients()) {
                    if (thatThread == this) continue;
                    System.out.printf("Sending PaintData to %s(%s)\n", thatThread.getClientID(), thatThread.getUsername());
                    thatThread.serverMessageService.sendChatMessage(getClientID(), getUsername(), message.message);
                }
            }

            if (message.type.equals(MessageType.PAINT_DATA.toString())) {
                PaintData paintData = (PaintData) message.payload;
                ServerData.paintBoard.setPixel(paintData.getCol(), paintData.getRow(), paintData.getColor());
                for (ServerThread thatThread : ClientManager.getClients()) {
                    if (thatThread == this) continue;
                    System.out.printf("Sending PaintData to %s(%s)\n", thatThread.getClientID(), thatThread.getUsername());
                    thatThread.serverMessageService.sendPaintData(paintData.getCol(), paintData.getRow(), paintData.getColor());
                }
            }

            if (message.type.equals(MessageType.PAINT_BOARD.toString())) {
                ServerData.paintBoard.clear();
                PaintBoard paintBoard = (PaintBoard) message.payload;
                ServerData.paintBoard.setData(paintBoard.getData());
                for (ServerThread thatThread : ClientManager.getClients()) {
                    if (thatThread == this) continue;
                    System.out.printf("Sending PAINT_BOARD to %s(%s)\n", thatThread.getClientID(), thatThread.getUsername());
                    thatThread.serverMessageService.sendPaintBoard(paintBoard);
                }
            }

            if (message.type.equals(MessageType.CLEAR_PAINT_BOARD.toString())) {
                ServerData.paintBoard.clear();
                for (ServerThread thatThread : ClientManager.getClients()) {
                    if (thatThread == this) continue;
                    System.out.printf("Sending CLEAR_PAINT_BOARD signal to %s(%s)\n", thatThread.getClientID(), thatThread.getUsername());
                    thatThread.serverMessageService.sendMessage(MessageType.CLEAR_PAINT_BOARD.toString(), "");
                }
            }

            if (message.type.equals(MessageType.REQUEST_CLIENT_LIST.toString())) {
                ArrayList<UserProfile> list = ClientManager.getUsers();
                serverMessageService.sendObject(MessageType.CLIENT_LIST.toString(), list);
            }

        }

        System.out.printf("Disconnected with client %s\n", getIpAddress());
        try {
            socket.close();
            ClientManager.removeClient(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIpAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    public int getPort() {
        return socket.getPort();
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public String getClientID() {
        return userProfile.id;
    }

    public String getUsername() {
        return userProfile.username;
    }
}
