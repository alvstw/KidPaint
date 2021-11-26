package service.client;

import helper.TCPHelper;
import model.PaintBoard;
import model.PaintData;
import model.UserProfile;
import model.client.ClientData;
import model.constant.Constant;
import model.constant.MessageType;
import model.message.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread extends Thread {
    private final Socket socket;

    private final String clientID;

    public ClientThread(Socket socket, String clientID) {
        this.socket = socket;
        this.clientID = clientID;
    }

    public void run() {
        while (!socket.isClosed()) {
            Message message = TCPHelper.receiveMessage(socket);
            if (message == null) continue;

            if (message.type.equals(MessageType.SET_CLIENT_ID.toString())) {

            }

            if (message.type.equals(MessageType.CLEAR_PAINT_BOARD.toString())) {
                ClientData.ui.clearPaintBoard();
            }

            if (message.type.equals(MessageType.CHAT_MESSAGE.toString())) {
                ClientData.ui.addChatMessage(message.senderName, message.message);
            }

            if (message.type.equals(MessageType.PAINT_BOARD.toString())) {
                PaintBoard paintBoard = (PaintBoard) message.payload;
                ClientData.ui.setData(paintBoard.getData(), Constant.blockSize);
                System.out.println("Downloaded PaintBoard");
            }

            if (message.type.equals(MessageType.PAINT_DATA.toString())) {
                PaintData paintData = (PaintData) message.payload;
                ClientData.ui.setPixel(paintData.getCol(), paintData.getRow(), paintData.getColor());
                System.out.printf("Painting %d,%d (%d)\n", paintData.getCol(), paintData.getRow(), paintData.getColor());
            }

            if (message.type.equals(MessageType.CLIENT_LIST.toString())) {
                ArrayList<UserProfile> userProfileList = new ArrayList<>();
                if (message.payload instanceof ArrayList<?>) {
                    List<?> objects = (List<?>) message.payload;
                    for(Object object : objects){
                        UserProfile userProfile = (UserProfile) object;
                        userProfileList.add(userProfile);
                    }
                }
                ClientData.connectedUsers = userProfileList;
                ClientData.ui.printOnlineUsers();
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIpAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    public String getClientID() {
        return clientID;
    }
}
