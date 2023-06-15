package org.zhenyok.logic;

import org.zhenyok.crypto.CRC16;
import org.zhenyok.crypto.MyCipher;
import org.zhenyok.pojo.Message;
import org.zhenyok.pojo.Package;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Decryptor {
    private static final int NUM_THREADS = 10;

    public static List<Message> decode(List<Package> packages) {
        List<Message> decodedMessages = Collections.synchronizedList(new ArrayList<>());

        try (ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS)) {
            for (Package pack : packages) {
                executorService.execute(() -> {
                    Message message = decode(pack.packageBytes());
                    if (message != null) {
                        decodedMessages.add(message);
                    }
                });
            }
        }

        return decodedMessages;
    }

    public static Message decode(byte[] pack) {
        try {
            ByteBuffer wrap = ByteBuffer.wrap(pack);
            byte clientId = wrap.get(1);
            long packetId = wrap.getLong(2);
            int textLength = wrap.getInt(10);
            int dataLength = wrap.getInt(14);
            short firstCrc16 = wrap.getShort(18);


            byte[] bytes = ByteBuffer.allocate(18)
                    .put((byte) 0x13)
                    .put(clientId)
                    .putLong(packetId)
                    .putInt(textLength)
                    .putInt(dataLength)
                    .array();

            if (CRC16.crcEncode(bytes) != firstCrc16) {
                throw new Exception("CRC16 error");
            }


            int cType = wrap.getInt(20);
            int bUserId = wrap.getInt(24);
            byte[] decryptedMessage = new byte[textLength];
            byte[] decryptedData = new byte[dataLength];

            wrap.get(28, decryptedMessage);

            int command = wrap.getInt(28 + textLength);
            int count = wrap.getInt(32 + textLength);
            double price = wrap.getDouble(36 + textLength);

            wrap.get(44 + textLength, decryptedData);

            byte[] resMessage = MyCipher.decrypt(decryptedMessage);
            byte[] resData = MyCipher.decrypt(decryptedData);

            return new Message(cType, bUserId, resMessage, command, count, price, resData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
