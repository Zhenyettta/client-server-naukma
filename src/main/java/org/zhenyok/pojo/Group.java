package org.zhenyok.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Group {
    private final String name;
    private final List<Product> products;
    public static List<Group> groups = Collections.synchronizedList(new ArrayList<>());

    public Group(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    @Override
    public String toString() {
         return name;
    }
}
