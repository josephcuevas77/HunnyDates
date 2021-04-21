package com.example.hunnydates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.hunnydates.R;
import com.example.hunnydates.utils.CurrentUser;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ClientCreationFragment extends Fragment {

    private ImageView profileImage;
    private EditText usernameEditText;
    private EditText displayNameEditText;
    private EditText ageEditText;
    private EditText descriptionEditText;
    private EditText referralCodeEditText;
    private Button createAccountButton;

    private String email, profileURL, username, displayName;

    public ClientCreationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString("email");
            profileURL = getArguments().getString("profile-url");
            username = getArguments().getString("username");
            displayName = getArguments().getString("displayName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_creation_screen, container, false);
        initializeComponents(view);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> data = new HashMap();
                data.put("email", email);
                data.put("display-name", displayNameEditText.getText().toString());
                data.put("username", usernameEditText.getText().toString());
                data.put("age", ageEditText.getText().toString());
                data.put("profile-url", profileURL);
                data.put("description", descriptionEditText.getText().toString());
                CurrentUser.getInstance().getDocument().set(data);
                CurrentUser.getInstance().queryProfileInfo();
                NavHostFragment.findNavController(getParentFragment()).popBackStack();
            }
        });

        return view;
    }

    private void initializeComponents(View view) {
        profileImage = view.findViewById(R.id.cc_profile_icon);
        usernameEditText = view.findViewById(R.id.cc_username_edit_text);
        displayNameEditText = view.findViewById(R.id.cc_display_name_edit_text);
        ageEditText = view.findViewById(R.id.cc_age_edit_text);
        descriptionEditText = view.findViewById(R.id.cc_description_edit_text);
        referralCodeEditText = view.findViewById(R.id.cc_referral_code_edit_text);
        createAccountButton = view.findViewById(R.id.cc_create_account_button);

        String profilePicURL = profileURL.replace("s96", "s384");
        Picasso.get().load(profilePicURL).into(profileImage);
        usernameEditText.setText(username);
        displayNameEditText.setText(displayName);
    }
}