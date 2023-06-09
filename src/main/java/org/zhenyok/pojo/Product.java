package org.zhenyok.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Product {
    private String name;
    private int count;
    private double price;
    private Group group;

    public static List<Product> products = Collections.synchronizedList(new ArrayList<>());

    public Product(String name, int count, double price, Group group) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public double getPrice() {
        return price;
    }

    public Group getGroup() {
        return group;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public static void setProducts(List<Product> products) {
        Product.products = products;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", price=" + price +
                ", group=" + group +
                '}';
    }
}
