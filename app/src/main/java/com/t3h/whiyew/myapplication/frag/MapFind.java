package com.t3h.whiyew.myapplication.frag;

import android.Manifest;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.model.GPSTracker;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;


/**
 * Created by Whiyew on 19/04/2017.
 */

public class MapFind extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final int REQUEST_PERMISTION = 1;
    public static final int WHAT_WAY = 1;
    EditText to;
    Button go;
    private Polyline polyline;
    private Geocoder geocoder;
    private Marker markerStart;
    private Marker markerEnd;
    GoogleMap map;
    MapView mapView;
    View view;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    LatLng latLng;
    ArrayList<String> listName;
    ArrayList<String> listGroup;
    String[] arrid;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    boolean aBoolean = true;
    ArrayList seletedItems;
    ValueAnimator vAnimator;
    Circle circle, circle1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.map, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;
        geocoder = new Geocoder(getActivity());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setOnMarkerClickListener(this);
        GPSTracker gpsTracker = new GPSTracker(getContext());
        LatLng lat = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(lat));
        map.animateCamera(CameraUpdateFactory.zoomTo(14));
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                final CharSequence[] items = {" Easy ", " Medium ", " Hard ", " Very Hard ",
                        " Easy ", " Medium ", " Hard ", " Very Hard ", " Easy ", " Medium ",
                        " Hard ", " Very Hard ", " Easy ", " Medium ", " Hard ", " Very Hard "};
// arraylist to keep the selected items
                Load(latLng);
            }
        });
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
        } else {
            map.setMyLocationEnabled(true);
        }


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(11));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        map.setMyLocationEnabled(true);
                    }
                } else {
                }
                return;
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(mCurrLocationMarker)) {

        }
        return true;
    }

    public void Load(LatLng latLng) {
        new Load(getActivity(),latLng).execute();
    }

    class Load extends AsyncTask<Void, Void, Void> {
        Activity mContex;
        LatLng latLng;
        public Load(Activity mContex,LatLng latLng) {
            this.mContex = mContex;
            this.latLng=latLng;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            boolean cher = true;
            final String android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference(android_id);
            listGroup = new ArrayList<>();
            listName = new ArrayList<>();
            myRef.child("Group").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listGroup.add(dataSnapshot.getKey().toString());
                    listName.add(dataSnapshot.getKey().toString().split(" ")[0]);
                    aBoolean = false;
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
            while (cher == true) {
                try {
                    Thread.sleep(50);
                    if (aBoolean == false) {
                        break;
                    }
                } catch (InterruptedException e) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            arrid = listName.toArray(new String[0]);
            seletedItems = new ArrayList();

            AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.dialog)
                    .setTitle("Select Group...")
                    .setMultiChoiceItems(arrid, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                            if (isChecked) {
                                // If the user checked the item, add it to the selected items
                                seletedItems.add(indexSelected);
                            } else if (seletedItems.contains(indexSelected)) {
                                // Else, if the item is already in the array, remove it
                                seletedItems.remove(Integer.valueOf(indexSelected));
                            }
                        }
                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //  Your code when user clicked on OK
                            //  You can write the code  to save the selected item here
                            if(seletedItems.size()>0){
                                Vibrator vibrator = (Vibrator) mContex.getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(500);
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
                            }
                            final String android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            for (int i = 0; i < seletedItems.size(); i++) {
                                final FirebaseDatabase data = FirebaseDatabase.getInstance();
                                final DatabaseReference myRef = data.getReference("Groups");
                                final int finalI = i;
                                myRef.child(listGroup.get((Integer) seletedItems.get(i))).child("NameOfGroup").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        // arrayId.add(dataSnapshot.getValue().toString());
                                        DatabaseReference myRef1 = data.getReference(dataSnapshot.getValue().toString());
                                        // Toast.makeText(getBaseContext(),dataSnapshot.getKey().toString(),Toast.LENGTH_LONG).show();
                                        if(dataSnapshot.getValue().toString().equals(android_id) == false){
                                            DatabaseReference warn = data.getReference(dataSnapshot.getValue().toString());
                                            warn.child("Warn").child("latlng").setValue(latLng);
                                            warn.child("Warn").child("check").setValue("true");
                                            String []name=listGroup.get((Integer) seletedItems.get(finalI)).split(" ");
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
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //  Your code when user clicked on Cancel
                        }
                    }).create();
            dialog.show();
        }
    }


}