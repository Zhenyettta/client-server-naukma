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

        try (ExecutorService executorService = Executors.newCachedThreadPool()) {
            for (Message message : messages) {
                executorService.execute(() -> {
                    byte[] encodedMessage = encodePackage(message);
                    encodedPackages.add(encodedMessage);
                });
            }
        }

        return encodedPackages;
    }

    private static byte[] encodePackage(Message message) {
        try {
            byte[] encodedText = MyCipher.encrypt(message.getMessageBytes());
            byte[] encodedData = MyCipher.encrypt(message.getDataBytes());
            int textLength = encodedText.length;
            int dataLength = encodedData.length;

            byte[] bytes = ByteBuffer.allocate(18)
                    .put((byte) 0x13)
                    .put((byte) 1)
                    .putLong(10)
                    .putInt(textLength)
                    .putInt(dataLength)
                    .array();

            ByteBuffer encodedMessageBuffer = ByteBuffer.allocate(24 + textLength + dataLength)
                    .putInt(message.getCType())
                    .putInt(message.getBUserId())
                    .put(encodedText)
                    .putInt(message.getCommand())
                    .putInt(message.getCount())
                    .putDouble(message.getPrice())
                    .put(encodedData);

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
