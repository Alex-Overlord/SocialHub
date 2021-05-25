package com.pixelperfect.socialhub.helpers;

import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.activities.PostsActivity;
import com.pixelperfect.socialhub.models.Post;
import com.pixelperfect.socialhub.models.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Helper {
    private static final String COLLECTION_MESSAGES = "messages";
    private static final String COLLECTION_POSTS = "posts";

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static Task<DocumentReference> createMessageWithImageForChat(String urlImage, String textMessage, String chat, User userSender, PostsActivity postsActivity) {
//        TextView posts_text = postsActivity.findViewById(R.id.posts_text);
//        LocalDateTime now = LocalDateTime.now().plusHours(2);
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
//        String formatDateTime = now.format(format);
//        Post post = new Post(postsActivity.currentUser.getUid(), postsActivity.currentUser.getDisplayName(), formatDateTime, posts_text.getText().toString(), "post");
//
//        return postsActivity.referenceCurrentNetworkPosts
//                .document(postsActivity.networkKey)
//                .
//    }
}
