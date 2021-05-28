package com.pixelperfect.socialhub.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.models.Network;
import com.pixelperfect.socialhub.models.User;

import java.util.Objects;
import java.util.UUID;

public class CreateNetworkActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private EditText editTextName, editTextDescription;
    private Button validateNetwork;
    private ProgressBar progressBar;
    private Spinner spinner;
    private String type;

    private FirebaseUser user;
    private DatabaseReference referenceUsers, referenceNetworks;

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
        referenceUsers = FirebaseDatabase.getInstance().getReference("Users");
        referenceNetworks = FirebaseDatabase.getInstance().getReference("Networks");

        // à enlever pour le rendu !!
//        editTextName.setText("Mon réseau");
//        editTextDescription.setText("Un super réseau où parler avec ces amis");

        progressBar = findViewById(R.id.progressBar);

        spinner = findViewById(R.id.network_types_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.network_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.validateNetwork:
                createNetwork();
                startActivity(new Intent(this, NetworksListActivity.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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

        Network network = new Network(UUID.randomUUID().toString(), name, description, type);
        User insertedUser = new User(user.getUid(), user.getDisplayName(), user.getEmail());

        network.addUser(user.getUid(), insertedUser);
        network.addAdmin(user.getUid(), insertedUser);
        referenceNetworks.push().setValue(network);
        network.setId(referenceNetworks.getKey());

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}