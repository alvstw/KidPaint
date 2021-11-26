import helper.TCPHelper;
import model.Studio;
import model.client.ClientData;
import model.constant.MessageType;
import model.message.Message;
import service.client.ClientMessageService;
import service.client.ClientService;
import window.StudioPicker;
import window.UI;
import window.WindowManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class KidPaint extends JFrame implements ActionListener {
    private static ClientService clientService;
    JTextField usernameField;

    public static void main(String[] args) throws IOException {
        clientService = new ClientService();
        ClientData.clientService = clientService;

        new KidPaint();
    }

    public KidPaint() {
        ClientData.username = "";
		this.setupClient();
    }

    public void setupClient() {
        // Set up a window(title, size, exit to close, resizability)
        this.setSize(new Dimension(200, 100));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // Create a container and panel
        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 0));
        container.add(panel, BorderLayout.CENTER);

        // JLabel
        JLabel label = new JLabel();
        label.setText("Please input you name:");
        container.add(label, BorderLayout.NORTH);

        // Text Field
        this.usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(500, 30));
        container.add(this.usernameField, BorderLayout.CENTER);

        // Button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        container.add(submitButton, BorderLayout.SOUTH);

        this.setVisible(true);
        System.out.println(this.usernameField.getText());
    }



	// painter.UI action listener
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("Submit")) {
            // set the text of the label to the text of the field
            clientService.setUsername(this.usernameField.getText());

            /* to-do: Fix the bug where user aren't allowed to start the painter after retry */
            if (clientService.getUsername().equals("")){
            	this.setupClient();
            	return;
			}
            // Close window
            JComponent component = (JComponent) e.getSource();
            Window window = SwingUtilities.getWindowAncestor(component);
            window.dispose();

            // Establish connection
            try {
                clientService.findServer();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Get studio list
            ClientData.clientMessageService.sendMessage(MessageType.REQUEST_STUDIO_LIST.toString());

            // Wait studio list
            while (ClientData.studios == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            ClientData.ui = UI.getInstance();

            // Open paint board
            WindowManager.openStudioPicker();
        }
    }

}
