package helper;

import model.message.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class TCPHelper {
    public static Message receiveMessage(Socket socket) {
        ObjectInputStream in;
        Object receivedObject;
        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (EOFException | SocketException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            if ((receivedObject = in.readObject()) == null) return null;
            Message message = (Message) receivedObject;
            System.out.printf("Receiving message from %s(%s): %s: %s\n", message.senderID, message.senderName, message.type, message.message);
            return message;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendMessage(Socket socket, Message message) {
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            out.writeObject(message);
            System.out.printf("Sending message to %s %s: %s\n", socket.getInetAddress().getHostAddress(), message.type, message.message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
