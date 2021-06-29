package com.mproduction.watchplaces.ui.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
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
import com.google.firebase.database.DatabaseError;
import com.mproduction.watchplaces.R;
import com.mproduction.watchplaces.data.LikeData;
import com.mproduction.watchplaces.data.PhotoItem;
import com.mproduction.watchplaces.data.PlaceItem;
import com.mproduction.watchplaces.data.PlaceType;
import com.mproduction.watchplaces.model.FirebaseDatabaseListener;
import com.mproduction.watchplaces.model.ScrimitDataModel;
import com.mproduction.watchplaces.model.ScrimitLocationModel;
import com.mproduction.watchplaces.ui.stackview.PhotoItemAdapter;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WearTrackScreen extends LocationTrackScreen implements View.OnClickListener, CardStackListener {
    private TextView toggleLabel;

    private TextView distanceLabel;
    private TextView timeLabel;

    private Place currentPlace;
    private String currentAddress;

    private ArrayList<PhotoItem> cardsData = new ArrayList<>();
    private PhotoItemAdapter cardsAdapter;
    private CardStackLayoutManager manager;
    private CardStackView cardView;

    private Location curLocation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        locationModel = ScrimitLocationModel.getInstance();
        locationModel.setLocationListener(this);
        dataModel = ScrimitDataModel.getInstance("XXpqDBPmcYaKsh2hdjfrN1qEfSh1");
        dataModel.listenData();

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
        cardsAdapter = new PhotoItemAdapter();
        cardView.setLayoutManager(new CardStackLayoutManager(this));
        cardView.setAdapter(cardsAdapter);
        manager = new CardStackLayoutManager(this, this);
        cardView.setLayoutManager(manager);

        findViewById(R.id.toggle_btn).setOnClickListener(this);
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.like_button).setOnClickListener(this);
        findViewById(R.id.skip_button).setOnClickListener(this);
        findViewById(R.id.check_btn).setOnClickListener(this);

        updateHeader();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_btn:
                checkCurLocation();
                break;
            case R.id.toggle_btn:
                toggleTracking();
                break;
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.like_button:
                likeItem();
                break;
            case R.id.skip_button:
                skipItem();
                break;
            case R.id.rewind_button:
                rewindItem();
                break;
        }
    }

    private void likeItem() {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new DecelerateInterpolator())
                .build();
        manager.setSwipeAnimationSetting(setting);
        cardView.swipe();
    }

    private void skipItem() {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new DecelerateInterpolator())
                .build();
        manager.setSwipeAnimationSetting(setting);
        cardView.swipe();
    }

    private void rewindItem() {
        RewindAnimationSetting setting = new RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new DecelerateInterpolator())
                .build();
        manager.setRewindAnimationSetting(setting);
        cardView.rewind();
    }

    @Override
    protected void toggleTracking() {
        super.toggleTracking();
    }

    @Override
    public void onTrackStarted() {
        super.onTrackStarted();
        toggleLabel.setText(locationModel.trackingNow ? R.string.stop_label : R.string.start_label);
        findViewById(R.id.check_btn).setVisibility(View.VISIBLE);
    }

    @Override
    public void onTrackEnded() {
        super.onTrackEnded();
        toggleLabel.setText(locationModel.trackingNow ? R.string.stop_label : R.string.start_label);
        findViewById(R.id.check_btn).setVisibility(View.GONE);
    }

    @Override
    public void onLocationUpdate(Location location) {
        checkCurPlace(location);
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

    private void checkCurPlace(Location location) {
        if (locationInitialized && (curLocation == null || curLocation.distanceTo(location) > 10)) {
            double distance = curLocation != null ? curLocation.distanceTo(location) : 0;
            curLocation = location;
        }
    }

    private void checkCurLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && locationInitialized) {

            ArrayList<Place.Field> placeFields = new ArrayList<>();
            placeFields.add(Place.Field.ID);
            placeFields.add(Place.Field.NAME);
            placeFields.add(Place.Field.TYPES);
            placeFields.add(Place.Field.ADDRESS);
            placeFields.add(Place.Field.LAT_LNG);

            FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
            PlacesClient placesClient = Places.createClient(this);
            uiHandler.sendEmptyMessage(UIHandler.MSG_SHOW_PROGRESS);
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                uiHandler.sendEmptyMessage(UIHandler.MSG_HIDE_PROGRESS);
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
        if (place != null) {
            currentPlace = place;
            String address = place != null ? place.getAddress() : "None";
            if (!address.equals(currentAddress)) {
                currentAddress = address;

                cardsData = dataModel.getPhotoItems(place);
                overlayLayout.setVisibility(currentPlace != null && cardsData.size() > 0 ? View.VISIBLE : View.GONE);

                TextView placeTitle = findViewById(R.id.place_title);
                placeTitle.setText(place != null ? place.getName() : getText(R.string.unknown));
                cardsAdapter.changeData(cardsData);
            }
        }
    }

    private void processResult(int position, boolean like) {
        if (position < cardsData.size()) {
            PhotoItem photoItem = cardsData.get(position);
            PlaceItem item = dataModel.getPlaceItem(currentPlace.getId());
            PlaceType type = dataModel.getPlaceType(currentPlace);
            if (item != null) {
                LikeData likeData = dataModel.getLikeData(dataModel.user.uid, item.id, null);
                likeData.likeMap.put(photoItem.id, like);
                dataModel.saveLikeData(likeData, new FirebaseDatabaseListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(DatabaseError error) {

                    }
                });
            } else if (type != null) {
                LikeData likeData = dataModel.getLikeData(dataModel.user.uid, null, type.id);
                likeData.likeMap.put(photoItem.id, like);
                dataModel.saveLikeData(likeData, new FirebaseDatabaseListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(DatabaseError error) {

                    }
                });
            }
        } else {
            overlayLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        int position = manager.getTopPosition();
        processResult(position, direction.equals(Direction.Right));
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}
