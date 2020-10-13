package com.example.samplerouting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class DriverAuth extends FragmentActivity {

    EditText email_signin_input, password_signin_input, name_signup_input, email_signup_input, password_signup_input;
    Button signin_button, signup_button, driver_auth_back_button;
    LinearLayout signin_layout, signup_layout;
    TextView signin_context, signup_context;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_auth);

        email_signin_input = findViewById(R.id.email_signin_input);
        password_signin_input = findViewById(R.id.password_signin_input);

        name_signup_input = findViewById(R.id.name_signup_input);
        email_signup_input = findViewById(R.id.email_signup_input);
        password_signup_input = findViewById(R.id.password_signup_input);

        signin_layout = findViewById(R.id.signin_layout);
        signup_layout = findViewById(R.id.signup_layout);

        signin_context = findViewById(R.id.signin_context);
        signup_context = findViewById(R.id.signup_context);

        signin_button = findViewById(R.id.signin_button);
        signup_button = findViewById(R.id.signup_button);
        driver_auth_back_button = findViewById(R.id.driver_auth_back_button);

        firebaseAuth = FirebaseAuth.getInstance();

        driver_auth_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
                finish();
            }
        });

        signup_context.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_layout.setVisibility(View.VISIBLE);
                signin_context.setVisibility(View.VISIBLE);
                signin_layout.setVisibility(View.GONE);
                signup_context.setVisibility(View.GONE);
            }
        });

        signin_context.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin_layout.setVisibility(View.VISIBLE);
                signup_context.setVisibility(View.VISIBLE);
                signup_layout.setVisibility(View.GONE);
                signin_context.setVisibility(View.GONE);
            }
        });

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_signin_input.getText().toString();
                String password = password_signin_input.getText().toString();

                if (!email.equals("") && !password.equals("")) {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getApplicationContext(), Driver.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Email/senha inválido", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_LONG).show();
                }
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = name_signup_input.getText().toString();
                String email = email_signup_input.getText().toString();
                String password = password_signup_input.getText().toString();

                if (!name.equals("") && !email.equals("") && !password.equals("")) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Conta criada com sucesso", Toast.LENGTH_LONG).show();

                                databaseReference = FirebaseDatabase.getInstance().getReference().child("markers");
                                databaseReference.child(FirebaseAuth.getInstance().getUid()).child("name").setValue(name);

                                signin_layout.setVisibility(View.VISIBLE);
                                signup_context.setVisibility(View.VISIBLE);
                                signup_layout.setVisibility(View.GONE);
                                signin_context.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Erro ao criar conta", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
