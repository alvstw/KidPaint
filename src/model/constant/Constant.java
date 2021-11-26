package model.constant;

public class Constant {
    public static final int serverUDPPort = 5000;
    public static final int serverTCPPort = 5001;
    public static final String broadcastAddress = "255.255.255.255";

    public static final String serverID = "28c6332d-8604-4c9d-9202-69a002c9b55b";
    public static final String serverName = "Server";

    public static final int blockSize = 16;

    public static final String consoleSeparator = new String(new char[10]).replace("\0", "-");
    public static final String fileSeparator = System.getProperty("file.separator");

    public static final String defaultStudioName = "Default Studio";
}
