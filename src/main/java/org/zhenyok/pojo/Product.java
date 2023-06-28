package org.zhenyok.pojo;

import java.io.Serializable;

public class Product implements Serializable {

    private final String name;
    private volatile int count;
    private volatile double price;
    private volatile String groupName;
    private volatile String supplier;
    private volatile String characteristics;
    private volatile int id;


    public Product(int id, String name, int count, double price, String group, String supplier, String characteristics) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.groupName = group;
        this.id = id != 0 ? id : -1;
        this.supplier = supplier;
        this.characteristics = characteristics;
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

    public String getGroup() {
        return groupName;
    }

    public void setGroup(String group) {
        this.groupName = group;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", price=" + price +
                ", group=" + groupName +
                ", supplier=" + supplier +
                ", char=" + characteristics +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }
}