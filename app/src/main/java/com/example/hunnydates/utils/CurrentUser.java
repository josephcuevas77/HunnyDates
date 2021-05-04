package com.example.hunnydates.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.example.hunnydates.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Current User: Singleton Class
 * Only one instance of Current User is required.
 */

public final class CurrentUser {
    private static CurrentUser currentUser = new CurrentUser();

    private Map<String, String> profileInfoMap = new HashMap<>();

    private String displayName = null;
    private String givenName = null;
    private String familyName = null;
    private String age = null;
    private String email = null;
    private Uri photoURL = null;
    private DocumentReference document = null;
    private List<String> blockedUsers = new ArrayList<>();

    private CurrentUser() {}

    public static CurrentUser getInstance() {
        return currentUser;
    }

    public Map<String, String> getProfileInfoMap() { return profileInfoMap; }

    public List<String> getBlockedUsers() {
        return blockedUsers;
    }

    public void setBlockedUsers(List<String> blockedUsers) {
        this.blockedUsers = blockedUsers;
    }

    public String getUsername() {
        return profileInfoMap.get("username");
    }

    public String getDisplayName() {
        return profileInfoMap.get("display-name");
    }

    public String getAge() { return profileInfoMap.get("age"); }

    public String getEmail() {
        return profileInfoMap.get("email");
    }

    public String getPhotoURL() {
        return profileInfoMap.get("profile-url");
    }

    public String getDescription() { return profileInfoMap.get("description"); }

    public void setDocument(DocumentReference document) {
        this.document = document;
    }

    public DocumentReference getDocument() {
        return document;
    }

    public void queryProfileInfo() {
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    if (userDocument.exists()) {
                        String username = userDocument.getString("username");
                        String displayName = userDocument.getString("display-name");
                        String email = userDocument.getString("email");
                        String age = userDocument.getString("age");
                        String profileURL = userDocument.getString("profile-url");
                        String description = userDocument.getString("description");

                        profileInfoMap.put("username", username);
                        profileInfoMap.put("display-name", displayName);
                        profileInfoMap.put("email", email);
                        profileInfoMap.put("age", age);
                        profileInfoMap.put("profile-url", profileURL);
                        profileInfoMap.put("description", description);
                    }
                }
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        blockedUsers.clear();
        CollectionReference blocks = document.collection("blocked-users");
        blocks.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        blockedUsers.add(document.getId());
                    }
                }
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clearCurrentUserInfo() {
        displayName = null;
        givenName = null;
        familyName = null;
        email = null;
        photoURL = null;
        document = null;
    }

    public CollectionReference getDatePlansCollections() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        return database.collection("date-plans");
    }

    public CollectionReference getBlockedUsersCollections() {
        return document.collection("blocked-users");
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
