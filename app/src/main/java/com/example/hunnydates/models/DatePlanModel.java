package com.example.hunnydates.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hunnydates.utils.CurrentUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.content.ContentValues.TAG;

public class DatePlanModel {

    private String title;
    private String description;
    private String location;
    private String user;
    private String id;
    private int ratingsCount;

    private DatePlanModel() {}

    public DatePlanModel(String user, String title, String description, String location, int ratingsCount, String id) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.location = location;
        this.ratingsCount = ratingsCount;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) { this.user = user; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public int getRatingsCount() { return ratingsCount; }

    public void setRatingsCount(int ratingsCount) { this.ratingsCount = ratingsCount; }

}
