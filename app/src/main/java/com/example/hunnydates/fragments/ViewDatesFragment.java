package com.example.hunnydates.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hunnydates.R;
import com.example.hunnydates.models.DatePlanModel;
import com.example.hunnydates.utils.CurrentUser;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;

public class ViewDatesFragment extends Fragment {
    public static final String CHANNEL_ID = "channel 2";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

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
        View view = inflater.inflate(R.layout.view_date_plans, container, false);
        initializeComponents(view);

        return view;
    }

    private void initializeComponents(View view) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.vdp_recycler_view);

        // Query
        Query query = CurrentUser.getInstance().getDatePlansCollections()
                .whereEqualTo("id", CurrentUser.getInstance().getEmail());

        // RecyclerOptions
        FirestoreRecyclerOptions<DatePlanModel> options = new FirestoreRecyclerOptions.Builder<DatePlanModel>()
                .setQuery(query, DatePlanModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<DatePlanModel, DatePlanViewHolder>(options) {
            @NonNull
            @Override
            public DatePlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_plan_item, parent, false);
                return new DatePlanViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DatePlanViewHolder holder, int position, @NonNull DatePlanModel model) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                holder.title.setText(model.getTitle());
                holder.description.setText(model.getDescription());
                holder.location.setText(model.getLocation());
                holder.user.setText(model.getUser());
                holder.rating.setText("Rating: " + model.getRating_count());
                Picasso.get().load(model.getImage_url()).into(holder.imageView);
                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CurrentUser.getInstance().getDatePlansCollections().document(snapshot.getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Document successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                    }
                });
                holder.upButton.setOnClickListener(new View.OnClickListener() {
                    DatePlanModel d = new DatePlanModel(
                            model.getUser(),
                            model.getTitle(),
                            model.getDescription(),
                            model.getLocation(),
                            model.getRating_count() + 1,
                            model.getId(),
                            model.getImage_url()
                    );

                    public void onClick(View view) {
                        CurrentUser.getInstance().getDatePlansCollections().document(snapshot.getId())
                        .set(d)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Document successfully edited!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error editing document", e);
                            }
                        });
                    }
                });
                holder.downButton.setOnClickListener(new View.OnClickListener() {
                    DatePlanModel d = new DatePlanModel(
                            model.getUser(),
                            model.getTitle(),
                            model.getDescription(),
                            model.getLocation(),
                            model.getRating_count() - 1,
                            model.getId(),
                            model.getImage_url()
                    );

                    public void onClick(View view) {
                        CurrentUser.getInstance().getDatePlansCollections().document(snapshot.getId())
                                .set(d)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Document successfully edited!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error editing document", e);
                                    }
                                });
                    }
                });
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private class DatePlanViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView location;
        private TextView user;
        private TextView rating;
        private Button deleteButton;
        private Button upButton;
        private Button downButton;
        private ImageView imageView;

        public DatePlanViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.dpi_title_tv);
            description = itemView.findViewById(R.id.dpi_desc_tv);
            location = itemView.findViewById(R.id.dpi_location_tv);
            deleteButton = itemView.findViewById(R.id.dpi_delete_btn);
            upButton = itemView.findViewById(R.id.dpi_up_button);
            downButton = itemView.findViewById(R.id.dpi_down_button);
            user = itemView.findViewById(R.id.dpi_user_tv);
            rating = itemView.findViewById(R.id.dpi_rating_tv);
            imageView = itemView.findViewById(R.id.dpi_imageView);
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