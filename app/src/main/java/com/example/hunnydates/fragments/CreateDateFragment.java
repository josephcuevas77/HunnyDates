package com.example.hunnydates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.hunnydates.R;
import com.example.hunnydates.utils.CurrentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateDateFragment extends Fragment {

    private EditText dateTitle;
    private EditText dateDesc;
    private Button createDate;
    private FirebaseFirestore database;

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

        initializeComponents(view);
        createDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDateToFirestore();
            }
        });

        return view;
    }

    private void initializeComponents(View view) {
        dateTitle = view.findViewById(R.id.dp_title_et);
        dateDesc = view.findViewById(R.id.dp_description_et);
        createDate = view.findViewById(R.id.dp_create_button);
    }

    private void postDateToFirestore() {
        database = FirebaseFirestore.getInstance();
        Map<String, Object> dateData = new HashMap<>();

        dateData.put("title", dateTitle.getText().toString());
        dateData.put("description", dateDesc.getText().toString());

        CurrentUser.getInstance().getDocument()
                .collection("date-plans")
                .add(dateData);
    }
}