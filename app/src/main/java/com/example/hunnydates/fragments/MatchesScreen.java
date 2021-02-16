package com.example.hunnydates.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hunnydates.R;

public class MatchesScreen extends Fragment {

    public MatchesScreen() {
    }

    public static MatchesScreen newInstance(String param1, String param2) {
        MatchesScreen fragment = new MatchesScreen();
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
        return inflater.inflate(R.layout.view_matches_screen, container, false);
    }
}