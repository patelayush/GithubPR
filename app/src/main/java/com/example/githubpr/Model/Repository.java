package com.example.githubpr.Model;

import java.io.Serializable;

public class Repository implements Serializable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

}
