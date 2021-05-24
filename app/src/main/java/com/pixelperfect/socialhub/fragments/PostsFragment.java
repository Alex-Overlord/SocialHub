package com.pixelperfect.socialhub.fragments;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.activities.PostsActivity;
import com.pixelperfect.socialhub.adapters.MessageAdapter;
import com.pixelperfect.socialhub.adapters.PostAdapter;
import com.pixelperfect.socialhub.models.Message;
import com.pixelperfect.socialhub.models.Post;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PostsFragment extends Fragment implements View.OnClickListener {

    public PostsActivity postsActivity;

    private RecyclerView posts_recycler_view;
    private ImageButton send_post;
    private ImageButton add_media;
    private TextView text_send;

    private Message message;
    private MessageAdapter messageAdapter;
    private List<Message> messages;

    private Post post;
    private PostAdapter postAdapter;
    private List<Post> posts;

    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_posts, container, false);

        postsActivity = (PostsActivity) getActivity();
        assert postsActivity != null;

        posts_recycler_view = root.findViewById(R.id.posts_recycler_view);
        posts_recycler_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        posts_recycler_view.setLayoutManager(linearLayoutManager);

        text_send = root.findViewById(R.id.posts_text);

        send_post = root.findViewById(R.id.send_text);
        send_post.setOnClickListener(this);

        add_media = root.findViewById(R.id.add_media);
        add_media.setOnClickListener(v -> onClickAddFile());

        read();

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void onClickAddFile() {
        if (!EasyPermissions.hasPermissions(Objects.requireNonNull(getContext()), PERMS)) {
            EasyPermissions.requestPermissions((Activity) getContext(), "Allow SocialHub to acces photos and media on your device ?", RC_IMAGE_PERMS);
            return;
        }
        Toast.makeText(getContext(), "You have access to pictures !", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_text) {
            if (!text_send.getText().toString().equals("")) {
                LocalDateTime now = LocalDateTime.now().plusHours(2);
                DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
                String formatDateTime = now.format(format);
                message = new Message(postsActivity.currentUser.getUid(), postsActivity.currentUser.getDisplayName(), formatDateTime, text_send.getText().toString(), "text");
                sendMessage(message);
            } else {
                Toast.makeText(getContext(), "You can't send a empty post !", Toast.LENGTH_SHORT).show();
            }
            text_send.setText("");
        }
    }

    private void sendMessage(Message message) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("idSender", message.getIdSender());
        hashMap.put("username", message.getUsername());
        hashMap.put("date", message.getDate());
        hashMap.put("content", message.getContent());
        hashMap.put("type", message.getType());
        postsActivity.referenceCurrentNetwork.child("messages").push().setValue(hashMap);
    }

    private void read() {
        posts = new ArrayList<>();
        messages = new ArrayList<>();


//        assert chatActivity != null;
//        assert chatActivity.referenceCurrentNetworkMessages != null;
//        chatActivity.referenceCurrentNetworkMessages.addValueEventListener(new ValueEventListener() {

//        assert postsActivity.referenceCurrentNetworkMessages != null;
        if (postsActivity.referenceCurrentNetworkMessages != null) {
            postsActivity.referenceCurrentNetworkMessages.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    messages.clear();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Message messageTmp = childSnapshot.getValue(Message.class);
                        messages.add(messageTmp);
                    }

                    messageAdapter = new MessageAdapter(getContext(), messages);
                    posts_recycler_view.setAdapter(messageAdapter);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }
}