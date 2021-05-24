package com.pixelperfect.socialhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.models.Message;
import com.pixelperfect.socialhub.models.Post;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
//    public static final int MSG_TYPE_LEFT = 0;
//    public static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<Post> posts;

    FirebaseUser currentUser;

    public PostAdapter(Context context, List<Post> posts) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
//        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
//        } else {
//            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
//        }
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);

        assert holder.show_message != null;
        holder.show_message.setText(post.getContent());
        holder.message_username.setText(post.getUsername());
        holder.message_date.setText(post.getDate());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public TextView message_username;
        public TextView message_date;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            message_username = itemView.findViewById(R.id.message_username);
            message_date = itemView.findViewById(R.id.message_date);
        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
////        if (messages.get(position).getIdSender().equals(currentUser.getUid())) {
////            return MSG_TYPE_RIGHT;
////        } else {
////            return MSG_TYPE_LEFT;
////        }
//        return 0;
//    }
}