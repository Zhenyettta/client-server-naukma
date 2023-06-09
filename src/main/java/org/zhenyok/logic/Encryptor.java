package org.zhenyok.logic;

import org.zhenyok.crypto.CRC16;
import org.zhenyok.crypto.MyCipher;
import org.zhenyok.pojo.Message;


import java.nio.ByteBuffer;

public class Encryptor {
    static int dataLength;
    static int textLength;

    public static byte[] encode(Message command) {
        try {
            byte[] message = encodeMessage(command);
            byte[] bytes = ByteBuffer.allocate(18)
                    .put((byte) 0x13)
                    .put((byte) 1)
                    .putLong(10)
                    .putInt(textLength)
                    .putInt(dataLength)
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
            byte[] encodedText = MyCipher.encrypt(message.getMessageBytes());
            byte[] encodedData = MyCipher.encrypt(message.getDataBytes());
            textLength = encodedText.length;
            dataLength = encodedData.length;
            System.out.println(message);
            return ByteBuffer.allocate(24 + encodedText.length + encodedData.length)
                    .putInt(message.getCType())
                    .putInt(message.getBUserId())
                    .put(encodedText)
                    .putInt(message.getCommand())
                    .putInt(message.getCount())
                    .putDouble(message.getPrice())
                    .put(encodedData)
                    .array();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}