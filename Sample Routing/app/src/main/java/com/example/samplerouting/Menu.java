package com.example.samplerouting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

public class Menu extends FragmentActivity {

    Button driver_button, passenger_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        driver_button = findViewById(R.id.driver_button);
        passenger_button = findViewById(R.id.passenger_button);

        driver_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DriverAuth.class);
                startActivity(intent);
                finish();
            }
        });

        passenger_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Passenger.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
