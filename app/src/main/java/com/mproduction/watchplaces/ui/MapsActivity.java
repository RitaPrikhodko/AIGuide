package com.mproduction.watchplaces.ui;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mproduction.watchplaces.R;
import com.mproduction.watchplaces.databinding.ActivityMapsBinding;
import com.mproduction.watchplaces.model.ScrimitLocationModel;
import com.mproduction.watchplaces.services.ScrimitTrackService;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ScrimitLocationModel.ScrimitLocationListener {
    protected ScrimitLocationModel locationModel;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    ScrimitTrackService mService;
    private boolean mBound = false;
    private boolean locationInitialized = false;
    protected Marker curMarker;

    private GoogleMap mGoogleMap;
    private ActivityMapsBinding binding;

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
    public void onResume() {
        super.onResume();
        if (checkPermissions() && !locationInitialized) {
            beginTracking();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationModel = ScrimitLocationModel.getInstance();
        locationModel.setLocationListener(this);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        }
    }

    protected void stopTracking() {
        if (mService != null && locationModel.trackingNow) {
            mService.stopLocationTrack();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
        curMarker = locationModel.updateMarker(mGoogleMap, curMarker, "Your Location");
    }

    protected boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                    PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
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
        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                Build.VERSION.SDK_INT == Build.VERSION_CODES.R &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.location_prominent_title)
                    .setMessage(R.string.location_prominent_prompt)
                    .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton(R.string.cancel, null).create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onLocationUpdate(Location location) {

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