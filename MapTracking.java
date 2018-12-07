package com.gawe.tpkom;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.gawe.tpkom.Model.Tracking;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class MapTracking extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String email;

    DatabaseReference locations;

    Double lat,lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_tracking);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Ref ke firebase
        locations = FirebaseDatabase.getInstance().getReference("locations");

        //Get intent
        if (getIntent() !=null)
        {
            email = getIntent().getStringExtra("email");
            lat = getIntent().getDoubleExtra("lat",46.422);
            lng = getIntent().getDoubleExtra("lng",-124.084);
        }
        if(!TextUtils.isEmpty(email))
            loadLocationForThisUser(email);
    }

    private void loadLocationForThisUser(String email) {
        Query user_location = locations.orderByChild("email").equalTo(email);
        user_location.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    Tracking tracking = postSnapShot.getValue(Tracking.class);

                    //Marker untuk lokasi teman
                    LatLng friendLocation = new LatLng(Double.parseDouble(tracking.getLat()),
                                                        Double.parseDouble(tracking.getLng()));

                    //Marker lokasi kita
                    Location currentUser = new Location("");
                    currentUser.setLatitude(lat);
                    currentUser.setLongitude(lng);

                    //Location dari koordinat teman
                    Location friend = new Location("");
                    friend.setLatitude(Double.parseDouble(tracking.getLat()));
                    friend.setLongitude(Double.parseDouble(tracking.getLng()));

                    distance(currentUser,friend);

                    //Fungsi Clear old markers
                    mMap.clear();



                    //Add friend marker on map
                    mMap.addMarker(new MarkerOptions()
                                .position(friendLocation)
                                .title(tracking.getEmail())
                                .title(tracking.getNama())
                                .snippet("Distance" +new DecimalFormat("#.#").format((currentUser.distanceTo(friend)) )+ "Meter")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),15.0f));



                }
                //Marker utk current user
                LatLng current = new LatLng(lat,lng);
                mMap.addMarker(new MarkerOptions().position(current).title(FirebaseAuth.getInstance().getCurrentUser().getEmail()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private double distance(Location currentUser, Location friend) {
        double theta = currentUser.getLongitude() - friend.getLongitude();
        double dist = Math.sin(deg2rad(currentUser.getLatitude()))
                * Math.sin(deg2rad(friend.getLatitude()))
                * Math.cos(deg2rad(currentUser.getLatitude()))
                * Math.cos(deg2rad(friend.getLatitude()))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
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
        mMap = googleMap;


    }
}
