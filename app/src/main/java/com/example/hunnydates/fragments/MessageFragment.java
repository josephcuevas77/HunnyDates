package com.example.hunnydates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hunnydates.R;
import com.example.hunnydates.models.MessageModel;
import com.example.hunnydates.utils.CurrentUser;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;


public class MessageFragment extends Fragment {

    private TextView header;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private String recipient;
    private EditText messageEditText;
    private Button sendButton;

    public MessageFragment() {
    }

    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipient = getArguments().getString("recipient");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_messaging_display, container, false);
        initializeComponents(view);
        return view;
    }

    private void initializeComponents(View view) {
        header = view.findViewById(R.id.cmd_header_tv);
        header.setText(recipient);
        messageEditText = view.findViewById(R.id.cmd_message_et);
        recyclerView = view.findViewById(R.id.cmd_recycler_view);
        sendButton = view.findViewById(R.id.cmd_send_msg_btn);

        // Query
        Query query = CurrentUser.getInstance().getDocument()
                .collection("messages")
                .document(recipient)
                .collection("messages")
                .orderBy("time-stamp");

        // RecyclerOptions
        FirestoreRecyclerOptions<MessageModel> options = new FirestoreRecyclerOptions.Builder<MessageModel>()
                .setQuery(query, MessageModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<MessageModel, MessageFragment.MessageViewHolder>(options) {
            @NonNull
            @Override
            public MessageFragment.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view;
                if(viewType == 1)
                    view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_message_item, parent, false);
                else
                    view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_message_item, parent, false);
                return new MessageFragment.MessageViewHolder(view);
            }

            @Override
            public int getItemViewType(int position) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                if(snapshot.get("sender").equals(CurrentUser.getInstance().getEmail()))
                    return 1;
                else
                    return 0;
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessageModel model) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());

                DocumentSnapshot.ServerTimestampBehavior behavior = DocumentSnapshot.ServerTimestampBehavior.ESTIMATE;
                Timestamp timestamp = snapshot.getTimestamp("time-stamp", behavior);
                holder.message.setText(model.getMessage());
                holder.timestamp.setText(timestamp.toDate().toString());
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postMessageToFirestore();
            }
        });
    }

    private void postMessageToFirestore() {
        Map<String, Object> messageData = new HashMap<>();
        Map<String, Object> receiverData = new HashMap<>();
        Map<String, Object> senderData = new HashMap<>();

        String receiver = recipient;
        String sender = CurrentUser.getInstance().getEmail();
        String message = messageEditText.getText().toString();

        messageData.put("recipient", receiver);
        messageData.put("sender", sender);
        messageData.put("message", message);
        messageData.put("time-stamp", FieldValue.serverTimestamp());

        receiverData.put("display-name", receiver);
        senderData.put("display-name", sender);

        CurrentUser.getInstance()
                .getDocument()
                .collection("messages")
                .document(receiver)
                .collection("messages")
                .add(messageData);

        CurrentUser.getInstance()
                .getDocument()
                .collection("messages")
                .document(receiver)
                .set(receiverData);

        FirebaseFirestore.getInstance()
                .collection("clients")
                .document(receiver)
                .collection("messages")
                .document(sender)
                .collection("messages")
                .add(messageData);

        FirebaseFirestore.getInstance()
                .collection("clients")
                .document(receiver)
                .collection("messages")
                .document(sender)
                .set(senderData);

        Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_SHORT).show();
        messageEditText.setText("");
    }

    private class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView message;
        private TextView timestamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.msg_message_tv);
            timestamp = itemView.findViewById(R.id.msg_timestamp_tv);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}