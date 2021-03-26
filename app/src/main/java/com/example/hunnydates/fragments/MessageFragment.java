package com.example.hunnydates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hunnydates.R;
import com.example.hunnydates.models.MessageModel;
import com.example.hunnydates.utils.CurrentUser;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class MessageFragment extends Fragment {

    private TextView header;
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private String recipient;

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

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.cmd_recycler_view);

        // Query
        Query query = CurrentUser.getInstance().getDocument()
                .collection("messages")
//                .whereEqualTo("receiver", recipient)
                .orderBy("time-stamp");

        // RecyclerOptions
        FirestoreRecyclerOptions<MessageModel> options = new FirestoreRecyclerOptions.Builder<MessageModel>()
                .setQuery(query, MessageModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<MessageModel, MessageFragment.MessageViewHolder>(options) {
            @NonNull
            @Override
            public MessageFragment.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
                return new MessageFragment.MessageViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessageModel model) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                holder.sender.setText(model.getSender());
                holder.receiver.setText(model.getReceipient());
                holder.message.setText(model.getMessage());
//                holder.timestamp.setText(model.getTimestamp().toString());
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView sender;
        private TextView receiver;
        private TextView message;
        private TextView timestamp;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sender = itemView.findViewById(R.id.msg_sender_tv);
            receiver = itemView.findViewById(R.id.msg_receiver_tv);
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