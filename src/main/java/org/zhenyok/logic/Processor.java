package org.zhenyok.logic;

import org.zhenyok.pojo.Group;
import org.zhenyok.pojo.Message;
import org.zhenyok.pojo.Product;

import java.util.ArrayList;
import java.util.Collections;

public class Processor {
    public static void process(Message message) {
        int command = message.getCommand();
        switch (command) {
            // Create product
            case 0 -> {
                Product product = new Product(new String(message.getDataBytes()), message.getCount(), message.getPrice(), null);
                Product.products.add(product);
                message.setMessageBytes("Product created successfully".getBytes());
            }

            case 1 -> {
                Product product = Product.products.stream()
                        .filter(i -> i.getName().equals(new String(message.getDataBytes())))
                        .findFirst()
                        .orElse(null);

                if (product != null) {
                    int count = product.getCount();
                    message.setMessageBytes(("Count of your product = " + count).getBytes());
                } else {
                    message.setMessageBytes("Product not found".getBytes());
                }
            }

            case 2 -> {
                Product product = Product.products.stream()
                        .filter(i -> i.getName().equals(new String(message.getDataBytes())))
                        .findFirst()
                        .orElse(null);

                product.setCount(product.getCount() - message.getCount());
                if (product.getCount() < 0) product.setCount(0);

                message.setMessageBytes(("Removed " + message.getCount() + " products, actual count = " + product.getCount()).getBytes());
            }

            case 3 -> {
                Product product = Product.products.stream()
                        .filter(i -> i.getName().equals(new String(message.getDataBytes())))
                        .findFirst()
                        .orElse(null);

                product.setCount(product.getCount() + message.getCount());

                message.setMessageBytes(("Added " + message.getCount() + " products, actual count = " + product.getCount()).getBytes());
            }

            case 4 -> {
                Group group = new Group(new String(message.getDataBytes()), Collections.synchronizedList(new ArrayList<>()));
                Group.groups.add(group);
                message.setMessageBytes(("Group " + new String(message.getDataBytes()) + " was created").getBytes());
            }

            case 5 -> {
                String productName = new String(message.getDataBytes()).split(" ")[0];

                Product product = Product.products.stream()
                        .filter(i -> i.getName().equals(productName))
                        .findFirst()
                        .orElse(null);


                String groupName = new String(message.getDataBytes()).split(" ")[1];

                Group group = Group.groups.stream()
                        .filter(i -> i.getName().equals(groupName))
                        .findFirst()
                        .orElse(null);

                group.addProduct(product);
                product.setGroup(group);

                System.out.println(group.getProducts() + " aboba");

                message.setMessageBytes(("Product " + productName + " was successfully added to " + groupName).getBytes());


            }

            case 6 -> {
                Product product = Product.products.stream()
                        .filter(i -> i.getName().equals(new String(message.getDataBytes())))
                        .findFirst()
                        .orElse(null);
                product.setPrice(message.getPrice());

                message.setMessageBytes(("Price of product " + new String(message.getDataBytes()) + " was changed to " + message.getPrice()).getBytes());
            }
        }
    }
}
