/*
Assignment: InClass13
File name: MainActivity.java
Full name:
Akhil Madhamshetty-801165622
Tarun thota-801164383
 */

package com.example.inclass13;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final String TAG = "demo";
    private GoogleMap mMap;
    Locations locations = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle("Paths Activity");

        String json = getJsonFromAssets(this, "trip.json");
//        Log.d(TAG, "JSON: " + json);
        getLocationsFromJson(json);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        ArrayList<LatLng> latLngs = new ArrayList<>();

        PolylineOptions polylineOptions = new PolylineOptions();
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        for (Location location:locations.getPoints()) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            polylineOptions.add(latLng);
            latLngBuilder.include(latLng);
        }
        LatLng sydney = new LatLng(-34, 151);
        Location firstLocation = locations.getPoints().get(0);
        LatLng firstLocationLatLng = new LatLng(firstLocation.getLatitude(),firstLocation.getLongitude());
        Location lastLocation = locations.getPoints().get(locations.getPoints().size()-1);
        LatLng lastLocationLatLng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(firstLocationLatLng))
                        .setTitle("Start Location");
        mMap.addMarker(new MarkerOptions().position(lastLocationLatLng)).setTitle("End Location");


        Polyline polyline = mMap.addPolyline(polylineOptions);
        final LatLngBounds latLngBounds = latLngBuilder.build();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 40));
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 40));
            }
        });

    }
    String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }
    void getLocationsFromJson(String json){
        Gson gson = new Gson();
        locations = gson.fromJson(json, Locations.class);
//        Log.d(TAG, "getLocationsFromJson: " + locations.toString());
    }
}
