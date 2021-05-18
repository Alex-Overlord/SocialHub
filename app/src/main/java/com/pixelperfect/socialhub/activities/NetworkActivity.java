package com.pixelperfect.socialhub.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.fragments.ChatsFragment;
import com.pixelperfect.socialhub.fragments.UsersFragment;
import com.pixelperfect.socialhub.models.Network;
import com.pixelperfect.socialhub.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class NetworkActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private String networkID;
    private Network network;
    private TextView textView;

    FirebaseUser firebaseUser;
    DatabaseReference referenceUser;
    DatabaseReference referenceNetworks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                networkID = null;
            } else {
                networkID = extras.getString("NETWORK_ID");
            }
        } else {
            networkID = (String) savedInstanceState.getSerializable("NETWORK_ID");
        }

        textView = findViewById(R.id.fragment_chats_text);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Network");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        referenceUser = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        referenceNetworks = FirebaseDatabase.getInstance().getReference("Networks");

        referenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                User userModel = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        viewPagerAdapter.addFragment(new UsersFragment(), "Users");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Networks");
//        DatabaseReference ref = reference.getParent().child(networkID);


//        Query query = reference.orderByChild("Networks").equalTo(networkID);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        network = dataSnapshot.getValue(Network.class);
//
//                        Toast.makeText(NetworkActivity.this, network.getId().getClass().toString(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(NetworkActivity.this, networkID.getClass().toString(), Toast.LENGTH_SHORT).show();
//
//                        assert network != null;
//                        if (network.getId().equals(networkID)) {
//                            Objects.requireNonNull(getSupportActionBar()).setTitle(network.getName());
//                        }
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
        referenceNetworks.orderByChild("id").equalTo(networkID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                    network = childSnapshot.getValue(Network.class);
                    getSupportActionBar().setTitle(network.getName());
//                    Toast.makeText(NetworkActivity.this, "name : " + network.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
//        referenceNetworks.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Network network_tmp = dataSnapshot.getValue(Network.class);
//                    network_tmp.setId(dataSnapshot.getKey());
//
////                    textView.setText("network_tmp.getId() : " + network_tmp.getId() + "\n networkID : " + networkID);
//                    Toast.makeText(NetworkActivity.this,
//                            "String.valueOf(network.getId()) : " + String.valueOf(network_tmp.getId())
//                                    + "\nString.valueOf(networkID) : " + String.valueOf(networkID)
//                                    + "\n" + String.valueOf(network_tmp.getId() == networkID),
//                            Toast.LENGTH_SHORT).show();
////                    getSupportActionBar().setTitle(networkID);
////
////                    if (dataSnapshot.getKey().equals(networkID)) {
////                    if ((String) network_tmp.getId() == (String) networkID) {
////
////                    Toast.makeText(NetworkActivity.this, networkID, Toast.LENGTH_SHORT).show();
////                    Toast.makeText(NetworkActivity.this, network_tmp.getId(), Toast.LENGTH_SHORT).show();
////                    assert networkID != null;
////                    assert network_tmp.getId() != null;
////                    if (networkID.equals(network_tmp.getId())) {
////
////                        Toast.makeText(NetworkActivity.this, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
////                        network = snapshot.getValue(Network.class);
////                        getSupportActionBar().setTitle(network.getName());
////                        Toast.makeText(NetworkActivity.this, network.getName(), Toast.LENGTH_SHORT).show();
////                    }
//
////                    Toast.makeText(NetworkActivity.this, snapshot.getKey(), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });




//        referenceNetworks.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Network network_tmp = dataSnapshot.getValue(Network.class);
////                    Toast.makeText(NetworkActivity.this, networkID, Toast.LENGTH_SHORT).show();
////                    Toast.makeText(NetworkActivity.this, network_tmp.getId(), Toast.LENGTH_SHORT).show();
//                    network = snapshot.getValue(Network.class);
//                    getSupportActionBar().setTitle(network.getName());
//
//                    assert networkID != null;
//                    assert network_tmp.getId() != null;
//                    if (networkID.equals(network_tmp.getId())) {
//                        //                    Toast.makeText(NetworkActivity.this,"network_tmp.getId() : " + network_tmp.getId() + "\n networkID : " + networkID, Toast.LENGTH_SHORT).show();
//                        network = snapshot.getValue(Network.class);
//                        getSupportActionBar().setTitle(network.getName());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case :
//                break;
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
        startActivity(new Intent(this, LoginActivity.class));
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @NotNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @org.jetbrains.annotations.Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}