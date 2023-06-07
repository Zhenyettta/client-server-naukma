package org.zhenyok.logic;

import org.zhenyok.crypto.CRC16;
import org.zhenyok.crypto.MyCipher;
import org.zhenyok.pojo.Message;
import org.zhenyok.pojo.Package;

import java.nio.ByteBuffer;

public class Decryptor {

    public static Message decode(Package pack) {
        try {
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
            byte[] decryptedData = new byte[messageLength - 8];

            buffer.get(8, decryptedMessage);

            int count = buffer.getInt(8 + decryptedMessage.length);
            double price = buffer.getDouble(12 + decryptedMessage.length);

            buffer.get(20 + decryptedMessage.length, decryptedData);
            byte[] resMessage = MyCipher.decrypt(decryptedMessage);
            byte[] resData = MyCipher.decrypt(decryptedData);

            return new Message(cType, bUserId, resMessage, count, price, resData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}