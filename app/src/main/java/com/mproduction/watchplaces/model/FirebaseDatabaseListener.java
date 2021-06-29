package com.mproduction.watchplaces.model;

import com.google.firebase.database.DatabaseError;

public interface FirebaseDatabaseListener {
    public void onSuccess();
    public void onFailure(DatabaseError error);
}
