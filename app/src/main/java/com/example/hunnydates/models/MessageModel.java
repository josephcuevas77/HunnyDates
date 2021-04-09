package com.example.hunnydates.models;


import com.google.firebase.Timestamp;

public class MessageModel {

    private String message;
    private String recipient;
    private String sender;
    private Timestamp timestamp;

    private MessageModel() {}

    private MessageModel(String message, String recipient, String sender, Timestamp timestamp) {
        this.message = message;
        this.recipient = recipient;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
