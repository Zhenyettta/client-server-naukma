package org.zhenyok.pojo;

import java.nio.ByteBuffer;

public record Message(int cType, int bUserId, byte[] messageBytes, int count, double price, byte[] dataBytes) {
    public Message(ByteBuffer byteBuffer, int wLen) {
        this(byteBuffer.getInt(16), byteBuffer.getInt(20), new byte[wLen - Integer.BYTES * 2], byteBuffer.getInt(wLen - Integer.BYTES * 2), byteBuffer.getDouble(wLen - Integer.BYTES * 2 + 4), new byte[wLen - Integer.BYTES * 2 + 8]);
        byteBuffer.get(messageBytes, 0, wLen - Integer.BYTES * 2);
        byteBuffer.get(dataBytes, 0, wLen - Integer.BYTES * 2 + 8);
    }

}