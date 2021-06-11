package com.mproduction.watchplaces.ui.location;

import android.location.Location;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.annotations.NotNull;
import com.mproduction.watchplaces.model.ScrimitLocationModel;
import com.mproduction.watchplaces.ui.template.ArenaActivity;

public class LocationMapScreen extends ArenaActivity implements ScrimitLocationModel.ScrimitLocationListener, OnMapReadyCallback {
    protected ScrimitLocationModel locationModel;

    protected MapView mMapView;
    protected GoogleMap mGoogleMap;

    protected ViewGroup overlayLayout;

    protected void toggleOverlay() {
        overlayLayout.setVisibility(overlayLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mMapView.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
        locationModel.clearListener();
    }

    @Override
    protected void initViews() {
        super.initViews();
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.clear();

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(43.656325, -79.380895)));
        mGoogleMap.setMinZoomPreference(12);
        mGoogleMap.setIndoorEnabled(true);
        UiSettings uiSettings = mGoogleMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
    }

    protected void moveCamera(double lat, double lng) {
        if (mGoogleMap != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
        }
        updateMap();
    }

    protected void updateMap() {

    }

    @Override
    protected void updateContents() {

    }

    @Override
    public void onLocationUpdate(Location location) {
        updateContents();
    }

    @Override
    public void onTrackStarted() {

    }

    @Override
    public void onTrackEnded() {

    }

    @Override
    public void onTrackResume() {

    }

    @Override
    public void onTrackPaused() {

    }
}

