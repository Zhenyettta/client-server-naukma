package org.zhenyok.logic;

import org.zhenyok.pojo.Group;
import org.zhenyok.pojo.Message;
import org.zhenyok.pojo.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Processor {
    private static final int NUM_THREADS = 10;
    private static final Lock productLock = new ReentrantLock();

    public static void process(Message message) {
        int command = message.getCommand();
        switch (command) {
            case 0:
                createProduct(message);
                break;

            case 1:
                getProductCount(message);
                break;

            case 2:
                removeProduct(message);
                break;

            case 3:
                addProduct(message);
                break;

            case 4:
                createGroup(message);
                break;

            case 5:
                addProductToGroup(message);
                break;

            case 6:
                changeProductPrice(message);
                break;
        }
    }

    private static void createProduct(Message message) {
        productLock.lock();
        System.out.println("CREATE");
        Product product = new Product(new String(message.getDataBytes()), message.getCount(), message.getPrice(), null);
        Product.products.add(product);
        message.setMessageBytes("Product created successfully".getBytes());
        productLock.unlock();
    }

    private static void getProductCount(Message message) {
        productLock.lock();
        System.out.println("GET");
        Product product = getProductByName(new String(message.getDataBytes()));

        if (product != null) {
            int count = product.getCount();
            message.setMessageBytes(("Count of your product = " + count).getBytes());
            productLock.unlock();
        } else {
            message.setMessageBytes("Product not found".getBytes());
            productLock.unlock();
        }
    }

    private static void removeProduct(Message message) {
        productLock.lock();
        System.out.println("REMOVE");
        Product product = getProductByName(new String(message.getDataBytes()));

        if (product != null) {

            int countToRemove = message.getCount();
            int currentCount = product.getCount();
            int newCount = Math.max(currentCount - countToRemove, 0);
            int removedCount = currentCount - newCount;
            product.setCount(newCount);
            message.setMessageBytes(("Removed " + removedCount + " products, actual count = " + newCount).getBytes());
            productLock.unlock();

        } else {
            message.setMessageBytes("Product not found".getBytes());
            productLock.unlock();
        }
    }

    private static void addProduct(Message message) {
        productLock.lock();
        System.out.println("ADD");
        Product product = getProductByName(new String(message.getDataBytes()));

        if (product != null) {

            product.setCount(product.getCount() + message.getCount());
            message.setMessageBytes(("Added " + message.getCount() + " products, actual count = " + product.getCount()).getBytes());

            productLock.unlock();

        } else {
            message.setMessageBytes("Product not found".getBytes());
            productLock.unlock();
        }
    }

    private static void createGroup(Message message) {
        productLock.lock();
        Group group = new Group(new String(message.getDataBytes()), Collections.synchronizedList(new ArrayList<>()));
        Group.groups.add(group);
        message.setMessageBytes(("Group " + new String(message.getDataBytes()) + " was created").getBytes());
        productLock.unlock();
    }

    private static void addProductToGroup(Message message) {
        productLock.lock();
        String[] data = new String(message.getDataBytes()).split(" ");
        String productName = data[0];
        String groupName = data[1];

        Product product = getProductByName(productName);
        Group group = getGroupByName(groupName);

        if (product != null && group != null) {
            synchronized (group) {
                group.addProduct(product);
                product.setGroup(group);
            }

            message.setMessageBytes(("Product " + productName + " was successfully added to " + groupName).getBytes());
            productLock.unlock();
        } else {
            message.setMessageBytes("Product or group not found".getBytes());
            productLock.unlock();
        }
    }

    private static void changeProductPrice(Message message) {
        productLock.lock();
        Product product = getProductByName(new String(message.getDataBytes()));

        if (product != null) {
            synchronized (product) {
                product.setPrice(message.getPrice());
            }

            message.setMessageBytes(("Price of product " + new String(message.getDataBytes()) + " was changed to " + product.getPrice()).getBytes());
            productLock.unlock();
        } else {
            message.setMessageBytes("Product not found".getBytes());
            productLock.unlock();
        }
    }

    private static Product getProductByName(String name) {
        return Product.products.stream()
                .filter(i -> i.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private static Group getGroupByName(String name) {
        return Group.groups.stream()
                .filter(i -> i.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static void processMessagesInParallel(List<Message> messages) {
        try (ExecutorService executorService = Executors.newCachedThreadPool()) {
            for (Message message : messages) {
                executorService.execute(() -> process(message));
            }
        }
    }
}
