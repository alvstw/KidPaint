package window;

import model.PaintBoard;
import model.UserProfile;
import model.client.ClientData;
import model.constant.Constant;
import model.constant.MessageType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

enum PaintMode {Pixel, Area}

public class UI extends JFrame {
	private JTextField msgField;
	private JTextArea chatArea;
	private JPanel pnlColorPicker;
	private JPanel paintPanel;
	private JToggleButton tglPen;
	private JToggleButton tglBucket;
	private JToggleButton tglClear;
	private JToggleButton tglImport;
	private JToggleButton tglExport;

	private static UI instance;
	private int selectedColor = -543230; 	// golden
	
	int[][] data = new int[50][50];			// pixel color data array
	int blockSize = Constant.blockSize;
	PaintMode paintMode = PaintMode.Pixel;


	/**
	 * get the instance of painter.UI. Singleton design pattern.
	 */
	public static UI getInstance() {
		if (instance == null)
			instance = new UI();
		
		return instance;
	}
	
	/**
	 * private constructor. To create an instance of painter.UI, call painter.UI.getInstance() instead.
	 */
	private UI() {
		setTitle(String.format("KidPaint - %s", ClientData.username));
		
		JPanel basePanel = new JPanel();
		getContentPane().add(basePanel, BorderLayout.CENTER);
		basePanel.setLayout(new BorderLayout(0, 0));
		
		paintPanel = new JPanel() {
			
			// refresh the paint panel
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				
				Graphics2D g2 = (Graphics2D) g; // Graphics2D provides the setRenderingHints method
				
				// enable anti-aliasing
			    RenderingHints rh = new RenderingHints(
			             RenderingHints.KEY_ANTIALIASING,
			             RenderingHints.VALUE_ANTIALIAS_ON);
			    g2.setRenderingHints(rh);
			    
			    // clear the paint panel using black
				g2.setColor(Color.black);
				g2.fillRect(0, 0, this.getWidth(), this.getHeight());
				
				// draw and fill circles with the specific colors stored in the data array
				for(int x=0; x<data.length; x++) {
					for (int y=0; y<data[0].length; y++) {
						g2.setColor(new Color(data[x][y]));
						g2.fillArc(blockSize * x, blockSize * y, blockSize, blockSize, 0, 360);
						g2.setColor(Color.darkGray);
						g2.drawArc(blockSize * x, blockSize * y, blockSize, blockSize, 0, 360);
					}
				}
			}
		};
		
