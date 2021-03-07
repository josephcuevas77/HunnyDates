package com.example.hunnydates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.hunnydates.R;
import com.example.hunnydates.utils.CurrentUser;

public class EditClientFragment extends Fragment {

    private EditText profileName;

    public EditClientFragment() {
    }

    public static EditClientFragment newInstance(String param1, String param2) {
        EditClientFragment fragment = new EditClientFragment();
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
        View view = inflater.inflate(R.layout.edit_client_profile_information, container, false);
        profileName = view.findViewById(R.id.ec_name_text_field);
        profileName.setText(CurrentUser.getInstance().getGivenName() + " " + CurrentUser.getInstance().getFamilyName());

        return view;
    }
}
