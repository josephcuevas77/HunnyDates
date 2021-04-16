package com.example.hunnydates.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.hunnydates.R;
import com.google.firebase.firestore.DocumentReference;

/**
 * Current User: Singleton Class
 * Only one instance of Current User is required.
 */

public final class CurrentUser {
    private static CurrentUser currentUser = new CurrentUser();

    private String displayName = null;
    private String givenName = null;
    private String familyName = null;
    private String email = null;
    private String accountID = null;
    private Uri photoURL = null;
    private DocumentReference document = null;

    private CurrentUser() {}

    public static CurrentUser getInstance() {
        return currentUser;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public Uri getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(Uri photoURL) {
        this.photoURL = photoURL;
    }

    public void setDocument(DocumentReference document) {
        this.document = document;
    }

    public DocumentReference getDocument() {
        return document;
    }

    public void clearCurrentUserInfo() {
        displayName = null;
        givenName = null;
        familyName = null;
        email = null;
        accountID = null;
        photoURL = null;
        document = null;
    }

    public void callNotification(Activity activity, String title, String msg, String CHANNEL_ID){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(R.mipmap.applogo_foreground)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setContentIntent(PendingIntent.getActivity(activity, 0, new Intent(), 0));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
// notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }

}
