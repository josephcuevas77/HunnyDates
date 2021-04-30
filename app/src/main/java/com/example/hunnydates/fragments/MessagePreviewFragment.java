package com.example.hunnydates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hunnydates.R;
import com.example.hunnydates.models.MessagePreviewModel;
import com.example.hunnydates.utils.CurrentUser;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class MessagePreviewFragment extends Fragment {

    private EditText recipientEditText;
    private Button navigateButton;
    private RecyclerView recyclerView;
    private NavController navController;
    private FirestoreRecyclerAdapter adapter;

    public MessagePreviewFragment() {
    }

    public static MessagePreviewFragment newInstance(String param1, String param2) {
        MessagePreviewFragment fragment = new MessagePreviewFragment();
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
        View view = inflater.inflate(R.layout.message_preview, container, false);
        initializeComponents(view);
        recyclerViewCode();

        return view;
    }

    private void initializeComponents(View view) {
        recipientEditText = view.findViewById(R.id.sup_recipient_et);
        recyclerView = view.findViewById(R.id.sup_recycler_view);
        navigateButton = view.findViewById(R.id.sup_nav_btn);
        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.client_nav_host_fragment);
        navController = navHostFragment.getNavController();

        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("recipient", recipientEditText.getText().toString());
                navController.navigate(R.id.messageFragment, bundle);
            }
        });
    }

    private void recyclerViewCode() {
        // Query
        Query query = CurrentUser.getInstance().getDocument()
                .collection("messages");

        // RecyclerOptions
        FirestoreRecyclerOptions<MessagePreviewModel> options = new FirestoreRecyclerOptions.Builder<MessagePreviewModel>()
                .setQuery(query, MessagePreviewModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<MessagePreviewModel, MessagePreviewFragment.MessagePreviewViewHolder>(options) {
            @NonNull
            @Override
            public MessagePreviewFragment.MessagePreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_preview_item, parent, false);
                return new MessagePreviewFragment.MessagePreviewViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MessagePreviewFragment.MessagePreviewViewHolder holder, int position, @NonNull MessagePreviewModel model) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                // Picasso is a library for importing images with a PhotoURL
                Picasso.get().load(snapshot.getString("photo-url")).into(holder.clientImageView);
                holder.clientImageView.setClickable(true);
                holder.clientImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", snapshot.getString("email"));
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_cb_nav_msg_preview_to_viewUserProfile, bundle);
                    }
                });
                holder.clientTextView.setText(snapshot.getString("display-name"));
                holder.clientTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("recipient", snapshot.getString("email"));
                        bundle.putString("photo-url", snapshot.getString("photo-url"));
                        navController.navigate(R.id.messageFragment, bundle);
                    }
                });
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private class MessagePreviewViewHolder extends RecyclerView.ViewHolder {

        private ImageView clientImageView;
        private TextView clientTextView;

        public MessagePreviewViewHolder(@NonNull View itemView) {
            super(itemView);
            clientImageView = itemView.findViewById(R.id.mpi_profile_iv);
            clientTextView = itemView.findViewById(R.id.mpi_client_tv);
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