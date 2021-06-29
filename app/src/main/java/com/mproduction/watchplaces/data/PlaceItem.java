package com.mproduction.watchplaces.data;

import com.mproduction.watchplaces.utils.FirebaseUtils;

import java.util.HashMap;

public class PlaceItem {
    public String id;
    public String label;

    public String place_id;
    public String types;

    public String description;

    public long timestamp;

    public double lat;
    public double lng;

    public HashMap<String, Object> getDataMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("id", id);
        result.put("label", label);
        result.put("place_id", place_id);
        result.put("types", types);
        result.put("timestamp", timestamp);
        result.put("lat", lat);
        result.put("lng", lng);

        result.put("description", description);

        return result;
    }

    public static PlaceItem fromMap(HashMap<String, Object> map) {
        PlaceItem result = new PlaceItem();

        result.id = (String) map.get("id");
        result.label = (String) map.get("label");
        result.place_id = (String) map.get("place_id");
        result.types = (String) map.get("types");
        result.timestamp = FirebaseUtils.getLong(map, "timestamp", -1);
        result.lat = FirebaseUtils.getDouble(map, "lat", 0);
        result.lng = FirebaseUtils.getDouble(map, "lng", 0);

        result.description = (String) map.get("description");

        return result;
    }
}
