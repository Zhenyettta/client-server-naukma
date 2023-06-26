//package org.zhenyok.server;
//
//import org.zhenyok.pojo.Message;
//import org.zhenyok.pojo.Package;
//import org.zhenyok.logic.Decryptor;
//import org.zhenyok.logic.Encryptor;
//
//
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.nio.ByteBuffer;
//
//public class StoreServerUDP {
//
//    private static final int SERVER_PORT = 5001;
//
//    public static void main(String[] args) {
//        StoreServerUDP server = new StoreServerUDP();
//        server.start();
//    }
//
//    public void start() {
//        try (DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT)) {
//            byte[] receiveBuffer = new byte[1024];
//
//            while (true) {
//                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
//                serverSocket.receive(receivePacket);
//
//                InetAddress clientAddress = receivePacket.getAddress();
//                int clientPort = receivePacket.getPort();
//                byte[] clientData = receivePacket.getData();
//
//                ClientHandlerThread clientThread = new ClientHandlerThread(serverSocket, clientAddress, clientPort, clientData);
//                clientThread.start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static class ClientHandlerThread extends Thread {
//        private final DatagramSocket serverSocket;
//        private final InetAddress clientAddress;
//        private final int clientPort;
//        private final byte[] clientData;
//
//        public ClientHandlerThread(DatagramSocket serverSocket, InetAddress clientAddress, int clientPort, byte[] clientData) {
//            this.serverSocket = serverSocket;
//            this.clientAddress = clientAddress;
//            this.clientPort = clientPort;
//            this.clientData = clientData;
//        }
//
//        @Override
//        public void run() {
//            try {
//                Message message = Decryptor.decode(clientData);
//                Processor.processMessagesInParallel(message);
//                Package responsePackage = Package.createPackage(ByteBuffer.wrap(Encryptor.encode(message)));
//                byte[] responseData = responsePackage.packageBytes();
//
//                DatagramPacket sendPacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);
//                serverSocket.send(sendPacket);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
