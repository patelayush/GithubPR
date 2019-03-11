package com.example.githubpr.Model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PullRequest implements Serializable {
    String title;
    String state;
    String diff_url;
    String updated_at;
    String updateddate;
    int number;
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDiff_url() {
        return diff_url;
    }

    public void setDiff_url(String diff_url) {
        this.diff_url = diff_url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUpdateddate(){
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        parser.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parsed = null;
        try {
            parsed = parser.parse(this.updated_at);
            java.text.DateFormat format = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
            return "#" + this.number + " opened on " + format.format(parsed) + " by " + this.user.getLogin();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "PullRequest{" +
                "title='" + title + '\'' +
                ", state='" + state + '\'' +
                ", diff_url='" + diff_url + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", updateddate='" + updateddate + '\'' +
                '}';
    }
}
