package org.zhenyok.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Group {
    public static List<Group> groups = Collections.synchronizedList(new ArrayList<>());
    private final String name;
    private final List<Product> products;

    public Group(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        synchronized (products) {
            return new ArrayList<>(products);
        }
    }

    public void addProduct(Product product) {
        synchronized (products) {
            products.add(product);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}