package lection2.tictak;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        ReentrantLock rel = new ReentrantLock();
        Condition condition = rel.newCondition();
        Data d = new Data();
assd

        new Worker(1, d, rel, condition);
        new Worker(2, d, rel, condition);
        new Worker(3, d, rel, condition);


    }
}
