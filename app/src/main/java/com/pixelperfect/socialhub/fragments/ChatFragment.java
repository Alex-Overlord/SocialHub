package com.pixelperfect.socialhub.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.activities.ChatActivity;
import com.pixelperfect.socialhub.adapters.MessageAdapter;
import com.pixelperfect.socialhub.models.Message;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatFragment extends Fragment implements View.OnClickListener {

    public ChatActivity chatActivity;

    RecyclerView chats_recycler_view;
    ImageButton btn_send;
    EditText text_send;
    Message message;
    MessageAdapter messageAdapter;
    List<Message> messages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_chats, container, false);

        chatActivity = (ChatActivity) getActivity();
        assert chatActivity != null;

        chats_recycler_view = root.findViewById(R.id.chats_recycler_view);
        chats_recycler_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        chats_recycler_view.setLayoutManager(linearLayoutManager);
        text_send = root.findViewById(R.id.chat_text);
        btn_send = root.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);

        readMessages();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send) {
            if (!text_send.getText().toString().equals("")) {
                LocalDateTime now = LocalDateTime.now().plusHours(2);
                DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
                String formatDateTime = now.format(format);
                message = new Message(chatActivity.currentUser.getUid(), chatActivity.currentUser.getDisplayName(), formatDateTime, text_send.getText().toString(), "text");
                sendMessage(message);
            } else {
                Toast.makeText(getContext(), "You can't send a empty message !", Toast.LENGTH_SHORT).show();
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
        chatActivity.referenceCurrentNetwork.child("messages").push().setValue(hashMap);
    }

    private void readMessages() {
        messages = new ArrayList<>();

        if (chatActivity.referenceCurrentNetworkMessages != null) {
            chatActivity.referenceCurrentNetworkMessages.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    messages.clear();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Message messageTmp = childSnapshot.getValue(Message.class);
                        messages.add(messageTmp);
                    }

                    messageAdapter = new MessageAdapter(getContext(), messages);
                    chats_recycler_view.setAdapter(messageAdapter);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }

}