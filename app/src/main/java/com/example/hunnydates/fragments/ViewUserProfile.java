package com.example.hunnydates.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.hunnydates.R;
import com.example.hunnydates.utils.CurrentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ViewUserProfile extends Fragment {

    private ImageView userProfilePic;
    private TextView username;
    private TextView displayName;
    private TextView description;
    private TextView age;
    private DocumentReference userDocument;
    private Button messageUser;
    private Button blockUserButton;

    private String clientId;
    private String profileImageURL;
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
        getUserDocument();
        initializeComponents(view);
        return view;
    }

    private void initializeComponents(View view) {
        userProfilePic = view.findViewById(R.id.vup_profile_icon);
        username = view.findViewById(R.id.vup_username);
        displayName = view.findViewById(R.id.vup_display_name);
        description = view.findViewById(R.id.vup_description);
        age = view.findViewById(R.id.vup_age);
        messageUser = view.findViewById(R.id.vup_message_btn);
        blockUserButton = view.findViewById(R.id.vup_block_btn);

        messageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("recipient", clientId);
                bundle.putString("photo-url", profileImageURL);
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_viewUserProfile_to_messageFragment, bundle);
            }
        });

        List<String> blockUsers = CurrentUser.getInstance().getBlockedUsers();
        if (blockUsers.contains(clientId)) {
            blockUserButton.setText("Unblock");
        }

        blockUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockUser();
            }
        });

    }

    private void blockUser() {
        List<String> blockUsers = CurrentUser.getInstance().getBlockedUsers();
        if (blockUsers.contains(clientId)) {
            blockUserButton.setText("Unblock");

            CurrentUser.getInstance().getDocument().collection("blocked-users").document(clientId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Document successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });

            FirebaseFirestore.getInstance().collection("clients")
                    .document(clientId).collection("blocked-users")
                    .document(CurrentUser.getInstance().getEmail())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Document successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });;

            blockUserButton.setText("Block");
            CurrentUser.getInstance().queryProfileInfo();
        }
        else {
            Map<String, Object> data = new HashMap<>();

            data.put("id", clientId);
            data.put("didBlock", true);

            CollectionReference collectionReferenceBlockedUsers = CurrentUser.getInstance().getBlockedUsersCollections();
            collectionReferenceBlockedUsers.document(clientId).set(data);


            DocumentReference blockedUserReference = FirebaseFirestore.getInstance().collection("clients")
                    .document(clientId).collection("blocked-users").document(CurrentUser.getInstance().getEmail());
            data.put("id", CurrentUser.getInstance().getEmail());
            data.put("didBlock", false);

            blockedUserReference.set(data);

            Toast.makeText(getContext(), "Blocked User", Toast.LENGTH_SHORT).show();

            blockUserButton.setText("Unblock");
            CurrentUser.getInstance().queryProfileInfo();
        }
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
                        profileImageURL = profileURL;
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