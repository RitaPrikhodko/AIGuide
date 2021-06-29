package com.mproduction.watchplaces.model;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.mproduction.watchplaces.data.LikeData;
import com.mproduction.watchplaces.data.PhotoItem;
import com.mproduction.watchplaces.data.PlaceItem;
import com.mproduction.watchplaces.data.PlaceType;
import com.mproduction.watchplaces.data.ScrimitUser;
import com.mproduction.watchplaces.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class ScrimitDataModel {

    public static ScrimitDataModel sInstance = null;

    public static ScrimitDataModel getInstance(String uid) {
        if (sInstance == null || sInstance.user == null || !sInstance.user.uid.equals(uid)) {
            sInstance = new ScrimitDataModel(uid);
        }

        return sInstance;
    }

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final FirebaseFunctions functions = FirebaseFunctions.getInstance();

    public static void clearInstance() {
        if (sInstance != null && sInstance.connectivityListener != null) {
            FirebaseDatabase.getInstance().getReference(".info/connected").removeEventListener(sInstance.connectivityListener);
            sInstance.connectivityListener = null;
        }
        if (sInstance != null && sInstance.itemsListener != null) {
            FirebaseDatabase.getInstance().getReference("placeItem").removeEventListener(sInstance.itemsListener);
            sInstance.itemsListener = null;
        }
        if (sInstance != null && sInstance.typesListener != null) {
            FirebaseDatabase.getInstance().getReference("placeType").removeEventListener(sInstance.typesListener);
            sInstance.typesListener = null;
        }
        if (sInstance != null && sInstance.photoListener != null) {
            FirebaseDatabase.getInstance().getReference("photoItems").removeEventListener(sInstance.photoListener);
            sInstance.photoListener = null;
        }
        sInstance = null;
    }

    public static ScrimitDataModel getInstance() {
        return sInstance;
    }

    public ScrimitUser user;
    public ArrayList<PhotoItem> photoItems;
    public ArrayList<PlaceType> placeTypes;
    public ArrayList<PlaceItem> placeItems;
    public ArrayList<LikeData> likeDataList;

    public boolean isConnected = false;
    private ValueEventListener connectivityListener;
    private ValueEventListener itemsListener;
    private ValueEventListener typesListener;
    private ValueEventListener photoListener;
    private ValueEventListener likeListener;

    public ScrimitDataModel(String uid) {
        user = new ScrimitUser(uid);
        photoItems = new ArrayList<>();
        placeTypes = new ArrayList<>();
        placeItems = new ArrayList<>();
        likeDataList = new ArrayList<>();

        connectivityListener = database.getReference(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isConnected = snapshot.getValue(Boolean.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isConnected = false;
            }
        });

    }

    public void listenData() {
        itemsListener = database.getReference("placeItem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, Object> users = (HashMap<String, Object>) dataSnapshot.getValue();
                    placeItems = new ArrayList<>();
                    if (users != null) {
                        for (Object user_item : users.values()) {
                            PlaceItem item = PlaceItem.fromMap((HashMap<String, Object>) user_item);

                            placeItems.add(item);
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String desc = error.getDetails();
                String msg = error.getMessage();
            }
        });
        typesListener = database.getReference("placeType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, Object> users = (HashMap<String, Object>) dataSnapshot.getValue();
                    placeTypes = new ArrayList<>();
                    if (users != null) {
                        for (Object user_item : users.values()) {
                            PlaceType item = PlaceType.fromMap((HashMap<String, Object>) user_item);

                            placeTypes.add(item);
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        photoListener = database.getReference("photoItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, Object> users = (HashMap<String, Object>) dataSnapshot.getValue();
                    photoItems = new ArrayList<>();
                    if (users != null) {
                        for (Object user_item : users.values()) {
                            PhotoItem item = PhotoItem.fromMap((HashMap<String, Object>) user_item);

                            photoItems.add(item);
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        likeListener = database.getReference("likeData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, Object> users = (HashMap<String, Object>) dataSnapshot.getValue();
                    likeDataList = new ArrayList<>();
                    if (users != null) {
                        for (Object user_item : users.values()) {
                            LikeData item = LikeData.fromMap((HashMap<String, Object>) user_item);

                            likeDataList.add(item);
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LikeData getLikeData(String uid, String placeItemId, String placeTypeId) {
        for (LikeData item : likeDataList) {
            if (item.uid.equals(uid) && ((placeItemId != null && placeItemId.equals(item.placeItemId)) ||
                    (placeTypeId != null && placeTypeId.equals(item.placeTypeId)))) {
                return item;
            }
        }
        LikeData data = new LikeData();
        data.uid = uid;
        data.placeItemId = placeItemId;
        data.placeTypeId = placeTypeId;
        return data;
    }

    public void saveLikeData(final LikeData target, final FirebaseDatabaseListener listener) {
        if (target.id == null) {
            target.id = database.getReference("likeData").push().getKey();
        }
        database.getReference("likeData").child(target.id).setValue(target.getDataMap(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    listener.onSuccess();
                } else {
                    listener.onFailure(databaseError);
                }
            }
        });
    }

    public PlaceItem getPlaceItem(String place_id) {
        for (PlaceItem item : placeItems) {
            if (item.place_id.equals(place_id)) {
                return item;
            }
        }
        return null;
    }

    public PlaceType getPlaceType(Place place) {
        for (PlaceType item : placeTypes) {
            if (place.getTypes().contains(item.getPlaceType())) {
                return item;
            }
        }
        return null;
    }

    public double getDistance(Place place, String placeId) {
        LatLng placePoint = place.getLatLng();
        PlaceItem item = getPlaceItem(placeId);

        if (placePoint != null && item != null && item.lat > 0) {
            return Utils.distance(
                    placePoint.latitude, placePoint.longitude, item.lat, item.lng);
        }

        return -1;
    }

    public PlaceItem getPlaceItem(Place place) {
        if (getPlaceItem(place.getId()) != null) {
            return getPlaceItem(place.getId());
        }
        for (PlaceItem item : placeItems) {
            double distance = getDistance(place, item.id);
            if (distance > 0 && distance <= 10) {
                return item;
            }
        }
        return null;
    }

    public ArrayList<PhotoItem> getPhotoItems(Place place) {
        String id = place.getId();
        ArrayList<PhotoItem> result = new ArrayList<>();

        PlaceItem item = getPlaceItem(place);
        if (item != null) {
            for (PhotoItem photoItem: photoItems) {
                if (photoItem.placeItemCode != null && photoItem.placeItemCode.equals(id)) {
                    result.add(photoItem);
                }
            }
            return result;
        }
        PlaceType type = getPlaceType(place);
        if (type != null) {
            for (PhotoItem photoItem: photoItems) {
                if (photoItem.placeTypeCode != null && photoItem.placeTypeCode.equals(type.typeCode)) {
                    result.add(photoItem);
                }
            }
            return result;
        }
        return result;
    }
}
