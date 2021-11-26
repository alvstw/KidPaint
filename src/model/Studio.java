package model;

import service.server.ClientManager;

import java.io.Serializable;

public class Studio implements Serializable {
    String name = "";
    PaintBoard paintBoard;
    ClientManager clientManager;

    public Studio(String name) {
        this.name = name;
        paintBoard = new PaintBoard();
        clientManager = new ClientManager();
    }

    public String getName() {
        return name;
    }

    public PaintBoard getPaintBoard() {
        return paintBoard;
    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public ClientManager getClientManager() {
        return clientManager;
    }
}
