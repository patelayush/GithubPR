package com.example.githubpr.Model;

import java.io.Serializable;

public class User implements Serializable {
    String login;
    Long id;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
