package com.example.hunnydates.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hunnydates.R;
import com.example.hunnydates.utils.CurrentUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private int RC_SIGN_IN = 1;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.hunny_dates_login, container, false);
        signInButton = view.findViewById(R.id.googleSignInButton);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSIO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), googleSIO);

        database = FirebaseFirestore.getInstance();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInButton.setEnabled(false);
                signIn();
            }
        });

        if(GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext()) != null) {
            signInButton.setEnabled(false);
            signIn();
        }
        return view;
    }
    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e) {
            Toast.makeText(getActivity(), "Sign In Unsuccessful", Toast.LENGTH_LONG).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct){
        if(acct != null){
            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
//                        Toast.makeText(LoginScreen.this, "Successful", Toast.LENGTH_LONG).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        loginToHunnyDates(user);
                    }
                    else{
//                        Toast.makeText(LoginScreen.this, "Unsuccessful", Toast.LENGTH_LONG).show();
                        loginToHunnyDates(null);
                    }
                }
            });
        }
        else{
            Toast.makeText(getActivity(), "Failed to access account.", Toast.LENGTH_LONG).show();
            signInButton.setEnabled(true);
        }
    }

    private void loginToHunnyDates(FirebaseUser fUser){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());
        if(fUser != null){
            CurrentUser.getInstance().setDisplayName(account.getDisplayName());
            CurrentUser.getInstance().setGivenName(account.getGivenName());
            CurrentUser.getInstance().setFamilyName(account.getFamilyName());
            CurrentUser.getInstance().setEmail(account.getEmail());
            CurrentUser.getInstance().setAccountID(account.getId());
            CurrentUser.getInstance().setPhotoURL(account.getPhotoUrl());

            databaseOperations();
        }
    }

    private void databaseOperations() {
        String documentID = CurrentUser.getInstance().getEmail().toLowerCase();
        DocumentReference documentReferenceAdmin = database.collection("admins").document(documentID);
        documentReferenceAdmin.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_loginScreen_to_adminActivity);
                        CurrentUser.getInstance().setDocument(database.collection("admins").document(documentID));
                        signInButton.setEnabled(true);
                    } else {
                        DocumentReference documentReferenceClient = database.collection("clients").document(documentID);
                        documentReferenceClient.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_loginScreen_to_clientActivity);
                                        CurrentUser.getInstance().setDocument(database.collection("clients").document(documentID));
                                        signInButton.setEnabled(true);
                                    } else {
                                        Map<String, Object> clientData = new HashMap<>();
                                        clientData.put("email", CurrentUser.getInstance().getEmail());
                                        clientData.put("username", CurrentUser.getInstance().getDisplayName());
                                        clientData.put("display-name", CurrentUser.getInstance().getGivenName() + " " + CurrentUser.getInstance().getFamilyName());
                                        database.collection("clients").document(documentID).set(clientData);
                                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_loginScreen_to_clientActivity);
                                        CurrentUser.getInstance().setDocument(database.collection("clients").document(documentID));
                                        signInButton.setEnabled(true);
                                    }
                                } else {
                                }
                            }
                        });
                    }
                } else {
                    signInButton.setEnabled(true);
                }
            }
        });
    }
}