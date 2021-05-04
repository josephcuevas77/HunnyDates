package com.example.hunnydates.models;

public class DatePlanPublicModel {

    private String title;
    private String description;
    private String location;
    private String user;
    private String id;
    private String user_profile_image_url;

    private String image_url;
    private int rating_count;

    private DatePlanPublicModel() {}

    public DatePlanPublicModel(String user, String title, String description, String location, int ratingsCount, String id, String user_profile_image_url, String url) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.location = location;
        this.user_profile_image_url = user_profile_image_url;
        this.rating_count = ratingsCount;
        this.id = id;
        this.image_url = url;
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

    public int getRating_count() { return rating_count; }

    public void setRating_count(int rating_count) { this.rating_count = rating_count; }

    public String getUser_profile_image_url() {
        return user_profile_image_url;
    }

    public void setUser_profile_image_url(String user_profile_image_url) {
        this.user_profile_image_url = user_profile_image_url;
    }

    public String getImage_url() { return image_url; }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}