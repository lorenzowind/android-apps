package com.example.samplerouting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

public class DriverAuth extends FragmentActivity {

    EditText email_input, password_input;
    Button login_button, driver_auth_back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_auth);

        email_input = findViewById(R.id.email_input);
        password_input = findViewById(R.id.password_input);

        login_button = findViewById(R.id.login_button);
        driver_auth_back_button = findViewById(R.id.driver_auth_back_button);

        driver_auth_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
                finish();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Driver.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
