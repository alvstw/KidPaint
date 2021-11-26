package model.server;

import model.PaintBoard;
import service.StudioManager;
import service.server.ClientManager;

public class ServerData {
    public static final StudioManager studioManager = new StudioManager();
    public static final ClientManager globalClientManger = new ClientManager();
}
