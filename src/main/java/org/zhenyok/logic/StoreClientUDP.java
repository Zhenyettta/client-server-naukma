package org.zhenyok.logic;

import org.zhenyok.pojo.Message;
import org.zhenyok.pojo.Package;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class StoreClientUDP extends Thread {
    private static final int RECONNECT_INTERVAL = 5000;
    private final String serverAddress;
    private final int serverPort;

    public StoreClientUDP(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) {
        StoreClientUDP clientThread = new StoreClientUDP("localhost", 5001);
        clientThread.start();
    }

    @Override
    public void run() {
//        while (true) {
            try (DatagramSocket socket = new DatagramSocket()) {
                socket.setSoTimeout(5000);
                InetAddress serverAddress = InetAddress.getByName(this.serverAddress);
                Message[] messages = getMessagesToSend();

                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket;

                for (Message message : messages) {
                    Package package1 = Package.createPackage(ByteBuffer.wrap(Encryptor.encode(message)));
                    byte[] requestData = package1.packageBytes();

                    DatagramPacket sendPacket = new DatagramPacket(requestData, requestData.length, serverAddress, serverPort);
                    socket.send(sendPacket);

                    receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    socket.receive(receivePacket);

                    byte[] responseData = receivePacket.getData();
                    Message receivedMessage = Decryptor.decode(responseData);

                    System.out.println(new String(receivedMessage.getMessageBytes()));

                }

            } catch (Exception e) {
                System.out.println("Retrying");
                StoreClientUDP clientThread = new StoreClientUDP("localhost", 5001);
                clientThread.start();
            }
//        }
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

    public void sendMessages(Message[] messages, DatagramSocket socket, InetAddress serverAddress) throws Exception {
        for (Message message : messages) {
            Package package1 = Package.createPackage(ByteBuffer.wrap(Encryptor.encode(message)));
            byte[] requestData = package1.packageBytes();

            DatagramPacket sendPacket = new DatagramPacket(requestData, requestData.length, serverAddress, serverPort);
            socket.send(sendPacket);

            Thread.sleep(100);
        }
    }
}
