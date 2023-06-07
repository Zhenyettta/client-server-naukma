package org.zhenyok.logic;

import org.zhenyok.crypto.CRC16;
import org.zhenyok.crypto.MyCipher;
import org.zhenyok.pojo.Message;


import java.nio.ByteBuffer;

public class Encryptor {

    public static byte[] encode(Message command) {
        try {
            byte[] message = encodeMessage(command);
            byte[] bytes = ByteBuffer.allocate(14)
                    .put((byte) 0x13)
                    .put((byte) 1)
                    .putLong(10)
                    .putInt(message.length)
                    .array();

            return ByteBuffer.allocate(bytes.length + message.length + 4)
                    .put(bytes)
                    .putShort(CRC16.crcEncode(bytes))
                    .put(message)
                    .putShort(CRC16.crcEncode(message))
                    .array();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] encodeMessage(Message message) {
        try {
            byte[] encodedText = MyCipher.encrypt(message.messageBytes());
            byte[] encodedData = MyCipher.encrypt(message.dataBytes());

            return ByteBuffer.allocate(16 + encodedText.length + encodedData.length)
                    .putInt(message.cType())
                    .putInt(message.bUserId())
                    .put(encodedText)
                    .putInt(message.count())
                    .putDouble(message.price())
                    .put(encodedText)
                    .array();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}