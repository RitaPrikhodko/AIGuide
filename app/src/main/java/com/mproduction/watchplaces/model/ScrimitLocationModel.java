package com.mproduction.watchplaces.model;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mproduction.watchplaces.R;
import com.mproduction.watchplaces.data.LocationRecord;
import com.mproduction.watchplaces.utils.Utils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScrimitLocationModel {

    public interface ScrimitLocationListener {
        void onLocationUpdate(Location location);
        void onTrackStarted();
        void onTrackEnded();
        void onTrackResume();
        void onTrackPaused();
    }

    public static ScrimitLocationModel sInstance = null;

    public static ScrimitLocationModel getInstance() {
        if (sInstance == null) {
            sInstance = new ScrimitLocationModel();
        }
        return sInstance;
    }

    public static void clearInstance() {
        sInstance = null;
    }

    public ScrimitLocationListener locationListener = null;

    public void setLocationListener(ScrimitLocationListener listener) {
        locationListener = listener;
    }

    public void clearListener() {
        locationListener = null;
    }

    public LocationRecord lastRecord = null;
    public ArrayList<LocationRecord> locationRecords = new ArrayList<>();
    public ArrayList<Location> locations = new ArrayList<>();
    public Location curLocation = null;
    public boolean trackingNow = false;
    public double maxSpeed = -1;
    public double minSpeed = -1;

    public void onLocationUpdate(Location location) {
        if (trackingNow) {
            if (curLocation != null && lastRecord != null) {
                LocationRecord record = new LocationRecord();
                record.start_lat = curLocation.getLatitude();
                record.start_lng = curLocation.getLongitude();
                record.start_alt = curLocation.getAltitude();
                record.end_lat = location.getLatitude();
                record.end_lng = location.getLongitude();
                record.end_alt = location.getAltitude();
                record.distance = curLocation.distanceTo(location);
                record.timestamp = Utils.getCurTime();
                record.timeDiff = record.timestamp - lastRecord.timestamp;

                lastRecord = record;

                locationRecords.add(record);
                if (maxSpeed == -1 || record.getSpeed() > maxSpeed) {
                    maxSpeed = record.getSpeed();
                }
                if (minSpeed == -1 || record.getSpeed() < minSpeed) {
                    minSpeed = record.getSpeed();
                }
            } else {
                lastRecord =  new LocationRecord();
                lastRecord.timestamp = Utils.getCurTime();
            }
            locations.add(location);
        }

        curLocation = location;
        if (locationListener != null) {
            locationListener.onLocationUpdate(location);
        }
    }

    public void onTrackStarted() {
        if (locationListener != null) {
            locationListener.onTrackStarted();
        }
        locationRecords = new ArrayList<>();
        locations = new ArrayList<>();
        curLocation = null;
        lastRecord = null;
        maxSpeed = -1;
        minSpeed = -1;
    }

    public void onTrackEnded() {
        if (locationListener != null) {
            locationListener.onTrackEnded();
        }
    }

    public void onTrackResume() {
        if (locationListener != null) {
            locationListener.onTrackResume();
        }
    }

    public void onTrackPaused() {
        if (locationListener != null) {
            locationListener.onTrackPaused();
        }
    }

    public void setTrackingNow(boolean tracking) {
        trackingNow = tracking;
    }

    public String getLocationText() {
        return curLocation == null ? "Tracking location" :
                "(" + curLocation.getLatitude() + ", " + curLocation.getLongitude() + ")";
    }

    public String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(new Date()));
    }

    public LatLng getCurLatLng() {
        return new LatLng(curLocation.getLatitude(), curLocation.getLongitude());
    }

    public double getTotalDistance() {
        double result = 0;

        for (LocationRecord item : locationRecords) {
            result += item.distance;
        }

        return result;
    }

    public long getTrackingTime() {
        long result = 0;

        for (LocationRecord item : locationRecords) {
            result += item.timeDiff;
        }

        return result;
    }

    public String getDistance() {
        double distance = getTotalDistance();

        return Utils.getDistanceStr(distance);
    }

    public String getTime() {
        long total = getTrackingTime();

        return Utils.getSecondsString(total);
    }

    public String getTimerTime() {
        long total = 0;
        if (locationRecords.size() > 0) {
            total = Utils.getCurTime() - locationRecords.get(0).timestamp - locationRecords.get(0).timeDiff;
        }

        return Utils.getSecondsString(total);
    }

    public String getMaxSpeed() {
        double result = maxSpeed > 0 ? maxSpeed : 0;

        return String.format("%.2f", result) + "m/s";
    }

    public String getMinSpeed() {
        double result = minSpeed > 0 ? minSpeed : 0;

        return String.format("%.2f", result) + "m/s";
    }

    public String getCurrentSpeed() {
        double result = lastRecord != null ? lastRecord.getSpeed() : 0;

        return String.format("%.2f", result) + "m/s";
    }

    public String getMaxPace() {
        double result = maxSpeed > 0 ? maxSpeed : 0;

        return Utils.getPaceString(result);
    }

    public String getMinPace() {
        double result = minSpeed > 0 ? minSpeed : 0;

        return Utils.getPaceString(result);
    }

    public String getCurrentPace() {
        double result = lastRecord != null ? lastRecord.getSpeed() : 0;

        return Utils.getPaceString(result);
    }

    public double getProgress() {
        return 0;
    }

    public Marker updateMarker(GoogleMap mGoogleMap, Marker curMarker, String title) {
        if (curMarker == null) {
            curMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(getCurLatLng())
                    .title(title));
        } else {
            curMarker.setPosition(getCurLatLng());
        }

        return curMarker;
    }

}
