package com.example.githubpr.Model;

import java.io.Serializable;

public class PullRequest implements Serializable {
    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