		paintPanel.addMouseListener(new MouseListener() {
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}

			// handle the mouse-up event of the paint panel
			@Override
			public void mouseReleased(MouseEvent e) {
				if (paintMode == PaintMode.Area && e.getX() >= 0 && e.getY() >= 0)
					paintArea(e.getX()/blockSize, e.getY()/blockSize);
			}
		});
		
		paintPanel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if (paintMode == PaintMode.Pixel && e.getX() >= 0 && e.getY() >= 0)
					paintPixel(e.getX()/blockSize,e.getY()/blockSize);
			}

			@Override public void mouseMoved(MouseEvent e) {}
			
		});
		
		paintPanel.setPreferredSize(new Dimension(data.length * blockSize, data[0].length * blockSize));
		
		JScrollPane scrollPaneLeft = new JScrollPane(paintPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		basePanel.add(scrollPaneLeft, BorderLayout.CENTER);
		
		JPanel toolPanel = new JPanel();
		basePanel.add(toolPanel, BorderLayout.NORTH);
		toolPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		
		pnlColorPicker = new JPanel();
		pnlColorPicker.setPreferredSize(new Dimension(24, 24));
		pnlColorPicker.setBackground(new Color(selectedColor));
		pnlColorPicker.setBorder(new LineBorder(new Color(0, 0, 0)));

		// show the color picker
		pnlColorPicker.addMouseListener(new MouseListener() {
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {
				ColorPicker picker = ColorPicker.getInstance(UI.instance);
				Point location = pnlColorPicker.getLocationOnScreen();
				location.y += pnlColorPicker.getHeight();
				picker.setLocation(location);
				picker.setVisible(true);
			}
			
		});
		
		toolPanel.add(pnlColorPicker);
		
		tglPen = new JToggleButton("Pen");
		tglPen.setSelected(true);
		toolPanel.add(tglPen);
		
		tglBucket = new JToggleButton("Bucket");
		toolPanel.add(tglBucket);

		tglClear = new JToggleButton("Clear");
		toolPanel.add(tglClear);

		tglImport = new JToggleButton("Import");
		toolPanel.add(tglImport);

		tglExport = new JToggleButton("Export");
		toolPanel.add(tglExport);
		
		// change the paint mode to PIXEL mode
		tglPen.addActionListener(arg0 -> {
			tglPen.setSelected(true);
			tglBucket.setSelected(false);
			paintMode = PaintMode.Pixel;
		});
		
		// change the paint mode to AREA mode
		tglBucket.addActionListener(arg0 -> {
			tglPen.setSelected(false);
			tglBucket.setSelected(true);
			paintMode = PaintMode.Area;
		});

		// clear the PaintBoard
		tglClear.addActionListener(arg0 -> {
			clearPaintBoard();
			ClientData.clientMessageService.sendMessage(MessageType.CLEAR_PAINT_BOARD.toString(), "");
		});

		// import the PaintBoard
		tglImport.addActionListener(arg0 -> {
			FileDialog dialog = new FileDialog(new Frame(), "Select File to Open", FileDialog.LOAD);
			dialog.setFilenameFilter((dir, name) -> name.endsWith(".pboard"));
			dialog.setVisible(true);
			System.out.println(dialog.getFile() + " chosen.");
			try {
				FileInputStream fileInputStream = new FileInputStream(dialog.getDirectory() + Constant.fileSeparator + dialog.getFile());
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				PaintBoard paintBoard = (PaintBoard) objectInputStream.readObject();
				clearPaintBoard();
				ClientData.clientMessageService.sendMessage(MessageType.CLEAR_PAINT_BOARD.toString(), "");
				setData(paintBoard.getData(), Constant.blockSize);
				ClientData.clientMessageService.sendPaintBoard(paintBoard);
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("Unable to read PaintBoard file.");
			}
		});

		// export the PaintBoard
		tglExport.addActionListener(arg0 -> {
			FileDialog dialog = new FileDialog(new Frame(), "Select location to save", FileDialog.SAVE);
			dialog.setFilenameFilter((dir, name) -> name.endsWith(".pboard"));
			dialog.setFile("KidPaint-PaintBoard-export.pboard");
			dialog.setVisible(true);
			System.out.println(dialog.getFile() + " chosen.");
			File file = new File(dialog.getDirectory(), dialog.getFile());
			try {
				if (file.createNewFile()) {
					System.out.println("File created: " + file.getName());
				} else {
					System.out.println("File will be overwritten.");
				}
			} catch (IOException e) {
				System.out.println("Exception occurred during file creation.");
			}

			PaintBoard paintBoard = new PaintBoard();
			paintBoard.setData(data);
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath());
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(paintBoard);
				objectOutputStream.close();
				System.out.println("Successfully exported the PaintBoard.");
			} catch (IOException e) {
				System.out.println("Exception occurred while writing the file.");
			}
		});

		JPanel msgPanel = new JPanel();
		
		getContentPane().add(msgPanel, BorderLayout.EAST);
		
		msgPanel.setLayout(new BorderLayout(0, 0));
		
		msgField = new JTextField();	// text field for inputting message
		
		msgPanel.add(msgField, BorderLayout.SOUTH);
		
		// handle key-input event of the message field
		msgField.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent e) {}
			@Override public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {		// if the user press ENTER
					onTextInputted(msgField.getText());
					msgField.setText("");
				}
			}
			
		});
		
		chatArea = new JTextArea();		// the read only text area for showing messages
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		
		JScrollPane scrollPaneRight = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPaneRight.setPreferredSize(new Dimension(300, this.getHeight()));
		msgPanel.add(scrollPaneRight, BorderLayout.CENTER);
		
		this.setSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * it will be invoked if the user selected the specific color through the color picker
	 * @param colorValue - the selected color
	 */
	public void selectColor(int colorValue) {
		SwingUtilities.invokeLater(()->{
			selectedColor = colorValue;
			pnlColorPicker.setBackground(new Color(colorValue));
		});
	}
		 
	/**
	 * it will be invoked if the user inputted text in the message field
	* @param message - message content
	 */
	private void onTextInputted(String message) {
		if (message.equalsIgnoreCase("/ls")) {
			ClientData.clientMessageService.sendMessage(MessageType.REQUEST_CLIENT_LIST.toString());
			return;
		}
		addChatMessage(ClientData.username, message);
		ClientData.clientMessageService.sendChatMessage(message);
	}

	/**
	 * it will be invoked if the user inputted text in the message field
	 * @param senderName - sender name
	 * @param message - message content
	 */
	public void addChatMessage(String senderName, String message) {
		String messageLine = String.format("%s: %s\n", senderName, message);
		chatArea.setText(chatArea.getText() + messageLine);
	}

	/**
	 * it will be invoked if the user inputted text in the message field
	 * @param message - message content
	 */
	public void addChatMessage(String message) {
		String messageLine = String.format("%s\n", message);
		chatArea.setText(chatArea.getText() + messageLine);
	}
	
	/**
	 * change the color of a specific pixel
	 * @param col, row - the position of the selected pixel
	 */
	public void paintPixel(int col, int row) {
		setPixel(col, row, selectedColor);
		ClientData.clientMessageService.sendPaintData(col, row, selectedColor);
	}

	/**
	 * change the color of a specific pixel
	 * @param col, row, color - the position of the selected pixel
	 */
	public void setPixel(int col, int row, int color) {
		if (col >= data.length || row >= data[0].length) return;

		data[col][row] = color;
		paintPanel.repaint(col * blockSize, row * blockSize, blockSize, blockSize);
	}
	
	/**
	 * change the color of a specific area
	 * @param col, row - the position of the selected pixel
	 * @return a list of modified pixels
	 */
	public List paintArea(int col, int row) {
		LinkedList<Point> filledPixels = new LinkedList<>();
		if (col >= data.length || row >= data[0].length) return filledPixels;

		int oriColor = data[col][row];
		LinkedList<Point> buffer = new LinkedList<>();
		
		if (oriColor != selectedColor) {
			buffer.add(new Point(col, row));
			
			while(!buffer.isEmpty()) {
				Point p = buffer.removeFirst();
				int x = p.x;
				int y = p.y;
				
				if (data[x][y] != oriColor) continue;
				
				data[x][y] = selectedColor;
				filledPixels.add(p);
				ClientData.clientMessageService.sendPaintData(x, y, selectedColor);
	
				if (x > 0 && data[x-1][y] == oriColor) buffer.add(new Point(x-1, y));
				if (x < data.length - 1 && data[x+1][y] == oriColor) buffer.add(new Point(x+1, y));
				if (y > 0 && data[x][y-1] == oriColor) buffer.add(new Point(x, y-1));
				if (y < data[0].length - 1 && data[x][y+1] == oriColor) buffer.add(new Point(x, y+1));
			}
			paintPanel.repaint();
		}
		return filledPixels;
	}
	
	/**
	 * set pixel data and block size
	 */
	public void setData(int[][] data, int blockSize) {
		this.data = data;
		this.blockSize = blockSize;
		paintPanel.setPreferredSize(new Dimension(data.length * blockSize, data[0].length * blockSize));
		paintPanel.repaint();
	}

	public void printOnlineUsers() {
		int userCount = 0;
		StringBuilder stringBuilder = new StringBuilder();
		for (UserProfile user : ClientData.connectedUsers) {
			if (userCount != 0) stringBuilder.append(" ,");
			stringBuilder.append(user.username);
			userCount++;
		}
		String userListString = stringBuilder.toString();
		if (userCount == 1) {
			addChatMessage("No one else online yet");
			return;
		}
		addChatMessage(String.format("%d online users: %s", userCount, userListString));
	}

	public void clearPaintBoard() {
		setData(new int[50][50], Constant.blockSize);
	}
}
