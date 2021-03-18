package com.example.hunnydates.models;

public class DatePlanModel {

    private String title;
    private String description;

    private DatePlanModel() {}

    private DatePlanModel(String title, String description) {
        this.title = title;
        this.description = description;
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
}
