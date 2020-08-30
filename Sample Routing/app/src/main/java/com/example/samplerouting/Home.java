package com.example.samplerouting;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Home extends FragmentActivity implements OnMapReadyCallback {

    Geocoder geocoder;
    GoogleMap mMap;

    TextView textView;
    ImageView imageView;
    Button button;

    boolean search_found = false;

    LatLng initial_coords, final_coords, current_coords;
    Marker current_marker;

    Double latitude_distance, longitude_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(this);

        textView = findViewById(R.id.search_text);
        imageView = findViewById(R.id.search_icon);
        button = findViewById(R.id.simulate_button);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textView.getText().equals("")) {
                    List<Address> addresses;
                    try {
                        addresses = geocoder.getFromLocationName(textView.getText().toString(), 1);

                        if (addresses.size() > 0) {
                            double latitude = addresses.get(0).getLatitude();
                            double longitude = addresses.get(0).getLongitude();

                            Toast.makeText(getApplicationContext(), addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();

                            final_coords =  new LatLng(latitude, longitude);

                            latitude_distance = final_coords.latitude - current_coords.latitude;
                            longitude_distance = final_coords.longitude - current_coords.longitude;

                            mMap.addMarker(new MarkerOptions()
                                    .position(final_coords)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(final_coords, 14));

                            search_found = true;
                        } else {
                            Toast.makeText(getApplicationContext(), "Endereço não encontrado", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
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

                        if(!Double.toString(current_coords.latitude).substring(0, 5).equals(Double.toString(final_coords.latitude).substring(0, 5)) &&
                                !Double.toString(current_coords.longitude).substring(0, 5).equals(Double.toString(final_coords.longitude).substring(0, 5))) {
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
                    Toast.makeText(getApplicationContext(), "Selecione um endereço válido", Toast.LENGTH_LONG).show();
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
    }
}