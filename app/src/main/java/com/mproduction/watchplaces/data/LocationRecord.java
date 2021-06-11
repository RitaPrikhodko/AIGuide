package com.mproduction.watchplaces.data;

import com.mproduction.watchplaces.utils.FirebaseUtils;

import java.util.HashMap;

public class LocationRecord {
    public int index = -1;
    public double start_lat;
    public double start_lng;
    public double start_alt;
    public double end_lat;
    public double end_lng;
    public double end_alt;
    public double distance;
    public long timestamp;
    public long timeDiff;

    public HashMap<String, Object> getDataMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("index", index);
        result.put("start_lat", start_lat);
        result.put("start_lng", start_lng);
        result.put("start_alt", start_alt);
        result.put("end_lat", end_lat);
        result.put("end_lng", end_lng);
        result.put("end_alt", end_alt);
        result.put("distance", distance);
        result.put("timestamp", timestamp);
        result.put("timeDiff", timeDiff);

        return result;
    }

    public static LocationRecord fromMap(HashMap<String, Object> map) {
        LocationRecord result = new LocationRecord();

        result.index = FirebaseUtils.getInteger(map, "index", -1);
        result.start_lat = FirebaseUtils.getDouble(map, "start_lat", -1);
        result.start_lng = FirebaseUtils.getDouble(map, "start_lng", -1);
        result.start_alt = FirebaseUtils.getDouble(map, "start_alt", -1);
        result.end_lat = FirebaseUtils.getDouble(map, "end_lat", -1);
        result.end_lng = FirebaseUtils.getDouble(map, "end_lng", -1);
        result.end_alt = FirebaseUtils.getDouble(map, "end_alt", -1);
        result.distance = FirebaseUtils.getDouble(map, "distance", -1);
        result.timestamp = FirebaseUtils.getLong(map, "timestamp", -1);
        result.timeDiff = FirebaseUtils.getLong(map, "timeDiff", -1);

        return result;
    }

    public String getStrData() {
        if (index != -1) {
            return start_lat + ";" + start_lng + ";" + start_alt + ";"
                    + end_lat + ";" + end_lng + ";" + end_alt + ";"
                    + distance + ";" + timestamp + ";" + timeDiff + ";" + index;
        } else {
            return start_lat + ";" + start_lng + ";" + start_alt + ";"
                    + end_lat + ";" + end_lng + ";" + end_alt + ";"
                    + distance + ";" + timestamp + ";" + timeDiff;
        }
    }

    public static LocationRecord fromStr(String str) {
        LocationRecord result = new LocationRecord();

        result.start_lat = FirebaseUtils.getDoubleStr(str, 0, -1);
        result.start_lng = FirebaseUtils.getDoubleStr(str, 1, -1);
        result.start_alt = FirebaseUtils.getDoubleStr(str, 2, -1);
        result.end_lat = FirebaseUtils.getDoubleStr(str, 3, -1);
        result.end_lng = FirebaseUtils.getDoubleStr(str, 4, -1);
        result.end_alt = FirebaseUtils.getDoubleStr(str, 5, -1);
        result.distance = FirebaseUtils.getDoubleStr(str, 6, -1);
        result.timestamp = FirebaseUtils.getLongStr(str, 7, -1);
        result.timeDiff = FirebaseUtils.getLongStr(str, 8, -1);
        result.index = FirebaseUtils.getIntegerStr(str, 9, -1);

        return result;
    }

    public double getSpeed() {
        if (timeDiff != 0 && timeDiff != -1) {
            return distance / (double) timeDiff * 1000;
        }
        return 0;
    }

    public long getPace() {
        if (distance != 0 && distance != -1) {
            return (long) ((double) timeDiff * 1000 / distance);
        }
        return 0;
    }
}
