package com.pixelperfect.socialhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.models.Network;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NetworkAdapter extends RecyclerView.Adapter<NetworkAdapter.ViewHolder> {
    private Context mContext;
    private List<Network> mNetwork;

    public NetworkAdapter(Context mContext, List<Network> mNetwork) {
        this.mNetwork = mNetwork;
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.network_item, parent, false);
        return new NetworkAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NetworkAdapter.ViewHolder holder, int position) {
        Network network = mNetwork.get(position);
        holder.name.setText(network.getName());
        holder.description.setText(network.getDescription());
        holder.type.setText(network.getType());
        if (holder.type.getText().equals("Chat")) {
            holder.type.setTextColor(ContextCompat.getColor(mContext, R.color.secondary));
        } else if (holder.type.getText().equals("Posts")) {
            holder.type.setTextColor(ContextCompat.getColor(mContext, R.color.primary));
        } else {
            holder.type.setTextColor(ContextCompat.getColor(mContext, R.color.purple_200));
        }
    }

    @Override
    public int getItemCount() {
        return mNetwork.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView description;
        public TextView type;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.network_name);
            description = itemView.findViewById(R.id.network_description);
            type = itemView.findViewById(R.id.network_type);
        }
    }
}