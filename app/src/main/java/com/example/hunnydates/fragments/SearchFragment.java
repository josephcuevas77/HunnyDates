package com.example.hunnydates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.hunnydates.R;
import com.example.hunnydates.utils.CurrentUser;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {

    private EditText recipientEditText;
    private EditText messageEditText;
    private Button sendMessageButton;
    private Button tempNavigateButton;
    private NavController navController;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.search_user_profile, container, false);
        initializeComponents(view);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postMessageToFirestore();
            }
        });

        tempNavigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("recipient", recipientEditText.getText().toString());
                navController.navigate(R.id.messageFragment, bundle);
            }
        });

        return view;
    }

    private void initializeComponents(View view) {
        recipientEditText = view.findViewById(R.id.sup_recipient_et);
        messageEditText = view.findViewById(R.id.sup_message_et);
        sendMessageButton = view.findViewById(R.id.sup_send_msg_btn);
        tempNavigateButton = view.findViewById(R.id.sup_tmp_btn);
        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.client_nav_host_fragment);
        navController = navHostFragment.getNavController();
    }

    private void postMessageToFirestore() {
        Map<String, Object> messageData = new HashMap<>();

        String receiver = recipientEditText.getText().toString();
        String sender = CurrentUser.getInstance().getEmail();
        String message = messageEditText.getText().toString();

        messageData.put("recipient", receiver);
        messageData.put("sender", sender);
        messageData.put("message", message);
        messageData.put("time-stamp", Timestamp.now());

        CurrentUser.getInstance().getDocument()
                .collection("messages")
                .add(messageData);

        FirebaseFirestore.getInstance()
                .collection("clients")
                .document(receiver)
                .collection("messages")
                .add(messageData);

        Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_SHORT).show();
    }
}