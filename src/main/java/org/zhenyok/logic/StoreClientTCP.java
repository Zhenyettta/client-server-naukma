package org.zhenyok.logic;

import org.zhenyok.pojo.Message;
import org.zhenyok.pojo.Package;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class StoreClientTCP extends Thread {
    private static final int RECONNECT_INTERVAL = 5000;
    private final String serverAddress;
    private final int serverPort;
    private DataOutputStream out;
    private DataInputStream in;

    public StoreClientTCP(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) {
        StoreClientTCP clientThread = new StoreClientTCP("localhost", 5000);
        clientThread.start();
    }

    @Override
    public void run() {
        while (true) {
            try (Socket socket = new Socket(serverAddress, serverPort)) {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                Message[] messages = getMessagesToSend();

                sendMessages(messages, out);

                while (true) {
                    int len = in.readInt();
                    byte[] messageBytes = in.readNBytes(len);

                    try {
                        Message receivedMessage = Decryptor.decode(messageBytes);
                        System.out.println(new String(receivedMessage.getMessageBytes()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            } catch (Exception e) {
                System.out.println("Client Error: " + e.getMessage());
                System.out.println("Reconnecting in " + RECONNECT_INTERVAL + " milliseconds...");
                try {
                    Thread.sleep(RECONNECT_INTERVAL);
                } catch (InterruptedException ex) {
                    System.out.println("Reconnection interrupted");
                    break;
                }
            }
        }
    }

    public void sendMessages(Message[] messages, DataOutputStream out) throws Exception {
        for (Message message : messages) {
            Package package1 = Package.createPackage(ByteBuffer.wrap(Encryptor.encode(message)));
            out.writeInt(package1.packageBytes().length);
            out.write(package1.packageBytes());
        }

        out.flush();
    }

    public Message[] getMessagesToSend() {
        Message[] messages = {
                new Message(1, 1, "boba".getBytes(), 0, 10, 0.0, "Product1".getBytes()),
                new Message(1, 1, "boba".getBytes(), 1, 10, 0.0, "Product1".getBytes()),
                new Message(1, 1, "boba".getBytes(), 2, 3, 0.0, "Product1".getBytes()),
                new Message(1, 1, "boba".getBytes(), 0, 3, 0.0, "Product3".getBytes()),
                new Message(1, 1, "boba".getBytes(), 4, 0, 0.0, "Group2".getBytes()),
                new Message(1, 1, "boba".getBytes(), 5, 0, 0.0, "Product3 Group2".getBytes()),
                new Message(1, 1, "boba".getBytes(), 3, 50, 0, "Product1".getBytes()),
                new Message(1, 1, "boba".getBytes(), 1, 0, 57.0, "Product1".getBytes())
        };
        return messages;
    }
}
