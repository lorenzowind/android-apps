package com.example.samplerouting;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Passenger extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;

    Button passenger_back_button;

    LatLng passenger_coords;

    List<Marker> driver_markers = null;
    MarkerOptions driver_marker_options;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("markers");
        driver_markers = new ArrayList<Marker>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.passenger_map);
        mapFragment.getMapAsync(this);

        passenger_back_button = findViewById(R.id.passenger_back_button);

        passenger_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        passenger_coords = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.addMarker(new MarkerOptions()
                .title("Posição atual")
                .position(passenger_coords)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(passenger_coords, 14));

        markersListener();
    }

    private void markersListener() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (Marker marker: driver_markers) {
                        marker.remove();
                    }

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                        double lat = (double) dataSnapshot.child("lat").getValue();
                        double lng = (double) dataSnapshot.child("lng").getValue();

                        LatLng driver_position = new LatLng(lat, lng);

                        driver_marker_options = new MarkerOptions();

                        driver_marker_options
                                .title("Posição atual do motorista " + dataSnapshot.child("name").getValue())
                                .position(driver_position)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

                        Marker marker = mMap.addMarker(driver_marker_options);

                        driver_markers.add(marker);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG).show();
            }
        });
    }
}