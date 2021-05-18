package com.pixelperfect.socialhub.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.RecyclerItemClickListener;
import com.pixelperfect.socialhub.adapters.NetworkAdapter;
import com.pixelperfect.socialhub.models.Network;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class NetworksListActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button createNetwork;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private ArrayList<Network> networks;
    private NetworkAdapter networkAdapter;
    private String selectedNetworkID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networks_list);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Networks List");

        createNetwork = findViewById(R.id.createNetwork);
        createNetwork.setOnClickListener(this);

        recyclerView = findViewById(R.id.networksList_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever

                        int itemPosition = recyclerView.getChildLayoutPosition(view);
                        Network item = networks.get(itemPosition);

//                        Toast.makeText(NetworksListActivity.this, "onItemClick " + item.getId(), Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(NetworksListActivity.this, NetworkActivity.class)
                                .putExtra("NETWORK_ID", item.getId()));
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        networks = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("Networks");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Network network = dataSnapshot.getValue(Network.class);
                    networks.add(network);
                }
                networkAdapter = new NetworkAdapter(NetworksListActivity.this, networks);
                recyclerView.setAdapter(networkAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(NetworksListActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    selectedNetworkID = dataSnapshot.getKey();
//                    Toast.makeText(NetworksListActivity.this, "selectedNetworkID : " + selectedNetworkID, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createNetwork:
                startActivity(new Intent(this, CreateNetworkActivity.class));
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
        startActivity(new Intent(NetworksListActivity.this, LoginActivity.class));
    }
}