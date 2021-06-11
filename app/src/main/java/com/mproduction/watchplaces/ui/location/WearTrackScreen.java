package com.mproduction.watchplaces.ui.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mproduction.watchplaces.R;
import com.mproduction.watchplaces.data.TrackCardRecord;
import com.mproduction.watchplaces.model.ScrimitLocationModel;
import com.mproduction.watchplaces.ui.stackview.TrackCardsAdapter;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WearTrackScreen extends LocationTrackScreen implements View.OnClickListener {
    private TextView toggleLabel;

    private TextView distanceLabel;
    private TextView timeLabel;

    private Place currentPlace;
    private String currentAddress;

    private ArrayList<TrackCardRecord> cardsData = new ArrayList<>();
    private TrackCardsAdapter cardsAdapter;
    private CardStackView cardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        locationModel = ScrimitLocationModel.getInstance();
        locationModel.setLocationListener(this);

        setContentView(R.layout.wear_track_screen);
        initViews();
        overlayLayout = findViewById(R.id.overlay);
        mMapView = findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

    }

    @Override
    protected void initViews() {
        super.initViews();

        toggleLabel = findViewById(R.id.toggle_label);
        distanceLabel = findViewById(R.id.distance_label);
        timeLabel = findViewById(R.id.time_label);
        cardView = findViewById(R.id.card_stack_view);
        cardsAdapter = new TrackCardsAdapter();
        cardView.setLayoutManager(new CardStackLayoutManager(this));
        cardView.setAdapter(cardsAdapter);


        findViewById(R.id.toggle_btn).setOnClickListener(this);
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.like_button).setOnClickListener(this);
        findViewById(R.id.skip_button).setOnClickListener(this);

        updateHeader();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggle_btn:
                toggleTracking();
                break;
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.skip_button:
                overlayLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void toggleTracking() {
        super.toggleTracking();
    }

    @Override
    public void onTrackStarted() {
        super.onTrackStarted();
        toggleLabel.setText(locationModel.trackingNow ? R.string.stop_label : R.string.start_label);
    }

    @Override
    public void onTrackEnded() {
        super.onTrackEnded();
        toggleLabel.setText(locationModel.trackingNow ? R.string.stop_label : R.string.start_label);
    }

    @Override
    public void onLocationUpdate(Location location) {
        checkCurPlace();
        super.onLocationUpdate(location);
        updateFooter();
        updateHeader();

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key), Locale.US);
        }

    }

    private void updateFooter() {
    }

    private void updateHeader() {
        TextView curSpeed = findViewById(R.id.current_speed);

        curSpeed.setText(locationModel.getCurrentPace());
        distanceLabel.setText(locationModel.getDistance());
        timeLabel.setText(locationModel.trackingNow ? locationModel.getTimerTime() : locationModel.getTime());
    }

    private void checkCurPlace() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && locationInitialized) {

            ArrayList<Place.Field> placeFields = new ArrayList<>();
            placeFields.add(Place.Field.NAME);
            placeFields.add(Place.Field.TYPES);
            placeFields.add(Place.Field.ADDRESS);

            FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
            PlacesClient placesClient = Places.createClient(this);
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FindCurrentPlaceResponse response = task.getResult();
                    if (response.getPlaceLikelihoods().size() > 0) {
                        PlaceLikelihood placeLikelihood = response.getPlaceLikelihoods().get(0);
                        uiHandler.sendMessage(uiHandler.obtainMessage(UIHandler.MSG_UPDATE_OVERLAY, placeLikelihood.getPlace()));
                        Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                    uiHandler.sendMessage(uiHandler.obtainMessage(UIHandler.MSG_UPDATE_OVERLAY, null));
                }
            });
        }
    }

    @Override
    protected void handleMessage(int message, Object object) {
        super.handleMessage(message, object);
        switch (message) {
            case UIHandler.MSG_UPDATE_OVERLAY:
                if (object == null || object instanceof Place) {
                    updateOverlay((Place) object);
                }
                break;
        }
    }

    private void updateOverlay(Place place) {
        if (currentPlace != place) {
            currentPlace = place;
            String address = place != null ? place.getAddress() : "None";
            if (!address.equals(currentAddress)) {
                currentAddress = address;

                overlayLayout.setVisibility(currentPlace != null && checkPlace(currentPlace) ? View.VISIBLE : View.GONE);

                TextView placeTitle = findViewById(R.id.place_title);
                placeTitle.setText(place != null ? place.getName() : getText(R.string.unknown));
                cardsData = getSampleData();
                cardsAdapter.changeData(cardsData);
            }
        }
    }

    private boolean checkPlace(Place place) {
        if (place != null) {
            List<Place.Type> types = place.getTypes();

            if (types.contains(Place.Type.STORE)) {
                return true;
            }
        }

        return false;
    }

    private ArrayList<TrackCardRecord> getSampleData() {
        ArrayList<TrackCardRecord> result = new ArrayList<>();

        result.add(new TrackCardRecord("grocery_1", R.drawable.grocery_1, "grocery 1"));
        result.add(new TrackCardRecord("grocery_2", R.drawable.grocery_2, "grocery 2"));
        result.add(new TrackCardRecord("grocery_3", R.drawable.grocery_3, "grocery 3"));
        result.add(new TrackCardRecord("grocery_4", R.drawable.grocery_4, "grocery 4"));
        result.add(new TrackCardRecord("grocery_5", R.drawable.grocery_5, "grocery 5"));
        result.add(new TrackCardRecord("grocery_6", R.drawable.grocery_6, "grocery 6"));
        result.add(new TrackCardRecord("grocery_7", R.drawable.grocery_7, "grocery 7"));
        result.add(new TrackCardRecord("grocery_8", R.drawable.grocery_8, "grocery 8"));

        return result;
    }

}
