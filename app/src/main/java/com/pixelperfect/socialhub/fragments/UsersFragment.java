package com.pixelperfect.socialhub.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.activities.NetworkActivity;
import com.pixelperfect.socialhub.activities.NetworksListActivity;
import com.pixelperfect.socialhub.adapters.UserAdapter;
import com.pixelperfect.socialhub.listeners.RecyclerItemClickListener;
import com.pixelperfect.socialhub.models.Network;
import com.pixelperfect.socialhub.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    public NetworkActivity networkActivity;
    private Network network;
    private String networkKey;
    public ArrayList<User> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.fragmert_users_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        networkActivity = (NetworkActivity) getActivity();
        assert networkActivity != null;
        networkKey = networkActivity.networkKey;
        network = networkActivity.network;
        usersList = new ArrayList<User>();
        readUsers();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(),recyclerView, new RecyclerItemClickListener.OnItemClickListener(){

                    @Override
                    public void onItemClick(View view, int position) {
                        //view.setBackgroundColor(Color.parseColor("FF0000"));
                        int itemPosition = recyclerView.getChildLayoutPosition(view);
                        User item = usersList.get(itemPosition);
                        Toast.makeText(getContext(), String.valueOf(itemPosition), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        DatabaseReference referenceNetworks = FirebaseDatabase.getInstance().getReference("Networks");
                        DatabaseReference referenceUsersNetwork = referenceNetworks.child(networkKey).child("users");
                        int itemPosition = recyclerView.getChildLayoutPosition(view);
                        User item = usersList.get(itemPosition);
                        String str = referenceUsersNetwork.child(item.getId().toString()).getKey();
                        referenceUsersNetwork.child(item.getId().toString()).removeValue();
                        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                        network.suppMember(item);

                    }
                })
        );

        return view;
    }

    private void readUsers() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference referenceNetworks = FirebaseDatabase.getInstance().getReference("Networks");
        DatabaseReference referenceUsersNetwork = referenceNetworks.child(networkKey).child("users");
        referenceUsersNetwork.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                network.getUsers().clear();

                assert currentUser != null;
                User insertedUser = new User(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail());
                network.addUser(currentUser.getUid(), insertedUser);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    network.getUsers().put(user.getId(),user);
                }

                userAdapter = new UserAdapter(getContext(), new ArrayList(network.getUsers().values()));
                usersList = new ArrayList(network.getUsers().values());
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}