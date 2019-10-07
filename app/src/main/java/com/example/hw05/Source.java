package com.example.hw05;

import java.io.Serializable;

public class Source implements Serializable {
    String id, name;

    public Source(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Source() {
    }

    @Override
    public String toString() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
