package com.example.hunnydates.models;

import java.sql.Timestamp;

public class MessageModel {

    private String message;
    private String receipient;
    private String sender;
    private Timestamp timestamp;

    private MessageModel() {}

    private MessageModel(String message, String recipient, String sender, Timestamp timestamp) {
        this.message = message;
        this.receipient = recipient;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceipient() {
        return receipient;
    }

    public void setReceipient(String receipient) {
        this.receipient = receipient;
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
