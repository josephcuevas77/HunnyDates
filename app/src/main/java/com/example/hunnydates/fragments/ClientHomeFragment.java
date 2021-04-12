package com.example.hunnydates.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hunnydates.R;
import com.example.hunnydates.utils.CurrentUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientHomeFragment extends Fragment {

    private ImageView profileImage;
    private TextView profileName;
    private Button editProfileButton;
    private Button logoutButton;
    private GoogleSignInClient mGoogleSignInClient;
    private NavController navController;

    public ClientHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClientHomeScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientHomeFragment newInstance() {
        ClientHomeFragment fragment = new ClientHomeFragment();
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
        View view = inflater.inflate(R.layout.client_home, container, false);

        initializeComponents(view);
        initializeGoogleSignIn();
        displayCurrentUserInfo();

        editProfileButton.setOnClickListener(editProfileButtonListener);
        logoutButton.setOnClickListener(logoutButtonListener);

        performDatabaseActions();

        return view;
    }

    private void initializeComponents(View view) {
        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.client_nav_host_fragment);
        navController = navHostFragment.getNavController();
        profileImage = view.findViewById(R.id.ch_profile_icon);
        profileName = view.findViewById(R.id.ch_profile_name);
        editProfileButton = view.findViewById(R.id.ch_edit_profile_button);
        logoutButton = view.findViewById(R.id.ch_logout_button);
    }

    private void initializeGoogleSignIn() {
        GoogleSignInOptions googleSIO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), googleSIO);
    }

    private void displayCurrentUserInfo() {
        // Picasso is a library for importing images with a PhotoURL
        Picasso.get().load(CurrentUser.getInstance().getPhotoURL().toString()).into(profileImage);
        if(CurrentUser.getInstance().getFamilyName() != null)
            profileName.setText(CurrentUser.getInstance().getGivenName() + " " + CurrentUser.getInstance().getFamilyName());
        else
            profileName.setText(CurrentUser.getInstance().getGivenName());
    }

    private View.OnClickListener editProfileButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navController.navigate(R.id.action_cb_nav_home_to_editClientFragment);
        }
    };

    private View.OnClickListener logoutButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseAuth.getInstance().signOut();
//                mGoogleSignInClient.revokeAccess();
            CurrentUser.getInstance().clearCurrentUserInfo();
            mGoogleSignInClient.signOut();
            getActivity().finish();
        }
    };

    private void performDatabaseActions() {
//        adminDatabase = FirebaseFirestore.getInstance();
//        CollectionReference adminDB = adminDatabase.collection("Admin");
//
//        Map<String, Object> data1 = new HashMap<>();
//        data1.put("email", CurrentUser.getInstance().getEmail());
//        data1.put("id", "999999");
//        data1.put("userName", "Hunny123");
//        adminDB.document("Admin").set(data1, SetOptions.merge());
//
//        DocumentReference docRef = adminDatabase.collection("Admin").document("Admin");
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "Document data: " + document.getData());
//                        adminEmail.setText("Email:" + document.getString("email"));
//                        adminID.setText("ID:" + document.getString("id"));
//                        adminUN.setText("User Name" + document.getString("userName"));
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
    }
}