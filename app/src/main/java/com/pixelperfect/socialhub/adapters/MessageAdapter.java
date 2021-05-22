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
import com.google.firebase.database.FirebaseDatabase;
import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.models.Message;
import com.pixelperfect.socialhub.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<Message> messages;

    FirebaseUser currentUser;

    public MessageAdapter(Context context, List<Message> messages) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);

        assert holder.show_message != null;
        holder.show_message.setText(message.getContent());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (messages.get(position).getIdSender().equals(currentUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return  MSG_TYPE_LEFT;
        }
    }
}