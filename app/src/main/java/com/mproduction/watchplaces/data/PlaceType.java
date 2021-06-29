package com.mproduction.watchplaces.data;

import com.google.android.libraries.places.api.model.Place;
import com.mproduction.watchplaces.utils.FirebaseUtils;

import java.util.HashMap;

public class PlaceType {
    public String id;
    public String title;

    public String typeCode;

    public long timestamp;

    public HashMap<String, Object> getDataMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("id", id);
        result.put("title", title);
        result.put("typeCode", typeCode);
        result.put("timestamp", timestamp);

        return result;
    }

    public static PlaceType fromMap(HashMap<String, Object> map) {
        PlaceType result = new PlaceType();

        result.id = (String) map.get("id");
        result.title = (String) map.get("title");
        result.typeCode = (String) map.get("typeCode");
        result.timestamp = FirebaseUtils.getLong(map, "timestamp", -1);

        return result;
    }

    public Place.Type getPlaceType() {
        return Place.Type.valueOf(typeCode);
    }
}
