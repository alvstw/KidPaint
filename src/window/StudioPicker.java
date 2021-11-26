package window;

import model.client.ClientData;
import model.constant.MessageType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

public class StudioPicker extends JFrame {
    private static StudioPicker instance;
    private ArrayList<String> wordlist;

    public static StudioPicker getInstance(ArrayList<String> studioList) {
        if (instance == null)
            instance = new StudioPicker(studioList);
        return instance;
    }

    private StudioPicker(ArrayList<String> studioList) {
        this.wordlist = studioList;

        setTitle(String.format("KidPaint - %s", ClientData.username));
        setSize(new Dimension(500, 250));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout (new BorderLayout());
        setResizable(false);

        JPanel basePanel = new JPanel();
        getContentPane().add(basePanel, BorderLayout.CENTER);
        basePanel.setLayout(new BorderLayout(0, 0));
        container.add(basePanel, BorderLayout.CENTER);

        JList<String> displayList = new JList<>(studioList.toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(displayList);
        basePanel.add(scrollPane, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        container.add(buttonPanel, BorderLayout.SOUTH);

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JTextField studioNameTextField = new JTextField();
        studioNameTextField.setPreferredSize(new Dimension (500, 30));
        buttonPanel.add(studioNameTextField, BorderLayout.SOUTH);

        JToggleButton tglConfirm = new JToggleButton("Confirm");
        tglConfirm.setSelected(true);
        buttonPanel.add(tglConfirm);

        pack();
        setVisible(true);

        // clear the PaintBoard
        tglConfirm.addActionListener(arg0 -> {
            String selectedStudio;
            selectedStudio = studioNameTextField.getText();
            if (selectedStudio.equals("")) {
                selectedStudio = displayList.getSelectedValue();
            }

            if (selectedStudio == null) {
                showMessageDialog(null, "Please select or enter a studio name.");
                return;
            }

            ClientData.selectedStudio = selectedStudio;
            System.out.printf("Selected studio: %s\n", selectedStudio);
            ClientData.clientMessageService.sendMessage(MessageType.SELECT_STUDIO.toString(), selectedStudio);

            // Close window
            this.dispose();

            // Open PaintBoard
            WindowManager.startPaintBoard();
        });

    }
}