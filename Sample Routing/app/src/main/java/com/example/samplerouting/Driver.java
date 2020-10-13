package com.example.samplerouting;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

public class Driver extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;

    Button driver_back_button;

    Marker current_marker = null;
    MarkerOptions current_marker_options;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("markers");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.driver_map);
        mapFragment.getMapAsync(this);

        driver_back_button = findViewById(R.id.driver_back_button);

        driver_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

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

        LatLng initial_coords = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.addMarker(new MarkerOptions()
                .title("Posição atual")
                .position(initial_coords)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(initial_coords, 14));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (current_marker != null) {
                    current_marker.remove();
                }

                current_marker_options = new MarkerOptions();

                current_marker_options
                        .title("Posição alternativa")
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

                current_marker = mMap.addMarker(current_marker_options);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

                String driver_uid = FirebaseAuth.getInstance().getUid();

                databaseReference.child(driver_uid).child("lat").setValue(latLng.latitude);
                databaseReference.child(driver_uid).child("lng").setValue(latLng.longitude);
            }
        });
    }
}