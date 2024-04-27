package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button createAccountButton;
    private Button resetPasswordButton;
    private Button login;
     private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        createAccountButton = findViewById(R.id.buttonCreateAccount);
        resetPasswordButton = findViewById(R.id.buttonResetPassword);
        login=findViewById(R.id.buttonlogin);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Basic validation
                if(email.isEmpty() || !email.contains("@")) {
                    editTextEmail.setError("Valid email required");
                    editTextEmail.requestFocus();
                    return;
                }
                if(password.isEmpty() || password.length() < 6) {
                    editTextPassword.setError("Password must be at least 6 characters");
                    editTextPassword.requestFocus();
                    return;
                }

                // Create user
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // User is successfully registered and logged in
                                // Start the new activity here or show message to user
                                Toast.makeText(MainActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Error occurred
                                if (task.getException() != null) {
                                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Input validation
                if(email.isEmpty() || !email.contains("@")) {
                    editTextEmail.setError("Valid email required");
                    editTextEmail.requestFocus();
                    return;
                }
                if(password.isEmpty() || password.length() < 6) {
                    editTextPassword.setError("Password must be at least 6 characters");
                    editTextPassword.requestFocus();
                    return;
                }

                // Sign in with email and password
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, GrilleProduitsActivity.class);
                                startActivity(intent);
                            } else {
                                // Handle failures
                                if (task.getException() != null) {
                                    Toast.makeText(MainActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();

                if(email.isEmpty() || !email.contains("@")) {
                    editTextEmail.setError("Une adresse email valide est requise");
                    editTextEmail.requestFocus();
                    return;
                }

                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Instructions de réinitialisation envoyées à votre adresse email", Toast.LENGTH_SHORT).show();
                            } else {
                                // Error handling
                                if (task.getException() != null) {
                                    Toast.makeText(MainActivity.this, "Échec de l'envoi : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Échec de l'envoi. Veuillez réessayer.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


    }

}