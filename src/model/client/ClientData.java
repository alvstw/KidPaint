package model.client;

import model.Studio;
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

    public static ArrayList<String> connectedUsers = new ArrayList<>();

    public static String selectedStudio;
    public static ArrayList<String> studios;
}
