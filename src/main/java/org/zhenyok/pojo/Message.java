package org.zhenyok.pojo;

import java.nio.ByteBuffer;
import java.util.Arrays;

public record Message(int cType, int bUserId, byte[] messageBytes) {
    public Message(ByteBuffer byteBuffer, int wLen) {
        this(byteBuffer.getInt(16), byteBuffer.getInt(20), new byte[wLen - Integer.BYTES * 2]);
        byteBuffer.get(messageBytes, 0, wLen - Integer.BYTES * 2);
    }

    @Override
    public String toString() {
        return "Message{" +
                "cType=" + cType +
                ", bUserId=" + bUserId +
                ", messageBytes=" + Arrays.toString(messageBytes) +
                '}';
    }
}
