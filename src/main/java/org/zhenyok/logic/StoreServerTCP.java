package org.zhenyok.logic;

import org.zhenyok.pojo.Message;
import org.zhenyok.pojo.Package;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class StoreServerTCP {

    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        StoreServerTCP server = new StoreServerTCP();
        server.start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread clientThread = new ClientHandlerThread(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandlerThread extends Thread {
        private Socket clientSocket;

        public ClientHandlerThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                 DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {

                while (true) {
                    int arr = input.readInt();
                    byte[] pckg = input.readNBytes(arr);
                    try {
                        Message message = Decryptor.decode(pckg);
                        Processor.processMessagesInParallel(message);
                        Package responsePackage = Package.createPackage(ByteBuffer.wrap(Encryptor.encode(message)));

                        synchronized (output) {
                            output.writeInt(responsePackage.packageBytes().length);
                            output.write(responsePackage.packageBytes());
                            output.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                System.out.println(clientSocket.getPort() + " closed connection");
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
