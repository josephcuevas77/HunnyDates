package com.example.hunnydates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.hunnydates.R;

public class ViewDatesFragment extends Fragment {

    public ViewDatesFragment() {
    }

    public static ViewDatesFragment newInstance(String param1, String param2) {
        ViewDatesFragment fragment = new ViewDatesFragment();
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
        return inflater.inflate(R.layout.view_date_plans, container, false);
    }
}