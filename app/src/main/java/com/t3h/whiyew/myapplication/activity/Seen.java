package com.t3h.whiyew.myapplication.activity;

import android.Manifest;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.frag.ManagerGroup;
import com.t3h.whiyew.myapplication.model.Member;
import com.t3h.whiyew.myapplication.model.MyLatLng;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Whiyew on 14/06/2017.
 */

public class Seen extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener  {
    GoogleMap map;
    MapView mapView;
    View view;
    Marker warnningMarker;
    boolean check4 = true;
    LatLng myLatlng;
    int temp;
    Circle circle, circle1;
    ValueAnimator vAnimator;
    String namegroupcheck;
    private ArrayList<String> ID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        namegroupcheck = intent.getStringExtra(ManagerGroup.KEY_STRING);
        String []name=namegroupcheck.split(" ");
        setTitle(name[0]);
        mapView = (MapView) findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        map = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setOnMarkerClickListener(this);
        mapView.setClickable(true);
        map.animateCamera(CameraUpdateFactory.zoomTo(11));
        final String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                Vibrator vibrator = (Vibrator) getApplication().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
//                Toast.makeText(getContext(),ManagerGroup.nameOfGroupClick,Toast.LENGTH_LONG).show();
                final FirebaseDatabase data = FirebaseDatabase.getInstance();
                //  map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                if (map.getCameraPosition().zoom != 17) {
                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    map.animateCamera(CameraUpdateFactory
                            .zoomTo(17));
                } else {
                    map.animateCamera(CameraUpdateFactory
                            .zoomTo(17));
                }
                if (circle != null) circle.remove();
                if (circle1 != null) circle1.remove();
                if (vAnimator != null) vAnimator.cancel();
                circle1 = map.addCircle(new CircleOptions().center(latLng)
                        .strokeColor(Color.RED)
                        .radius(5)
                        .fillColor(Color.RED)
                );
                circle = map.addCircle(new CircleOptions().center(latLng)
                        .strokeColor(Color.RED)
                        .radius(10000)
                        .strokeWidth(5).fillColor(0x7f0c001c)
                );
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
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Groups");
                myRef.child(namegroupcheck).child("NameOfGroup").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        // arrayId.add(dataSnapshot.getValue().toString());
                        DatabaseReference myRef1 = database.getReference(dataSnapshot.getValue().toString());
                       // Toast.makeText(getBaseContext(),dataSnapshot.getKey().toString(),Toast.LENGTH_LONG).show();
                        if(dataSnapshot.getValue().toString().equals(android_id) == false){
                            DatabaseReference warn = data.getReference(dataSnapshot.getValue().toString());
                            warn.child("Warn").child("latlng").setValue(latLng);
                            warn.child("Warn").child("check").setValue("true");
                            String []name=namegroupcheck.split(" ");
                            warn.child("Warn").child("name").setValue(name[0]);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Groups");
        myRef.child(namegroupcheck).child("NameOfGroup").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().toString().equals(android_id) == false){
                    final DatabaseReference myRef = database.getReference(dataSnapshot.getKey().toString());
                    myRef.child("LatLng").addValueEventListener(new ValueEventListener() {
                        Marker markerName;
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            final LatLng latLng = new LatLng(dataSnapshot.getValue(MyLatLng.class).getLatitude(),
                                    dataSnapshot.getValue(MyLatLng.class).getLongitude());

                            if (markerName != null) {
                                markerName.remove();
                            }
                            myRef.child("Intofmation").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Member member;
                                    if (dataSnapshot.getValue(Member.class) == null) {
                                        member = new Member(ID.get(temp), ID.get(temp) + ".com", "0", "English", "Việt Nam");
                                    } else {
                                        member = dataSnapshot.getValue(Member.class);
                                    }
                                    markerName = map.addMarker(new MarkerOptions().position(latLng).title(member.getName()));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                     //       markerName = map.addMarker(new MarkerOptions().position(latLng).title("Vương"));

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    final DatabaseReference myRef = database.getReference(dataSnapshot.getKey().toString());
                    myRef.child("LatLng").addValueEventListener(new ValueEventListener() {
                        Marker markerName;
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            final LatLng latLng = new LatLng(dataSnapshot.getValue(MyLatLng.class).getLatitude(),
                                    dataSnapshot.getValue(MyLatLng.class).getLongitude());

                            if (markerName != null) {
                                markerName.remove();
                            }
//                            markerName = map.addMarker(new MarkerOptions().position(latLng).title("Dũng").icon(BitmapDescriptorFactory
//                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            myRef.child("Intofmation").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Member member;
                                    if (dataSnapshot.getValue(Member.class) == null) {
                                        member = new Member(ID.get(temp), ID.get(temp) + ".com", "0", "English", "Việt Nam");
                                    } else {
                                        member = dataSnapshot.getValue(Member.class);
                                    }
                                    markerName = map.addMarker(new MarkerOptions().position(latLng).title(member.getName()).icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            if (temp == 0) {
                                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                temp = 1;
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//
//        for (int i = 0; i < ManagerGroup.arrayId.size(); i++) {
//            if (ManagerGroup.arrayId.get(i).equals(android_id) == false) {
//                DatabaseReference myRef = database.getReference(ManagerGroup.arrayId.get(i));
//                {
//                    myRef.child("LatLng").addValueEventListener(new ValueEventListener() {
//                        Marker markerName;
//
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            LatLng latLng = new LatLng(dataSnapshot.getValue(MyLatLng.class).getLatitude(),
//                                    dataSnapshot.getValue(MyLatLng.class).getLongitude());
//
//                            if (markerName != null) {
//                                markerName.remove();
//                            }
//                            markerName = map.addMarker(new MarkerOptions().position(latLng).title("Vương"));
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            } else {
//                DatabaseReference myRef = database.getReference(ManagerGroup.arrayId.get(i));
//                {
//                    myRef.child("LatLng").addValueEventListener(new ValueEventListener() {
//                        Marker markerName;
//
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            LatLng latLng = new LatLng(dataSnapshot.getValue(MyLatLng.class).getLatitude(),
//                                    dataSnapshot.getValue(MyLatLng.class).getLongitude());
//
//                            if (markerName != null) {
//                                markerName.remove();
//                            }
//                            markerName = map.addMarker(new MarkerOptions().position(latLng).title("Dũng").icon(BitmapDescriptorFactory
//                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//                            if (temp == 0) {
//                                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                                temp = 1;
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//
//            }
//        }
        if (Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            }
        } else {
            map.setMyLocationEnabled(true);
        }

    }
    @Override
    public boolean onMarkerClick(Marker marker) {

        marker.showInfoWindow();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
