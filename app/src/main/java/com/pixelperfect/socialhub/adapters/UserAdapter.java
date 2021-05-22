package com.pixelperfect.socialhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUser;

    public UserAdapter(Context mContext, List<User> mUserModels) {
        this.mUser = mUserModels;
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        assert mContext != null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.ViewHolder holder, int position) {
        User user = mUser.get(position);
        holder.username.setText(user.getFullName());

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
        }
    }
}