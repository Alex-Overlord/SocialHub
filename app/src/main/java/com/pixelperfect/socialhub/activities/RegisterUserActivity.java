package com.pixelperfect.socialhub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.models.User;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView registerUser;
    private EditText editTextFullName, editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private ImageView banner;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName = findViewById(R.id.fullName);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String fullName = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (fullName.isEmpty()) {
            editTextFullName.setError("Full name is required!");
            editTextFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        User user = new User(mAuth.getCurrentUser().getUid(), fullName, email);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest
                                    .Builder()
                                    .setDisplayName(fullName).build();
                                firebaseUser.updateProfile(profileChangeRequest);

                                Toast.makeText(RegisterUserActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(this, LoginActivity.class));
                            } else {
                                Toast.makeText(RegisterUserActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterUserActivity.this, "Failed to register!", Toast.LENGTH_LONG).show();
                    }
                });
        progressBar.setVisibility(View.GONE);
    }
}