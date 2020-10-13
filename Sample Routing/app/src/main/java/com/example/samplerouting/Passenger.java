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

public class Passenger extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;

    Button simulate_button, passenger_back_button;

    boolean search_found = false;

    LatLng initial_coords, final_coords, current_coords;

    Marker current_marker, driver_marker = null;
    MarkerOptions driver_marker_options;

    Double latitude_distance, longitude_distance;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("markers");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.passenger_map);
        mapFragment.getMapAsync(this);

        simulate_button = findViewById(R.id.simulate_button);
        passenger_back_button = findViewById(R.id.passenger_back_button);

        passenger_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
                finish();
            }
        });

        simulate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler(Looper.getMainLooper());

                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (!current_coords.equals(initial_coords)) {
                            current_marker.remove();
                        }

                        current_coords = new LatLng(current_coords.latitude + (latitude_distance * 0.1), current_coords.longitude + (longitude_distance * 0.1));

                        current_marker = mMap.addMarker(new MarkerOptions()
                                .position(current_coords)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current_coords, 5));

                        if(!Double.toString(current_coords.latitude).substring(0, 6).equals(Double.toString(final_coords.latitude).substring(0, 6)) &&
                                !Double.toString(current_coords.longitude).substring(0, 6).equals(Double.toString(final_coords.longitude).substring(0, 6))) {
                            handler.postDelayed(this, 2000);
                            search_found = false;
                        } else {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current_coords, 14));

                            Toast.makeText(getApplicationContext(), "Você chegou ao seu destino", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                if (search_found) {
                    handler.postDelayed(runnable, 2000);
                } else {
                    Toast.makeText(getApplicationContext(), "Selecione um pin válido", Toast.LENGTH_LONG).show();
                }
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

        initial_coords = new LatLng(location.getLatitude(), location.getLongitude());
        current_coords = initial_coords;

        mMap.addMarker(new MarkerOptions()
                .position(initial_coords)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(initial_coords, 14));

        markersListener();
    }

    private void markersListener() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    double lat = (double) snapshot.child("driver").child("lat").getValue();
                    double lng = (double) snapshot.child("driver").child("lng").getValue();

                    LatLng driver_position = new LatLng(lat, lng);

                    if (driver_marker != null) {
                        driver_marker.remove();
                    }

                    driver_marker_options = new MarkerOptions();
                    
                    driver_marker_options
                            .title("Posição atual do motorista")
                            .position(driver_position)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

                    driver_marker = mMap.addMarker(driver_marker_options);

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(driver_position, 14));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG).show();
            }
        });
    }
}