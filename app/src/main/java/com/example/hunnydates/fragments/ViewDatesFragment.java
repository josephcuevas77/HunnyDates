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
import com.example.hunnydates.models.DatePlanModel;
import com.example.hunnydates.utils.CurrentUser;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewDatesFragment extends Fragment {

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
        Query query = firebaseFirestore.collection("clients")
                .document(CurrentUser.getInstance().getEmail())
                .collection("date-plans");

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
                holder.title.setText(model.getTitle());
                holder.description.setText(model.getDescription());
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private class DatePlanViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;

        public DatePlanViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.dpi_title);
            description = itemView.findViewById(R.id.dpi_description);
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