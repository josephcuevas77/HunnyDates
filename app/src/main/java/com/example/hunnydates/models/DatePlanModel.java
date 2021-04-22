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

    private DatePlanModel() {}

    private DatePlanModel(String title, String description, String location) {
        this.title = title;
        this.description = description;
        this.location = location;
    }

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

}
