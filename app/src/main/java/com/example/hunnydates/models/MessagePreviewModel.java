package com.example.hunnydates.models;


import com.google.firebase.Timestamp;

public class MessagePreviewModel {

    private String clientName;

    private MessagePreviewModel() {}

    private MessagePreviewModel(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
