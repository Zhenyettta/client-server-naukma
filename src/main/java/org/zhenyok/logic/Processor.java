//package org.zhenyok.logic;
//
//import org.zhenyok.database.DatabaseHandler;
//import org.zhenyok.pojo.Group;
//import org.zhenyok.pojo.Message;
//import org.zhenyok.pojo.Product;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
//public class Processor {
//
//    private static final Lock productLock = new ReentrantLock();
//    private static final DatabaseHandler handler = new DatabaseHandler();
//
//
//    public static void process(Message message) {
//        int command = message.getCommand();
//        switch (command) {
//            case 0 -> createProduct(message);
//            case 1 -> getProductCount(message);
//            case 2 -> removeProductCount(message);
//            case 3 -> addProductCount(message);
//            case 4 -> createGroup(message);
//            case 5 -> addProductToGroup(message);
//            case 6 -> changeProductPrice(message);
//            case 7 -> removeProduct(message);
//            case 8 -> getProductPrice(message);
//            case 9 -> getGroup(message);
//            case 10 -> sort(message);
//        }
//    }
//
//    private static void sort(Message message) {
//        productLock.lock();
//        String sortingCriteria = new String(message.getMessageBytes());
//        try{
//            message.setMessageBytes((handler.sort(sortingCriteria)).getBytes());
//        }
//        finally {
//            productLock.unlock();
//        }
//    }
//
//    private static void getGroup(Message message) {
//        productLock.lock();
//        String productName = new String(message.getMessageBytes());
//        try {
//            if (handler.checkProductByName(productName)){
//                String groupName = handler.getGroupByProdName(productName);
//                if(groupName!=null)
//                    message.setMessageBytes(("Group of " + productName + " is " + groupName).getBytes());
//                else message.setMessageBytes((productName + " does not have group").getBytes());
//            }
//            else
//                message.setMessageBytes((productName + " was not found").getBytes());
//        } finally {
//            productLock.unlock();
//        }
//    }
//
//
//    private static void createProduct(Message message) {
//        productLock.lock();
//        String productName = new String(message.getMessageBytes());
//        try {
//            if (!handler.checkProductByName(productName)) {
//                Product product = new Product(productName, message.getCount(), message.getPrice(), null);
//                handler.createProduct(product);
//                message.setMessageBytes(("Product " + productName + " created successfully").getBytes());
//            } else {
//                message.setMessageBytes(("Product " + productName + " already exists").getBytes());
//            }
//        } finally {
//            productLock.unlock();
//        }
//    }
//
//    private static void getProductCount(Message message) {
//        productLock.lock();
//        try {
//            String productName = new String(message.getMessageBytes());
//            if (handler.checkProductByName(productName)) {
//                int count = handler.getProductCount(productName);
//                message.setMessageBytes(("Count of " + productName + " = " + count).getBytes());
//            } else {
//                message.setMessageBytes((productName + " was not found").getBytes());
//            }
//        } finally {
//            productLock.unlock();
//        }
//    }
//
////    private static void removeProductCount(Message message) {
////        productLock.lock();
////        try {
////            String productName = new String(message.getMessageBytes());
////
////            if (handler.checkProductByName(productName)) {
////                Product product = handler.getProduct(productName);
////                int countToRemove = message.getCount();
////                int currentCount = product.getCount();
////                int newCount = Math.max(currentCount - countToRemove, 0);
////                int removedCount = currentCount - newCount;
////                handler.setCount(productName, newCount);
////                message.setMessageBytes(("Removed " + removedCount + " products, actual count = " + newCount).getBytes());
////            } else {
////                message.setMessageBytes((productName + " was not found").getBytes());
////            }
////        } finally {
////            productLock.unlock();
////        }
////    }
//
//    private static void addProductCount(Message message) {
//        productLock.lock();
//        try {
//            String productName = new String(message.getMessageBytes());
//
//            if (handler.checkProductByName(productName)) {
//                Product product = handler.getProduct(productName);
//                handler.setCount(productName, product.getCount() + message.getCount());
//                message.setMessageBytes(("Added " + message.getCount() + " products, actual count = " + handler.getProductCount(productName)).getBytes());
//            } else {
//                message.setMessageBytes((productName + " was not found").getBytes());
//            }
//        } finally {
//            productLock.unlock();
//        }
//    }
//
//    private static void createGroup(Message message) {
//        productLock.lock();
//        try {
//            String groupName = new String(message.getMessageBytes());
//            if (!handler.checkGroupByName(groupName)) {
//                Group group = new Group(groupName);
//                handler.createGroup(group);
//                message.setMessageBytes(("Group " + groupName + " was created").getBytes());
//            } else {
//                message.setMessageBytes(("Group " + groupName + " already exists").getBytes());
//            }
//        } finally {
//            productLock.unlock();
//        }
//    }
//
//    private static void addProductToGroup(Message message) {
//        String[] data = new String(message.getMessageBytes()).split(" ");
//        String productName = data[0];
//        String groupName = data[1];
//
//        productLock.lock();
//        try {
//            if (isValidProductAndGroup(productName, groupName)) {
//                Product product = handler.getProduct(productName);
//                Group group = handler.getGroup(groupName);
//
//                if (product.getGroup() == null) {
//                    synchronized (group) {
//                        handler.setGroup(productName, groupName);
//                        product.setGroup(group);
//                    }
//                    message.setMessageBytes(("Product " + productName + " was successfully added to " + groupName).getBytes());
//                } else {
//                    message.setMessageBytes("Product already belongs to a group".getBytes());
//                }
//            } else {
//                message.setMessageBytes("Product or group was not found".getBytes());
//            }
//        } finally {
//            productLock.unlock();
//        }
//    }
//
//
//    private static boolean isValidProductAndGroup(String productName, String groupName) {
//        return handler.checkGroupByName(groupName) && handler.checkProductByName(productName);
//    }
//
//
//    private static void changeProductPrice(Message message) {
//        productLock.lock();
//        try {
//            String productName = new String(message.getMessageBytes());
//
//            if (handler.checkProductByName(productName)) {
//                Product product = handler.getProduct(productName);
//                synchronized (product) {
//                    handler.setPrice(productName, message.getPrice());
//                }
//
//                message.setMessageBytes(("Price of " + new String(message.getMessageBytes()) + " was changed to " + message.getPrice()).getBytes());
//            } else {
//                message.setMessageBytes((productName + " was not found").getBytes());
//            }
//        } finally {
//            productLock.unlock();
//        }
//    }
//
//    private static void removeProduct(Message message) {
//        productLock.lock();
//        try {
//            String productName = new String(message.getMessageBytes());
//            if (handler.checkProductByName(productName)) {
//                Product product = handler.getProduct(productName);
//                synchronized (product) {
//                    handler.removeProduct(productName);
//                }
//                message.setMessageBytes((productName + " was deleted").getBytes());
//            } else {
//                message.setMessageBytes((productName + " was not found").getBytes());
//            }
//        } finally {
//            productLock.unlock();
//        }
//    }
//
//    private static void getProductPrice(Message message) {
//        productLock.lock();
//        try {
//            String productName = new String(message.getMessageBytes());
//            if (handler.checkProductByName(productName)) {
//                int count = handler.getProductPrice(productName);
//                message.setMessageBytes(("Price of " + productName + " = " + count).getBytes());
//            } else {
//                message.setMessageBytes((productName + " was not found").getBytes());
//            }
//        } finally {
//            productLock.unlock();
//        }
//    }
//
//
//    public static void processMessagesInParallel(Message message) {
//        try (ExecutorService service = Executors.newSingleThreadExecutor()) {
//            service.execute(() -> process(message));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
