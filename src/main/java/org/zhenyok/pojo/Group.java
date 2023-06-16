package org.zhenyok.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Group {
    public static List<Group> groups = Collections.synchronizedList(new ArrayList<>());
    private final String name;


    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name;
    }
}