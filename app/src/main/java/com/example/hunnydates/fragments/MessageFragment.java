package com.example.hunnydates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hunnydates.R;
import com.example.hunnydates.models.MessageModel;
import com.example.hunnydates.utils.CurrentUser;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class MessageFragment extends Fragment {

    // Components
    private TextView header;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private EditText messageEditText;
    private Button sendButton;
    private ImageView clientProfilePicture;

    // Instance Variables
    private DocumentReference senderDocument, receiverDocument;
    private String senderEmail, senderDisplayName, senderPhotoURL,
                    recipientEmail, recipientDisplayName, recipientPhotoURL;

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
            recipientEmail = getArguments().getString("recipient");
            recipientPhotoURL = getArguments().getString("photo-url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_messaging_display, container, false);
        initializeComponents(view);
        initializeInstanceVariables();
        recyclerViewCode();
        return view;
    }

    private void initializeComponents(View view) {
        clientProfilePicture = view.findViewById(R.id.cmd_profile_iv);
        // Picasso is a library for importing images with a PhotoURL
        Picasso.get().load(recipientPhotoURL).into(clientProfilePicture);
        header = view.findViewById(R.id.cmd_header_tv);
        messageEditText = view.findViewById(R.id.cmd_message_et);
        recyclerView = view.findViewById(R.id.cmd_recycler_view);
        sendButton = view.findViewById(R.id.cmd_send_msg_btn);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postMessageToFirestore();
            }
        });
    }

    private void initializeInstanceVariables() {
        senderDocument = CurrentUser.getInstance()
                .getDocument();
        senderEmail = CurrentUser.getInstance().getEmail();
        senderDisplayName = CurrentUser.getInstance().getDisplayName();
        senderPhotoURL = CurrentUser.getInstance().getPhotoURL().toString();

        receiverDocument = FirebaseFirestore.getInstance()
                .collection("clients")
                .document(recipientEmail);

        receiverDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        recipientDisplayName = document.getString("display-name");
                        header.setText(recipientDisplayName);
                        recipientPhotoURL = document.getString("profile-url");
                    }
                }
            }
        });
    }

    private void recyclerViewCode() {
        // Query
        Query query = senderDocument
                .collection("messages")
                .document(recipientEmail)
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
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
                return new MessageFragment.MessageViewHolder(view);
            }

            @Override
            public int getItemViewType(int position) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                if(snapshot.getBoolean("isSender") == (true))
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

        // Define/Instantiate layoutManager so we can setStackFromEnd to true (display from bottom to top)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        // Code for recycler view auto scrolling to bottom only if you are already at the bottom
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapter.getItemCount();
                int lastVisiblePosition =
                        layoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });

        recyclerView.setAdapter(adapter);

    }

    private void postMessageToFirestore() {
        Map<String, Object> messageData = new HashMap<>();
        Map<String, Object> receiverData = new HashMap<>();
        Map<String, Object> senderData = new HashMap<>();

        String sender = CurrentUser.getInstance().getEmail();
        String message = messageEditText.getText().toString();

        messageData.put("isSender", true);
        messageData.put("message", message);
        messageData.put("time-stamp", FieldValue.serverTimestamp());

        receiverData.put("display-name", recipientDisplayName);
        receiverData.put("email", recipientEmail);
        receiverData.put("photo-url", recipientPhotoURL);
        senderData.put("display-name", senderDisplayName);
        senderData.put("email", senderEmail);
        senderData.put("photo-url", senderPhotoURL);

        senderDocument.collection("messages")
                .document(recipientEmail)
                .collection("messages")
                .add(messageData);

        senderDocument.collection("messages")
                .document(recipientEmail)
                .set(receiverData);

        messageData.put("isSender", false);

        receiverDocument.collection("messages")
                .document(sender)
                .collection("messages")
                .add(messageData);

        receiverDocument.collection("messages")
                .document(sender)
                .set(senderData);

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