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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hunnydates.R;
import com.example.hunnydates.models.DatePlanPublicModel;
import com.example.hunnydates.utils.CurrentUser;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ViewPublicDatesFragment extends Fragment {
    public static final String CHANNEL_ID = "channel 2";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

    public ViewPublicDatesFragment() {
    }

    public static ViewPublicDatesFragment newInstance(String param1, String param2) {
        ViewPublicDatesFragment fragment = new ViewPublicDatesFragment();
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
        View view = inflater.inflate(R.layout.view_all_date_plans, container, false);
        initializeComponents(view);

        return view;
    }

    private void initializeComponents(View view) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.vadp_recycler_view);

        // Query
        List<String> temp = new ArrayList<>();
        for(Map.Entry<String,Boolean> entry : CurrentUser.getInstance().getBlockedUsers().entrySet()) {
            temp.add(entry.getKey());
        }

        Query query;
        if (temp.isEmpty()) {
            query = CurrentUser.getInstance().getDatePlansCollections()
                .whereEqualTo("is_private", false);
        }
        else {
            query = CurrentUser.getInstance().getDatePlansCollections()
                    .whereNotIn("id", temp)
                    .whereEqualTo("is_private", false);
        }

        // RecyclerOptions
        FirestoreRecyclerOptions<DatePlanPublicModel> options = new FirestoreRecyclerOptions.Builder<DatePlanPublicModel>()
                .setQuery(query, DatePlanPublicModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<DatePlanPublicModel, DatePlanViewHolder>(options) {
            @NonNull
            @Override
            public DatePlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_plan_public_item, parent, false);
                return new DatePlanViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DatePlanViewHolder holder, int position, @NonNull DatePlanPublicModel model) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                holder.title.setText(model.getTitle());
                holder.description.setText(model.getDescription());
                holder.location.setText(model.getLocation());
                holder.user.setText(model.getUser());
                holder.rating.setText("Rating: " + model.getRating_count());
                Picasso.get().load(model.getImage_url()).into(holder.locationImageView);
                Picasso.get().load(model.getUser_profile_image_url()).into(holder.profileImageView);
                holder.profileImageView.setClickable(true);
                holder.profileImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", model.getId());
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_cb_nav_public_dates_to_viewUserProfile, bundle);
                    }
                });
                holder.voteUpImageView.setClickable(true);
                holder.voteUpImageView.setOnClickListener(new View.OnClickListener() {
                    DatePlanPublicModel d = new DatePlanPublicModel(
                            model.getUser(),
                            model.getTitle(),
                            model.getDescription(),
                            model.getLocation(),
                            model.getRating_count() + 1,
                            model.getId(),
                            model.getUser_profile_image_url(),
                            model.getImage_url(),
                            model.getIs_Private()

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
                holder.voteDownImageView.setClickable(true);
                holder.voteDownImageView.setOnClickListener(new View.OnClickListener() {
                    DatePlanPublicModel d = new DatePlanPublicModel(
                            model.getUser(),
                            model.getTitle(),
                            model.getDescription(),
                            model.getLocation(),
                            model.getRating_count() - 1,
                            model.getId(),
                            model.getUser_profile_image_url(),
                            model.getImage_url(),
                            model.getIs_Private()
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
        private ImageView profileImageView;
        private ImageView voteUpImageView;
        private ImageView voteDownImageView;
        private ImageView locationImageView;

        public DatePlanViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.dppi_title_tv);
            description = itemView.findViewById(R.id.dppi_desc_tv);
            location = itemView.findViewById(R.id.dppi_location_tv);
            profileImageView = itemView.findViewById(R.id.dppi_profile_iv);
            locationImageView = itemView.findViewById(R.id.dppi_location_iv);
            voteUpImageView = itemView.findViewById(R.id.dppi_vote_up_iv);
            voteDownImageView = itemView.findViewById(R.id.dppi_vote_down_iv);
            user = itemView.findViewById(R.id.dppi_user_tv);
            rating = itemView.findViewById(R.id.dppi_rating_tv);
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