package org.zhenyok.pojo;

import com.sun.source.doctree.SerialDataTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Product implements Serializable {

    private final String name;
    private volatile int count;
    private volatile double price;
    private volatile Group group;

    public Product(String name, int count, double price, Group group) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.group = group;
    }



    public String getName() {
        return name;
    }

    public synchronized int getCount() {
        return count;
    }

    public synchronized void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Group getGroup() {
        return group;
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