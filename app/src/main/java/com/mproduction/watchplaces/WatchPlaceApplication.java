package com.mproduction.watchplaces;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class WatchPlaceApplication extends Application {

    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }

}
