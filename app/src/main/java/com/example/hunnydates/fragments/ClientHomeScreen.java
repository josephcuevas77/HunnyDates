package com.example.hunnydates.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientHomeScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientHomeScreen extends Fragment {

    private ImageView profileImage;
    private TextView profileName;
    private Button editProfileButton;
    private Button logoutButton;
    private GoogleSignInClient mGoogleSignInClient;

    private BottomNavigationView bottomNavigationView;

    public ClientHomeScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClientHomeScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientHomeScreen newInstance() {
        ClientHomeScreen fragment = new ClientHomeScreen();
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
        View view = inflater.inflate(R.layout.client_home_screen, container, false);

        profileImage = view.findViewById(R.id.ch_profile_icon);
        profileName = view.findViewById(R.id.ch_profile_name);
        editProfileButton = view.findViewById(R.id.ch_edit_profile_button);
        logoutButton = view.findViewById(R.id.ch_logout_button);

        Picasso.get().load(CurrentUser.getInstance().getPhotoURL()).into(profileImage);
        profileName.setText(CurrentUser.getInstance().getGivenName() + " " + CurrentUser.getInstance().getFamilyName());

        GoogleSignInOptions googleSIO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), googleSIO);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Editing Profile Info", Toast.LENGTH_LONG).show();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
//                mGoogleSignInClient.revokeAccess();
                signOut();

                Navigation.findNavController(getView()).navigate(R.id.loginScreen);
            }
        });

        bottomNavigationView = view.findViewById(R.id.ch_bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
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

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // By using switch we can easily get
            // the selected fragment
            // by using there id.
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.cb_nav_home:
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.loginScreen);
                    break;
                case R.id.cb_nav_matches:
                    break;
                case R.id.cb_nav_search:
                    break;
                case R.id.cb_nav_create:
                    break;
                case R.id.cb_nav_list:
                    break;
            }
            return true;
        }
    };
}