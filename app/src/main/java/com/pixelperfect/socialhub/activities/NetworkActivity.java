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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.fragments.ChatsFragment;
import com.pixelperfect.socialhub.fragments.UsersFragment;
import com.pixelperfect.socialhub.models.Message;
import com.pixelperfect.socialhub.models.Network;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class NetworkActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;

    public String networkID;
    public String networkKey;
    public Network network;
    public FirebaseUser currentUser;
    public DatabaseReference referenceCurrentNetwork;
    public DatabaseReference referenceCurrentNetworkMessages;

    DatabaseReference referenceUser;
    DatabaseReference referenceNetworks;

//    ImageButton btn_send;
//    EditText text_send;
//    Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

//        btn_send = findViewById(R.id.btn_send);
//        text_send = findViewById(R.id.text_send);

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

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Network");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        referenceUser = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        referenceNetworks = FirebaseDatabase.getInstance().getReference("Networks");

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        viewPagerAdapter.addFragment(new UsersFragment(), "Users");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        referenceNetworks.orderByChild("id").equalTo(networkID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {

                    networkKey = childSnapshot.getKey();
                    assert networkKey != null;
                    referenceCurrentNetwork = referenceNetworks.child(networkKey);
                    referenceCurrentNetworkMessages = referenceCurrentNetwork.child("messages");

                    network = childSnapshot.getValue(Network.class);
                    Objects.requireNonNull(getSupportActionBar()).setTitle(network.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


//        btn_send.setOnClickListener(this);
    }

//    private void sendMessage(Message message) {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("idSender", message.getIdSender());
//        hashMap.put("date", message.getDate());
//        hashMap.put("content", message.getContent());
//        hashMap.put("type", message.getType());
//        referenceCurrentNetwork.child("messages").push().setValue(hashMap);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_send:
//                if (!text_send.getText().toString().equals("")) {
//                    message = new Message(currentUser.getUid(), new Date(), text_send.getText().toString(), "text");
//                    sendMessage(message);
//                } else {
//                    Toast.makeText(NetworkActivity.this, "You can't send a empty message !", Toast.LENGTH_SHORT).show();
//                }
//                text_send.setText("");
//                break;
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