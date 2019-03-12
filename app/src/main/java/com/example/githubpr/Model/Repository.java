package com.example.githubpr.Model;

import android.graphics.Color;

import java.io.Serializable;
import java.util.Random;

public class Repository implements Serializable {
    int stargazers_count;
    int forks_count;
    String name;
    String language;
    int color;

    public int getForks_count() {
        return forks_count;
    }

    public void setForks_count(int forks_count) {
        this.forks_count = forks_count;
    }

    public int getStargazers_count() {
        return stargazers_count;
    }

    public void setStargazers_count(int stargazers_count) {
        this.stargazers_count = stargazers_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getColor() {
        if(this.language!=null && this.color==0) {
            Random rnd = new Random();
            this.color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        }
        return this.color;
    }

    private void setColor() {
        Random rnd = new Random();
        this.color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

    }

    @Override
    public String toString() {
        return "Repository{" +
                "stargazers_count=" + stargazers_count +
                ", forks_count=" + forks_count +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                ", color=" + color +
                '}';
    }
}
