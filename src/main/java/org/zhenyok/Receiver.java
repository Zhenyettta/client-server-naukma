package org.zhenyok;

import org.zhenyok.pojo.Message;
import org.zhenyok.pojo.Package;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;

public class Receiver {
    private static final byte[] keyBytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private static final String algorithm = "AES";
    private static final SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);

    public static Message decode(Package pack) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);

            ByteBuffer wrap = ByteBuffer.wrap(pack.packageBytes());
            byte clientId = wrap.get(1);
            long packetId = wrap.getLong(2);
            int messageLength = wrap.getInt(10);
            short firstCrc16 = wrap.getShort(14);

            byte[] bytes = ByteBuffer.allocate(14)
                    .put((byte) 0x13)
                    .put(clientId)
                    .putLong(packetId)
                    .putInt(messageLength)
                    .array();

            if (CRC16.crcEncode(bytes) != firstCrc16) {
                throw new Exception("CRC16 error");
            }

            byte[] messageBuffer = new byte[messageLength];
            System.arraycopy(pack.packageBytes(), 16, messageBuffer, 0, messageLength);

            if (CRC16.crcEncode(messageBuffer) != wrap.getShort(16 + messageLength)) {
                throw new Exception("CRC16 error");
            }

            ByteBuffer buffer = ByteBuffer.wrap(messageBuffer);
            int cType = buffer.getInt(0);
            int bUserId = buffer.getInt(4);
            byte[] decryptedMessage = new byte[messageLength - 8];
            buffer.get(8, decryptedMessage);
            byte[] resMessage = cipher.doFinal(decryptedMessage);

            return new Message(cType, bUserId, resMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
