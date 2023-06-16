package org.zhenyok.pojo;

import java.nio.ByteBuffer;

public class Message {
    private final int cType;
    private final int bUserId;
    private final int command;
    private final int count;
    private final double price;

    private byte[] messageBytes;

    public Message(int cType, int bUserId, byte[] messageBytes, int command, int count, double price) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.messageBytes = messageBytes;
        this.command = command;
        this.count = count;
        this.price = price;

    }

    public Message(ByteBuffer byteBuffer, int textLength, int dataLength) {
        this(byteBuffer.getInt(20), byteBuffer.getInt(24), new byte[textLength], byteBuffer.getInt(24 + textLength), byteBuffer.getInt(28 + textLength), byteBuffer.getDouble(32 + textLength));
        byteBuffer.get(messageBytes, 0, textLength);

    }

    public int getCType() {
        return cType;
    }

    public int getBUserId() {
        return bUserId;
    }

    public byte[] getMessageBytes() {
        return messageBytes;
    }

    public synchronized void setMessageBytes(byte[] messageBytes) {
        this.messageBytes = messageBytes;
    }

    public int getCommand() {
        return command;
    }

    public int getCount() {
        return count;
    }

    public double getPrice() {
        return price;
    }



    @Override
    public String toString() {
        return "Message{" +
                "cType=" + cType +
                ", bUserId=" + bUserId +
                ", messageBytes=" + new String(messageBytes) +
                ", command=" + command +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}
