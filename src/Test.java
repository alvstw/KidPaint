import model.constant.MessageType;
import model.message.Message;
import service.client.ClientMessageService;
import service.client.ClientService;

import java.io.IOException;

public class Test {
    private static ClientService clientService;

    public static void main(String[] args) throws IOException, InterruptedException {
        testSendChatMessage();
    }

    public static void testUDP() throws IOException, InterruptedException {
        clientService = new ClientService();
        while(true){
            // Establish connection
            try {
                clientService.findServer();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Thread.sleep(1000);
        }
    }

    public static void testSendChatMessage() throws IOException, InterruptedException {
        ClientService clientService = new ClientService();
        ClientMessageService clientMessageService = new ClientMessageService(clientService.getSocket());
//        clientService.setUsername("Paul");
        clientService.findServer();
//        clientMessageService.sendChatMessage("test");
    }

    public static void testMessageConvert() {
        Message m1 = new Message();
        m1.type = MessageType.SET_USERNAME.toString();
        m1.message = "dsads";
        byte[] encoded = m1.encode();

        Message m2 = new Message();
        m2.decode(encoded);
        System.out.println(m2.type + m2.message + m2.createdAt);
    }
}
