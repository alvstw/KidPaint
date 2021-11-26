package model.client;

import model.UserProfile;
import service.client.ClientMessageService;
import service.client.ClientService;
import window.UI;

import java.util.ArrayList;

public class ClientData {
    public static String id;
    public static String username;

    public static ClientService clientService;
    public static ClientMessageService clientMessageService;
    public static UI ui;

    public static ArrayList<UserProfile> connectedUsers = new ArrayList<>();
}
