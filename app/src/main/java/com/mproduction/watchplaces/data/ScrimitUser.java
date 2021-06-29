package com.mproduction.watchplaces.data;

import java.util.HashMap;

public class ScrimitUser {
    public String uid;
    public String username;
    public String email;
    public String location;
    public String photoUri;

    public String type;

    public ScrimitUser(String uid) {
        this.uid = uid;
        type = "player";

    }

    public HashMap<String, Object> getDataMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("uid", uid);
        result.put("username", username);
        result.put("email", email);
        result.put("location", location);
        result.put("photoUri", photoUri);
        result.put("type", type);

        return result;
    }

    public static ScrimitUser fromMap(HashMap<String, Object> map) {
        String uid = (String) map.get("uid");
        ScrimitUser result = new ScrimitUser(uid);

        result.username = (String) map.get("username");
        result.email = (String) map.get("email");
        result.location = (String) map.get("location");
        result.photoUri = (String) map.get("photoUri");
        result.type = (String) map.get("type");

        return result;
    }

}
