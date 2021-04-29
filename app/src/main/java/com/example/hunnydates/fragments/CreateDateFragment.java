package com.example.hunnydates.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.hunnydates.R;
import com.example.hunnydates.utils.CurrentUser;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static android.app.Activity.RESULT_CANCELED;

public class CreateDateFragment extends Fragment {

    private static final String TAG = "placesAPI";
    private static final String TAG1 = "bitmap";

    private final int RESULT_OK = -1;
    private final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private EditText dateTitle;
    private EditText dateDesc;
    private Button createDate;
    private Button searchButton;
    private TextView placeSelected;
    private ImageView placeImage;

    public CreateDateFragment() {
    }

    public static CreateDateFragment newInstance(String param1, String param2) {
        CreateDateFragment fragment = new CreateDateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            fetchPhoto(place);
            Log.i(TAG, "Place: " + place.getName() + ", " + place.getAddress());
            Toast.makeText(getActivity().getApplicationContext(), "Place: " + place.getName() + ", " + place.getId(), Toast.LENGTH_LONG).show();
            placeSelected.setText(place.getName() + "\n" + place.getAddress() + "\n" +
                    place.getPhoneNumber());
        }else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.i(TAG, status.getStatusMessage());
        }else if(resultCode == RESULT_CANCELED){

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        Places.initialize(getActivity().getApplicationContext(), "AIzaSyDk1EqoqRzGwNDEVPojmXsDklfbLnx_H9E");
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

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startAutocompleteActivity(view);
            }
            });

        return view;
    }

    private void initializeComponents(View view) {
        dateTitle = view.findViewById(R.id.dp_title_et);
        dateDesc = view.findViewById(R.id.dp_description_et);
        createDate = view.findViewById(R.id.dp_create_button);
        searchButton = view.findViewById(R.id.dp_search_button);
        placeSelected = view.findViewById(R.id.dp_place_selected);
        placeImage = view.findViewById(R.id.dp_image_view);
    }

    private void postDateToFirestore() {
        Map<String, Object> dateData = new HashMap<>();

        dateData.put("title", dateTitle.getText().toString());
        dateData.put("description", dateDesc.getText().toString());
        dateData.put("location", placeSelected.getText().toString());

        CurrentUser.getInstance().getDocument()
                .collection("date-plans")
                .add(dateData);

        Toast.makeText(getActivity(), "Date Plan Added", Toast.LENGTH_SHORT).show();
    }

    public void startAutocompleteActivity(View view){
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN,
                Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME, Place.Field.PHONE_NUMBER,
                        Place.Field.PHOTO_METADATAS, Place.Field.ID))
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setLocationBias(RectangularBounds.newInstance(
                        new LatLng(33.6846,117.8265), //Irvine
                        new LatLng(34.0522, 118.2437))) //LA
                .setCountries(Arrays.asList("US"))
                .build(this.getActivity().getApplicationContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    public void fetchPhoto(Place place){
        PlacesClient placesClient = Places.createClient(getActivity().getApplicationContext());
        final String placeId = place.getId();
        final List<Place.Field> fields = Collections.singletonList(Place.Field.PHOTO_METADATAS);
        final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);
        final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
        if (metadata == null || metadata.isEmpty()) {
            Log.w(TAG, "No photo metadata.");
            return;
        }
        final PhotoMetadata photoMetadata = metadata.get(0);

        final String attributions = photoMetadata.getAttributions();

        final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                .build();
        placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
            Bitmap bitmap = fetchPhotoResponse.getBitmap();
            Log.i(TAG1, "Bitmap to String attempt: " + bitmap.toString());
            placeImage.setImageBitmap(bitmap);
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
            }
        });
    }
}