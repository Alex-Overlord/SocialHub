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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.listeners.RecyclerItemClickListener;
import com.pixelperfect.socialhub.adapters.NetworkAdapter;
import com.pixelperfect.socialhub.models.Network;
import com.pixelperfect.socialhub.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class NetworksListActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button createNetwork;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private ArrayList<Network> networks;
    private NetworkAdapter networkAdapter;

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
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        int itemPosition = recyclerView.getChildLayoutPosition(view);
                        Network item = networks.get(itemPosition);

                        DatabaseReference refNetworks = FirebaseDatabase.getInstance().getReference().child("Networks");

                        refNetworks.orderByChild("id").equalTo(item.getId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    DatabaseReference refUsersNetwork = getInstance().getReference().child("Networks").child(Objects.requireNonNull(childSnapshot.getKey())).child("users");
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    assert user != null;
                                    User insertedUser = new User(user.getUid(), user.getDisplayName(), user.getEmail());

                                    refUsersNetwork.child(user.getUid()).setValue(insertedUser);

                                    if (item.getType().equals("Chat")) {
                                        startActivity(new Intent(NetworksListActivity.this, ChatActivity.class)
                                                .putExtra("NETWORK_ID", item.getId()));
                                    } else if (item.isMember(insertedUser) && item.getType().equals("Posts")) {
                                        startActivity(new Intent(NetworksListActivity.this, PostsActivity.class)
                                                .putExtra("NETWORK_ID", item.getId()));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(NetworksListActivity.this, "selectionner le reseau ?", Toast.LENGTH_LONG).show();
                    }
                })
        );

        networks = new ArrayList<>();

        reference = getInstance().getReference().child("Networks");
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
        startActivity(new Intent(NetworksListActivity.this, LoginActivity.class));
    }
}