package lection2.tictak;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Worker extends Thread {
    private final int id;
    private final Data data;
    private final ReentrantLock lock;
    private final Condition condition;

    public Worker(int id, Data d, ReentrantLock lock, Condition condition) {
        this.id = id;
        data = d;
        this.lock = lock;
        this.condition = condition;
        this.start();

    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            lock.lock();
            try {
                while (id != data.getState()) {
                    condition.await();
                }
                if (id == 2) {
                    data.Toy();
                } else if (id == 3) {
                    data.Tic();
                } else {
                    data.Tak();
                }
                condition.signalAll();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }
}