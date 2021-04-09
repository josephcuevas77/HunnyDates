package com.example.hunnydates.models;


import com.google.firebase.Timestamp;

public class MessageModel {

    private String message;
    private Timestamp timestamp;

    private MessageModel() {}

    private MessageModel(String message, Timestamp timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}
