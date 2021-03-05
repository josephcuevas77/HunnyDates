package com.example.hunnydates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.hunnydates.R;

public class CreateDateFragment extends Fragment {

    private EditText dateTitle;
    private EditText dateDesc;
    //private Button createDate;

    public CreateDateFragment() {
    }

    public static CreateDateFragment newInstance(String param1, String param2) {
        CreateDateFragment fragment = new CreateDateFragment();
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
        View view = inflater.inflate(R.layout.create_date_plan, container, false);
        return view;
    }
}