package org.zhenyok.logic;

import org.zhenyok.pojo.Message;

@FunctionalInterface
public interface Receiver {
    void receiveMessage(Message message);
}
