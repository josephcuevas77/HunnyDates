package com.example.hunnydates.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hunnydates.CurrentUser;
import com.example.hunnydates.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ClientHomeScreen extends AppCompatActivity {

    private ImageView profileImage;
    private TextView profileName;
    private Button editProfileButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_home_screen);

        profileImage = findViewById(R.id.ch_profile_icon);
        profileName = findViewById(R.id.ch_profile_name);
        editProfileButton = findViewById(R.id.ch_edit_profile_button);
        logoutButton = findViewById(R.id.ch_logout_button);

        Picasso.get().load(CurrentUser.getInstance().getPhotoURL()).into(profileImage);
        profileName.setText(CurrentUser.getInstance().getGivenName() + " " + CurrentUser.getInstance().getFamilyName());

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ClientHomeScreen.this, CurrentUser.getInstance().getPhotoURL().toString(), Toast.LENGTH_LONG).show();
                System.out.println(CurrentUser.getInstance().getPhotoURL().toString());
            }
        });
    }
}