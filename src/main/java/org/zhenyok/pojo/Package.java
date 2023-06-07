package org.zhenyok.pojo;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public record Package(byte bSrc, long bPktId, int wLen, short wCrc16, Message message, short wCrc16End,
                      byte[] packageBytes) {
    private static final byte B_MAGIC = 0x13;

    public static Package createPackage(ByteBuffer bytes) throws Exception {
        byte firstByte = bytes.get(0);
        if (firstByte != B_MAGIC) {
            throw new Exception("Not correct package");
        }
        byte bSrc = bytes.get(1);
        long bPktId = bytes.getLong(2);
        int wLen = bytes.getInt(10);
        short wCrc16 = bytes.getShort(14);
        short wCrc16End = bytes.getShort(16 + wLen);
        byte[] packageBytes = bytes.array();
        Message message = new Message(bytes, wLen);

        return new Package(bSrc, bPktId, wLen, wCrc16, message, wCrc16End, packageBytes);
    }
}