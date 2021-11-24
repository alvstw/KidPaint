import model.ClientData;
import window.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;

public class KidPaint extends JFrame implements ActionListener {
    JTextField usernameField;

    public static void main(String[] args) {
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

    public void startPaintBoard() {
		UI ui = UI.getInstance();            // get the instance of painter.UI
		ui.setData(new int[50][50], 20);    // set the data array and block size. comment this statement to use the default data array and block size.
		ui.setVisible(true);
	}

	// painter.UI action listener
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("Submit")) {
            // set the text of the label to the text of the field
            ClientData.username = this.usernameField.getText();

            /* to-do: Fix the bug where user aren't allowed to start the painter after retry */
            if (ClientData.username.equals("")){
            	this.setupClient();
            	return;
			}
            // Close window
            JComponent component = (JComponent) e.getSource();
            Window window = SwingUtilities.getWindowAncestor(component);
            window.dispose();

            // Open paint board
            this.startPaintBoard();
        }
    }

}
