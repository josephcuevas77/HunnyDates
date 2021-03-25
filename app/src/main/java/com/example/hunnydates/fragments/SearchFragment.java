package com.example.hunnydates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.hunnydates.R;
import com.example.hunnydates.utils.CurrentUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {

    private EditText recipientEditText;
    private EditText messageEditText;
    private Button sendMessageButton;
    private Button showMessagesButton;

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

        showMessagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Show messages", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void initializeComponents(View view) {
        recipientEditText = view.findViewById(R.id.sup_recipient_et);
        messageEditText = view.findViewById(R.id.sup_message_et);
        sendMessageButton = view.findViewById(R.id.sup_send_msg_btn);
        showMessagesButton = view.findViewById(R.id.sup_show_msg_btn);
    }

    private void postMessageToFirestore() {
        Map<String, Object> messageData = new HashMap<>();

        messageData.put("recipient", recipientEditText.getText().toString());
        messageData.put("message", messageEditText.getText().toString());

        CurrentUser.getInstance().getDocument()
                .collection("outgoing-messages")
                .add(messageData);

        messageData.remove("recipient");
        messageData.put("sender", CurrentUser.getInstance().getEmail());

        FirebaseFirestore.getInstance()
                .collection("clients")
                .document(recipientEditText.getText().toString())
                .collection("incoming-messages")
                .add(messageData);

        Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_SHORT).show();
    }
}