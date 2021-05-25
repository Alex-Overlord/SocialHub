package com.pixelperfect.socialhub.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

public class PostsFragment extends Fragment implements View.OnClickListener {

    public PostsActivity postsActivity;

    private RecyclerView posts_recycler_view;
    private ImageButton send_post;
    private ImageButton add_media;
    private TextView posts_text;
    private ImageView imageViewPreview;

    private Message message;
    private MessageAdapter messageAdapter;
    private List<Message> messages;

    private Post post;
    private PostAdapter postAdapter;
    private List<Post> posts;

    private Uri uriImageSelected;

    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;

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

        posts_text = root.findViewById(R.id.posts_text);

        send_post = root.findViewById(R.id.send_text);
        send_post.setOnClickListener(this);

        add_media = root.findViewById(R.id.add_media);
        add_media.setOnClickListener(v -> {
            onClickAddFile();
        });

        imageViewPreview = root.findViewById(R.id.imageViewPreview);

        read();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createPostWithImage() {
        TextView posts_text = postsActivity.findViewById(R.id.posts_text);
        LocalDateTime now = LocalDateTime.now().plusHours(2);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        String formatDateTime = now.format(format);
        Post post = new Post(postsActivity.currentUser.getUid(), postsActivity.currentUser.getDisplayName(), formatDateTime, posts_text.getText().toString(), "post");
        sendPost(post);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        this.handleResponse(requestCode, resultCode, data);
    }

    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void onClickAddFile() {
        if (!EasyPermissions.hasPermissions(postsActivity, PERMS)) {
            EasyPermissions.requestPermissions(postsActivity, "Allow SocialHub to access photos and media on your device ?", RC_IMAGE_PERMS, PERMS);
            return;
        }
        Toast.makeText(getContext(), "You have access to pictures !", Toast.LENGTH_SHORT).show();
        chooseImageFromPhone();
        imageViewPreview.setVisibility(View.VISIBLE);
    }

    // File management
    private void chooseImageFromPhone() {
        if (!EasyPermissions.hasPermissions(postsActivity, PERMS)) {
            EasyPermissions.requestPermissions(postsActivity, "Allow SocialHub to access photos and media on your device ?", RC_IMAGE_PERMS, PERMS);
            return;
        }
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), RC_IMAGE_PERMS);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RC_CHOOSE_PHOTO) {
//            if (requestCode == RESULT_OK) {
        this.uriImageSelected = data.getData();
        Glide.with(postsActivity)
                .load(this.uriImageSelected)
                .apply(RequestOptions.centerCropTransform())
                .into(this.imageViewPreview);
//        } else {
//            Toast.makeText(postsActivity, "No image choosen!", Toast.LENGTH_SHORT).show();
//        }
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_text) {
            if (!(imageViewPreview.getDrawable() == null)) {
                uploadPhotoInFirebaseAndSendMessage(posts_text.getText().toString());
            } else if (!posts_text.getText().toString().equals("")) {
                LocalDateTime now = LocalDateTime.now().plusHours(2);
                DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
                String formatDateTime = now.format(format);
                message = new Message(postsActivity.currentUser.getUid(), postsActivity.currentUser.getDisplayName(), formatDateTime, posts_text.getText().toString(), "text");
                sendMessage(message);
            } else {
                Toast.makeText(getContext(), "You can't send a empty post !", Toast.LENGTH_SHORT).show();
            }
            posts_text.setText("");
        }
    }


    private void read() {
        posts = new ArrayList<>();
        messages = new ArrayList<>();

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

    private void onClickSendMessage() {
        if (!TextUtils.isEmpty(posts_text.getText()) && postsActivity.currentUser != null) {
            if (!(imageViewPreview.getDrawable() == null)) {
//                sendMessage();
            } else {
                uploadPhotoInFirebaseAndSendMessage(posts_text.getText().toString());
                posts_text.setText("");
                Toast.makeText(getContext(), "Picture uploaded", Toast.LENGTH_SHORT).show();
                imageViewPreview.setImageResource(R.drawable.logo_small_icon_only_inverted);
            }
        }
    }

    private void uploadPhotoInFirebaseAndSendMessage(final String message) {
        String uuid = UUID.randomUUID().toString();
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);
        if (this.uriImageSelected != null) {
            mImageRef.putFile(this.uriImageSelected)
                    .addOnSuccessListener(postsActivity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String pathImageSavedInFirebase = (Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata())
                                    .getReference())).getDownloadUrl().toString();

                            LocalDateTime now = LocalDateTime.now().plusHours(2);
                            DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
                            String formatDateTime = now.format(format);
                            post = new Post(postsActivity.currentUser.getUid(), postsActivity.currentUser.getDisplayName(), formatDateTime, pathImageSavedInFirebase, "post");
                        }
                    });
        }
    }

    private void sendPost(Post post) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("idSender", post.getIdSender());
        hashMap.put("username", post.getUsername());
        hashMap.put("date", post.getDate());
        hashMap.put("content", post.getContent());
        hashMap.put("type", post.getType());
        postsActivity.referenceCurrentNetwork.child("posts").push().setValue(hashMap);
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
}