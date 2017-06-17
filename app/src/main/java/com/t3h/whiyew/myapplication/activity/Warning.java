package com.t3h.whiyew.myapplication.activity;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.abstractclass.WakeLocker;
import com.t3h.whiyew.myapplication.asyntask.MyAsynctask;
import com.t3h.whiyew.myapplication.model.GPSTracker;
import com.t3h.whiyew.myapplication.model.MyLatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.t3h.whiyew.myapplication.R.id.map;

/**
 * Created by Whiyew on 31/03/2017.
 */

public class Warning extends FragmentActivity implements OnMapReadyCallback {
    public static final int WHAT_WAY = 1;
    private GoogleMap mMap;
    LatLng latLng;
    Vibrator vibrator;
    private PowerManager.WakeLock wl;
    Circle circle, circle1;
    private Geocoder geocoder;
    LatLng s, e;
    ValueAnimator vAnimator;
    private Polyline polyline;
    MapView mapView;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.erro);

        relativeLayout = (RelativeLayout) findViewById(R.id.back);
        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.anim_fade);
        relativeLayout.startAnimation(hyperspaceJump);
        WakeLocker.release();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 1000};
        vibrator.vibrate(pattern, 0);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        MapsInitializer.initialize(this);
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                vibrator.cancel();
                relativeLayout.clearAnimation();
                return false;
            }
        });
        geocoder = new Geocoder(this);
        // Add a marker in Sydney and move the camera
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        GPSTracker gpsTracker = new GPSTracker(getBaseContext());
        s = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        Marker you = mMap.addMarker(new MarkerOptions().position(s).title("You"));
        final DatabaseReference myRef = database.getReference(android_id);
        myRef.child("Warn").child("latlng").addListenerForSingleValueEvent(new ValueEventListener() {
            Marker markerName;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                latLng = new LatLng(dataSnapshot.getValue(MyLatLng.class).getLatitude(),
                        dataSnapshot.getValue(MyLatLng.class).getLongitude());

                MyAsynctask myAsynctask = new MyAsynctask(s, latLng, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (WHAT_WAY == msg.what) {
                            ArrayList<LatLng> latLngs = (ArrayList<LatLng>) msg.obj;
                            if (polyline != null) {
                                polyline.remove();
                            }
                            PolylineOptions polylineOptions = new PolylineOptions();
                            polylineOptions.color(Color.RED);
                            polylineOptions.width(5);
                            polylineOptions.addAll(latLngs);
                            polyline = mMap.addPolyline(polylineOptions);
                        }
                    }
                });
                myAsynctask.execute();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory
                        .zoomTo(17));
                if (circle != null) circle.remove();
                if (circle1 != null) circle1.remove();
                if (vAnimator != null) vAnimator.cancel();
                circle1 = mMap.addCircle(new CircleOptions().center(latLng)
                        .strokeColor(Color.RED)
                        .radius(5)
                        .fillColor(Color.RED)
                );
                circle = mMap.addCircle(new CircleOptions().center(latLng)
                        .strokeColor(Color.RED)
                        .radius(10000)
                        .strokeWidth(5).fillColor(0x7f0c001c)
                );
                if (markerName != null) {
                    markerName.remove();
                }
                myRef.child("Warn").child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        markerName = mMap.addMarker(new MarkerOptions().position(latLng).title(dataSnapshot.getValue().toString())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.wa))

                        );
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                vAnimator = new ValueAnimator();
                vAnimator.setRepeatCount(ValueAnimator.INFINITE);
                vAnimator.setRepeatMode(ValueAnimator.RESTART);  /* PULSE */
                vAnimator.setIntValues(0, 100);
                vAnimator.setDuration(2000);
                vAnimator.setEvaluator(new IntEvaluator());
                vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float animatedFraction = valueAnimator.getAnimatedFraction();
                        // Log.e("", "" + animatedFraction);
                        circle.setRadius(animatedFraction * 100);


                    }
                });
                vAnimator.start();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private Marker drawMarker(LatLng latLng, float hue, String title, String snippet) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.title(title);
        options.snippet(snippet);
        options.icon(BitmapDescriptorFactory.defaultMarker(hue));
        return mMap.addMarker(options);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (WHAT_WAY == msg.what) {
                ArrayList<LatLng> latLngs = (ArrayList<LatLng>) msg.obj;
                if (polyline != null) {
                    polyline.remove();
                }
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.RED);
                polylineOptions.width(5);
                polylineOptions.addAll(latLngs);
                polyline = mMap.addPolyline(polylineOptions);
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        vibrator.cancel();
        finish();
    }

    private LatLng getLocationFromName(String name) {
        LatLng latLng = null;
        try {
            List<Address> addresses = geocoder.getFromLocationName(name, 1);
            if (addresses.size() > 0) {
                latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }

}
