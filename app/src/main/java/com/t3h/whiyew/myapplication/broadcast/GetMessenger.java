package com.t3h.whiyew.myapplication.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.activity.HomeApp;

/**
 * Created by Whiyew on 30/03/2017.
 */

public class GetMessenger extends BroadcastReceiver {
    private static final int MY_NOTIFICATION_ID = 1;
    NotificationManager notificationManager;
    Notification myNotification;

    @Override
    public void onReceive(final Context context, Intent intent) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        final DatabaseReference myRef = database.getReference(android_id);
        myRef.child("Messenger").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    if (dataSnapshot.getValue().toString().equals("true") == true) {
                        myRef.child("Messenger").setValue("false", new DatabaseReference.CompletionListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Intent myIntent = new Intent(context, HomeApp.class);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(
                                            context,
                                            0,
                                            myIntent,
                                            0);
                                        myNotification = new NotificationCompat.Builder(context)
                                                .setContentTitle("Bạn có một thông báo!")
                                                .setContentText("Lời mời vào nhóm...")
                                                .setTicker("Thông báo từ ứng dụng....!")
                                                .setWhen(System.currentTimeMillis())
                                                .setContentIntent(pendingIntent)
                                                .setDefaults(Notification.DEFAULT_SOUND)
                                                .setAutoCancel(true)
                                                .setSmallIcon(R.drawable.maps9)
                                                .build();



                                    notificationManager =
                                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(MY_NOTIFICATION_ID, myNotification);

                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
