package com.mproduction.watchplaces.ui.location;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.annotations.NotNull;
import com.mproduction.watchplaces.R;
import com.mproduction.watchplaces.services.ScrimitTrackService;
import com.mproduction.watchplaces.ui.location.LocationMapScreen;

import java.util.ArrayList;

public class LocationTrackScreen extends LocationMapScreen implements GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    protected static final String TAG = "TRACK SCREEN";

    ScrimitTrackService mService;
    private boolean mBound = false;
    protected boolean locationInitialized = false;
    protected Marker curMarker;
    protected Polyline curTrackLine;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ScrimitTrackService.LocalBinder binder = (ScrimitTrackService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            if (checkPermissions() && !locationInitialized) {
                beginTracking();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void initViews() {
        super.initViews();

        if (!checkPermissions()) {
            requestPermissions();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, ScrimitTrackService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions() && !locationInitialized) {
            beginTracking();
        }
    }

    @Override
    protected void onStop() {
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
        super.onStop();
    }

    protected boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) && PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    protected void requestPermissions() {
        final String[] permissions = Build.VERSION.SDK_INT == Build.VERSION_CODES.Q ?
                new String[] {
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                } :
                new String[] {
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                };
        ActivityCompat.requestPermissions(this,
                permissions,
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    protected void toggleTracking() {
        if (mService != null) {
            if (!locationModel.trackingNow) {
                mService.startLocationTrack();
            } else {
                mService.stopLocationTrack();
            }
        }
    }

    protected void beginTracking() {
        if (mService != null && !locationModel.trackingNow) {
            mService.startLocationTrack();
            curTrackLine = null;
        }
    }

    protected void stopTracking() {
        if (mService != null && locationModel.trackingNow) {
            mService.stopLocationTrack();
        }
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        super.onMapReady(googleMap);
        if (checkPermissions() && !locationInitialized) {
            beginTracking();
        }
    }

    @Override
    public void onLocationUpdate(Location location) {
        super.onLocationUpdate(location);
        if (locationModel.trackingNow) {
            moveCamera(location.getLatitude(), location.getLongitude());
        }
        if (!locationInitialized) {
            stopTracking();
            locationInitialized = true;
        }
        updateTrackingRoute();
        if (locationModel.getProgress() >= 1) {
            stopTracking();
        }
    }

    public void updateTrackingRoute() {
        if (curMarker == null) {
            curMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(locationModel.getCurLatLng())
                    .title("Your Location"));
        } else {
            curMarker.setPosition(locationModel.getCurLatLng());
        }
        if (curTrackLine == null) {
            curTrackLine = mGoogleMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .color(getResources().getColor(R.color.track_route_color))
                    .add(locationModel.getCurLatLng()));
        } else {
            ArrayList<LatLng> items = new ArrayList<>();
            for (Location item : locationModel.locations) {
                items.add(new LatLng(item.getLatitude(), item.getLongitude()));
            }
            curTrackLine.setPoints(items);
        }

    }

    @Override
    public void onPolygonClick(@NonNull @NotNull Polygon polygon) {

    }

    @Override
    public void onPolylineClick(@NonNull @NotNull Polyline polyline) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermissions() && !locationInitialized) {
            beginTracking();
        }
        if (!checkPermissions()) {
            requestPermissions();
        }
    }
}
