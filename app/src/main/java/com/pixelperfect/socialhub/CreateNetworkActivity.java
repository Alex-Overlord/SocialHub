package com.pixelperfect.socialhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pixelperfect.socialhub.models.Network;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class CreateNetworkActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private EditText editTextName, editTextDescription;
    private Button validateNetwork;
    private ProgressBar progressBar;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private String saveCurrentDate, saveCurrentTime, randomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_network);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Create Network");

        editTextName = findViewById(R.id.networkName);
        editTextDescription = findViewById(R.id.networkDescription);

        validateNetwork = findViewById(R.id.validateNetwork);
        validateNetwork.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        // à enlever pour le rendu !!
        editTextName.setText("Mon réseau");
        editTextDescription.setText("Un super réseau où parler avec ces amis");

        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.validateNetwork:
                createNetwork();
                startActivity(new Intent(this, NetworkActivity.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        if (id == R.id.action_user) {
            startActivity(new Intent(this, ProfileActivity.class));
        }

        if (id == R.id.action_networks) {
            startActivity(new Intent(this, NetworksListActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(CreateNetworkActivity.this, LoginActivity.class));
    }

    public void createNetwork() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("Name is required!");
            editTextName.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            editTextDescription.setError("Description is required!");
            editTextDescription.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calDate.getTime());

        Calendar calTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calTime.getTime());

        randomName = saveCurrentDate + saveCurrentTime;


//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//                        if (user.isEmailVerified()) {
//
//                            // redirect to user profile
//                            startActivity(new Intent(MainActivity.this
//                                    , NetworksActivity.class));
//                        } else {
//                            user.sendEmailVerification();
//                            Toast.makeText(MainActivity.this
//                                    , "Check your email to verify your account!"
//                                    , Toast.LENGTH_LONG).show();
//                        }
//                    } else {
//                        Toast.makeText(MainActivity.this
//                                , "Failed to login! Please check your credentials"
//                                , Toast.LENGTH_LONG).show();
//                    }
//                });
        progressBar.setVisibility(View.GONE);
    }

}