package org.zhenyok;

import org.zhenyok.pojo.Message;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;

public class Sender {
    private static final byte[] keyBytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private static final String algorithm = "AES";
    private static final SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);

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
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encodedText = cipher.doFinal(message.messageBytes());
            return ByteBuffer.allocate(8 + encodedText.length)
                    .putInt(message.cType())
                    .putInt(message.bUserId())
                    .put(encodedText)
                    .array();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
