package com.example.hunnydates.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginScreen extends Fragment {
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN = 1;

    public LoginScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginScreen newInstance(String param1, String param2) {
        LoginScreen fragment = new LoginScreen();
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
        View view = inflater.inflate(R.layout.login_screen, container, false);
        signInButton = view.findViewById(R.id.googleSignInButton);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSIO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), googleSIO);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        if(GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext()) != null) {
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
        catch (ApiException e){
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
        }
    }

    private void loginToHunnyDates(FirebaseUser fUser){
//        Set to true to skip the login process.
//        Otherwise, set to false.
        boolean debugging = false;

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());
        if(fUser != null){
            CurrentUser.getInstance().setDisplayName(account.getDisplayName());
            CurrentUser.getInstance().setGivenName(account.getGivenName());
            CurrentUser.getInstance().setFamilyName(account.getFamilyName());
            CurrentUser.getInstance().setEmail(account.getEmail());
            CurrentUser.getInstance().setAccountID(account.getId());
            CurrentUser.getInstance().setPhotoURL(account.getPhotoUrl());

            NavHostFragment.findNavController(this).navigate(R.id.clientHomeScreen);
        }

        if(debugging) {
//            Navigation.findNavController(getView()).navigate(R.id.clientHomeScreen);
            NavHostFragment.findNavController(this).navigate(R.id.clientHomeScreen);
        }
    }
}