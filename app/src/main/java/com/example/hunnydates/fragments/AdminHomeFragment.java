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

import com.example.hunnydates.R;
import com.example.hunnydates.utils.CurrentUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHomeFragment extends Fragment {

    private static final String TAG = "";
    private ImageView profileImage;
    private TextView profileName;
    private Button logoutButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseFirestore db;
    private TextView adminEmail;
    private TextView adminID;
    private TextView adminUN;


    public AdminHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClientHomeScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminHomeFragment newInstance() {
        AdminHomeFragment fragment = new AdminHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_home, container, false);

        profileImage = view.findViewById(R.id.ah_profile_icon);
        profileName = view.findViewById(R.id.ah_profile_name);
        logoutButton = view.findViewById(R.id.ah_logout_button);

        Picasso.get().load(CurrentUser.getInstance().getPhotoURL()).into(profileImage);
        profileName.setText(CurrentUser.getInstance().getGivenName() + " " + CurrentUser.getInstance().getFamilyName());

        GoogleSignInOptions googleSIO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), googleSIO);

        db = FirebaseFirestore.getInstance();
        CollectionReference adminDB = db.collection("Admin");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("email", CurrentUser.getInstance().getEmail());
        data1.put("id", "999999");
        data1.put("userName", "Hunny123");
        adminDB.document("Admin").set(data1, SetOptions.merge());

        adminEmail = view.findViewById(R.id.ah_profile_email);
        adminID = view.findViewById(R.id.ah_profile_id);
        adminUN = view.findViewById(R.id.ah_profile_username);

        DocumentReference docRef = db.collection("Admin").document("Admin");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document data: " + document.getData());
                        adminEmail.setText("Email:" + document.getString("email"));
                        adminID.setText("ID:" + document.getString("id"));
                        adminUN.setText("User Name" + document.getString("userName"));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
//                mGoogleSignInClient.revokeAccess();
                signOut();

                getActivity().finish();
            }
        });
        return view;
    }



    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

}