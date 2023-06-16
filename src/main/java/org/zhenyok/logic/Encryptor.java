package org.zhenyok.logic;

import org.zhenyok.crypto.CRC16;
import org.zhenyok.crypto.MyCipher;
import org.zhenyok.pojo.Message;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Encryptor {
    private static final int NUM_THREADS = 10;

    public static List<byte[]> encode(List<Message> messages) {
        List<byte[]> encodedPackages = new ArrayList<>();

        try (ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS)) {
            for (Message message : messages) {
                executorService.execute(() -> {
                    byte[] encodedMessage = encodePackage(message);
                    encodedPackages.add(encodedMessage);
                });
            }
        }

        return encodedPackages;
    }
    public static byte[] encode(Message message) {
        List<byte[]> encodedPackages = new ArrayList<>();

        try (ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS)) {

                executorService.execute(() -> {
                    byte[] encodedMessage = encodePackage(message);
                    encodedPackages.add(encodedMessage);
                });

        }

        return encodedPackages.get(0);
    }



    private static byte[] encodePackage(Message message) {
        try {
            byte[] encodedText = MyCipher.encrypt(message.getMessageBytes());

            int textLength = encodedText.length;


            byte[] bytes = ByteBuffer.allocate(18)
                    .put((byte) 0x13)
                    .put((byte) 1)
                    .putLong(10)
                    .putInt(textLength)
                    .array();

            ByteBuffer encodedMessageBuffer = ByteBuffer.allocate(24 + textLength)
                    .putInt(message.getCType())
                    .putInt(message.getBUserId())
                    .put(encodedText)
                    .putInt(message.getCommand())
                    .putInt(message.getCount())
                    .putDouble(message.getPrice());

            byte[] encodedMessage = encodedMessageBuffer.array();

            return ByteBuffer.allocate(bytes.length + encodedMessage.length + 4)
                    .put(bytes)
                    .putShort(CRC16.crcEncode(bytes))
                    .put(encodedMessage)
                    .putShort(CRC16.crcEncode(encodedMessage))
                    .array();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
