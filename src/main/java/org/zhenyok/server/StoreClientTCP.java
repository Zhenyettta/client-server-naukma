package org.zhenyok.server;

import org.zhenyok.logic.Decryptor;
import org.zhenyok.logic.Encryptor;
import org.zhenyok.pojo.Message;
import org.zhenyok.pojo.Package;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class StoreClientTCP {
    private static final int RECONNECT_INTERVAL = 5000;
    private final String serverAddress;
    private final int serverPort;
    private DataOutputStream out;
    private DataInputStream in;

    public StoreClientTCP(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) throws InterruptedException {
        StoreClientTCP client = new StoreClientTCP("localhost", 5000);
        Thread clientThread = new Thread(client::run);
        clientThread.start();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    Input command from the list:
                    0: Create product
                    1: Get product count
                    2: Remove product count
                    3: Add product count
                    4: Create group
                    5: Add product to group
                    6: Change product price
                    7: Remove product
                    8: Get Product price
                    9: Get Group
                    10: Sort by selected""");

            int command = scanner.nextInt();

            switch (command) {
                case 0 -> {
                    System.out.println("Input product name");
                    String name = scanner.next();
                    System.out.println("Input product count");
                    int count = scanner.nextInt();
                    System.out.println("Input product price");
                    double price = scanner.nextDouble();
                    Message message = new Message(1, 1, name.getBytes(), 0, count, price);
                    client.sendMessageAndWait(message);
                }
                case 1 -> {
                    System.out.println("Input product name");
                    String name = scanner.next();
                    Message message = new Message(1, 1, name.getBytes(), 1, 0, 0);
                    client.sendMessageAndWait(message);
                }
                case 2 -> {
                    System.out.println("Input product name");
                    String name = scanner.next();
                    System.out.println("Input count to remove");
                    int count = scanner.nextInt();
                    Message message = new Message(2, 1, name.getBytes(), 2, count, 0);
                    client.sendMessageAndWait(message);
                }
                case 3 -> {
                    System.out.println("Input product name");
                    String name = scanner.next();
                    System.out.println("Input count to add");
                    int count = scanner.nextInt();
                    Message message = new Message(3, 1, name.getBytes(), 3, count, 0);
                    client.sendMessageAndWait(message);
                }
                case 4 -> {
                    System.out.println("Input group name");
                    String groupName = scanner.next();
                    Message message = new Message(4, 1, groupName.getBytes(), 4, 0, 0);
                    client.sendMessageAndWait(message);
                }
                case 5 -> {
                    System.out.println("Input group name");
                    String groupName = scanner.next();
                    System.out.println("Input product name");
                    String productName = scanner.next();
                    String mixed = productName + " " + groupName;
                    Message message = new Message(5, 1, mixed.getBytes(), 5, 0, 0);
                    client.sendMessageAndWait(message);
                }
                case 6 -> {
                    System.out.println("Input product name");
                    String name = scanner.next();
                    System.out.println("Input new price");
                    double price = scanner.nextDouble();
                    Message message = new Message(6, 1, name.getBytes(), 6, 0, price);
                    client.sendMessageAndWait(message);
                }
                case 7 -> {
                    System.out.println("Input product name");
                    String name = scanner.next();
                    Message message = new Message(7, 1, name.getBytes(), 7, 0, 0);
                    client.sendMessageAndWait(message);
                }
                case 8 -> {
                    System.out.println("Input product name");
                    String name = scanner.next();
                    Message message = new Message(8, 1, name.getBytes(), 8, 0, 0);
                    client.sendMessageAndWait(message);
                }
                case 9 -> {
                    System.out.println("Input product name");
                    String name = scanner.next();
                    Message message = new Message(9, 1, name.getBytes(), 9, 0,0);
                    client.sendMessageAndWait(message);
                }
                case 10 ->{
                    System.out.println("Input sorting criteria (name, count, price)");
                    String name = scanner.next().toLowerCase();
                    if (!name.equals("name") && !name.equals("count") && !name.equals("price")) {
                        System.out.println("Invalid criteria");
                        break;
                    }
                    Message message = new Message(10, 1, name.getBytes(), 10, 0,0);
                    client.sendMessageAndWait(message);
                }
                default -> System.out.println("Invalid command.");
            }
        }
    }

    public void run() {
        while (true) {
            try {
                Socket socket = new Socket(serverAddress, serverPort);
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                break;
            } catch (Exception e) {
                System.out.println("Server is not available. Retrying in " + RECONNECT_INTERVAL / 1000 + " seconds...");
                try {
                    Thread.sleep(RECONNECT_INTERVAL);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    private void sendMessageAndWait(Message message) {
        try {
            Package pkg = Package.createPackage(ByteBuffer.wrap(Encryptor.encode(message)));


            out.writeInt(pkg.packageBytes().length);
            out.write(pkg.packageBytes());
            out.flush();


            int responseLength = in.readInt();
            byte[] responseBytes = new byte[responseLength];
            in.readFully(responseBytes);


            Message responseMessage = Decryptor.decode(responseBytes);
            System.out.println("Server response: " + new String(responseMessage.getMessageBytes()));
        } catch (Exception e) {
            System.out.println("An error occurred while sending the message: " + e.getMessage());
        }
    }
}
