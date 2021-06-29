package com.mproduction.watchplaces.data;

import com.google.firebase.database.ServerValue;
import com.mproduction.watchplaces.utils.FirebaseUtils;

import java.util.HashMap;

public class LikeData {
    public String id;
    public String uid;

    public String placeTypeId;
    public String placeItemId;

    public HashMap<String, Boolean> likeMap = new HashMap<>();

    public long timestamp;

    public HashMap<String, Object> getDataMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("id", id);
        result.put("uid", uid);
        result.put("placeTypeId", placeTypeId);
        result.put("placeItemId", placeItemId);
        result.put("timestamp", ServerValue.TIMESTAMP);
        result.put("likeMap", likeMap);

        return result;
    }

    public static LikeData fromMap(HashMap<String, Object> map) {
        LikeData result = new LikeData();

        result.id = (String) map.get("id");
        result.uid = (String) map.get("uid");
        result.placeTypeId = (String) map.get("placeTypeId");
        result.placeItemId = (String) map.get("placeItemId");
        result.timestamp = FirebaseUtils.getLong(map, "timestamp", -1);
        result.likeMap = FirebaseUtils.getStrBooleanList(map, "likeMap");

        return result;
    }
}
