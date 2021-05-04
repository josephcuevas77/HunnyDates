package com.example.hunnydates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hunnydates.R;
import com.example.hunnydates.utils.CurrentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ViewUserProfile extends Fragment {

    private ImageView userProfilePic;
    private TextView username;
    private TextView displayName;
    private TextView description;
    private TextView age;
    private DocumentReference userDocument;

    private Button blockButton;

    private String clientId;
    public ViewUserProfile() {
    }

    public static ViewUserProfile newInstance(String param1, String param2) {
        ViewUserProfile fragment = new ViewUserProfile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            clientId = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_user_profile, container, false);
        initializeComponents(view);
        getUserDocument();
        return view;
    }

    private void initializeComponents(View view) {
        userProfilePic = view.findViewById(R.id.vup_profile_icon);
        username = view.findViewById(R.id.vup_username);
        displayName = view.findViewById(R.id.vup_display_name);
        description = view.findViewById(R.id.vup_description);
        age = view.findViewById(R.id.vup_age);

        blockButton = view.findViewById(R.id.vup_block_button);

        blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean test = CurrentUser.getInstance().isUserBlocked(clientId);
                Toast.makeText(getContext(), "isUserBlocked " + test, Toast.LENGTH_SHORT).show();
                //blockUser();
            }
        });
    }

    private void blockUser() {
        Map<String, Object> data = new HashMap<>();

        data.put("id", clientId);

        CollectionReference collectionReferenceBlockedUsers = CurrentUser.getInstance().getBlockedUsersCollections();
        collectionReferenceBlockedUsers.document(clientId).set(data);

        Toast.makeText(getContext(), "Blocked User", Toast.LENGTH_SHORT).show();
    }

    private void getUserDocument() {
        userDocument = FirebaseFirestore.getInstance().collection("clients").document(clientId);
        userDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    if (userDocument.exists()) {
                        String profileURL = userDocument.getString("profile-url");
                        String _username = userDocument.getString("username");
                        String _displayName = userDocument.getString("display-name");
                        String _age = userDocument.getString("age");
                        String _description = userDocument.getString("description");

                        Picasso.get().load(profileURL).into(userProfilePic);

                        username.setText(_username);
                        displayName.setText(_displayName);
                        age.setText(_age);
                        description.setText(_description);
                    }
                }
            }
        });
    }
}