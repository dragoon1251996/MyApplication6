package com.t3h.whiyew.myapplication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.t3h.whiyew.myapplication.model.GPSTracker;


/**
 * Created by Whiyew on 26/03/2017.
 */

public class ReService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GPSTracker gpsTracker = new GPSTracker(context);
        LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DatabaseReference myRef = database.getReference(android_id);
        myRef.child("LatLng").setValue(latLng);
//        Intent background = new Intent(context, UpdateMarker.class);
//        context.startService(background);
    }
}
