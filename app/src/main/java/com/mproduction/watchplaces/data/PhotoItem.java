package com.mproduction.watchplaces.data;

import com.mproduction.watchplaces.utils.FirebaseUtils;

import java.util.HashMap;

public class PhotoItem {
    public String id;
    public String title;

    public String placeTypeId;
    public String placeTypeCode;
    public String placeItemId;
    public String placeItemCode;
    public String thumbUri;

    public String description;

    public long timestamp;

    public HashMap<String, Object> getDataMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("id", id);
        result.put("title", title);
        result.put("placeTypeId", placeTypeId);
        result.put("placeTypeCode", placeTypeCode);
        result.put("placeItemId", placeItemId);
        result.put("placeItemCode", placeItemCode);
        result.put("thumbUri", thumbUri);
        result.put("timestamp", timestamp);

        result.put("description", description);

        return result;
    }

    public static PhotoItem fromMap(HashMap<String, Object> map) {
        PhotoItem result = new PhotoItem();

        result.id = (String) map.get("id");
        result.title = (String) map.get("title");
        result.placeTypeId = (String) map.get("placeTypeId");
        result.placeTypeCode = (String) map.get("placeTypeCode");
        result.placeItemId = (String) map.get("placeItemId");
        result.placeItemCode = (String) map.get("placeItemCode");
        result.thumbUri = (String) map.get("thumbUri");
        result.timestamp = FirebaseUtils.getLong(map, "timestamp", -1);

        result.description = (String) map.get("description");

        return result;
    }
}
