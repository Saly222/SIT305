package com.example.lostfound;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LostFoundItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        items = getIntent().getParcelableArrayListExtra("items");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        if (items == null || items.isEmpty()) return;

        for (LostFoundItem item : items) {
            LatLng position = new LatLng(item.getLatitude(), item.getLongitude());
            String markerTitle = item.getTitle() + " (" + item.getStatus() + ")";

            float markerColor;
            if ("lost".equalsIgnoreCase(item.getStatus())) {
                markerColor = BitmapDescriptorFactory.HUE_RED;  // Red for lost
            } else if ("found".equalsIgnoreCase(item.getStatus())) {
                markerColor = BitmapDescriptorFactory.HUE_GREEN;  // Green for found
            } else {
                markerColor = BitmapDescriptorFactory.HUE_ORANGE; // Default/other color
            }

            mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(markerTitle)
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
        }
        LatLng first = new LatLng(items.get(0).getLatitude(), items.get(0).getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(first, 10f));
    }
}
