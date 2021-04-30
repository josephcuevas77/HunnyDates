package com.example.hunnydates.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hunnydates.R;
import com.example.hunnydates.utils.CurrentUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class MatchesFragment extends Fragment {
    public static final String CHANNEL_ID = "channel 2";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MatchesFragment() {
    }

    public static MatchesFragment newInstance(String param1, String param2) {
        MatchesFragment fragment = new MatchesFragment();
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
        return inflater.inflate(R.layout.view_matches, container, false);
    }
}