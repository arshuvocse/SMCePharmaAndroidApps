package com.creatrix.salessolution.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;

import com.creatrix.salessolution.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
public class TestingPurposeActivity extends AppCompatActivity
        implements
        OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_testing_purpose);

//        // Get the SupportMapFragment and request notification when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Polyline path = googleMap.addPolyline(new PolylineOptions()
                .add(
                        new LatLng(23.707069, 90.422613),
                        new LatLng(23.723859, 90.414583),
                        new LatLng(23.743109, 90.415183),
                        new LatLng(23.759869, 90.393378),
                        new LatLng(23.787780,90.396497),
                        new LatLng(23.798617,90.370433)

                )
        );

        // Style the polyline
        path.setWidth(10);
        path.setColor(Color.parseColor("#FF0000"));

        // Position the map's camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.707069, 90.422613), 10));
    }
}