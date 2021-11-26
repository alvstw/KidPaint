package window;

import model.client.ClientData;

import java.util.ArrayList;

public class WindowManager {
    public static void startPaintBoard() {
        UI ui = ClientData.ui;           // get the instance of painter.UI
        ui.setData(new int[50][50], 20);    // set the data array and block size. comment this statement to use the default data array and block size.
        ui.setVisible(true);
        ClientData.clientService.getClientMessageService().requestPaintBoard();
        ClientData.ui.addChatMessage(String.format("Welcome %s!\nYou have entered studio %s\nYou can chat here with others.\nType /ls to see who else is online.", ClientData.username, ClientData.selectedStudio, ClientData.clientService.getUsername()));
    }

    public static void openStudioPicker() {
        ArrayList<String> studioNameList = new ArrayList<>(ClientData.studios);
        StudioPicker ui = StudioPicker.getInstance(studioNameList);            // get the instance of painter.UI
        ui.setVisible(true);
    }
}
