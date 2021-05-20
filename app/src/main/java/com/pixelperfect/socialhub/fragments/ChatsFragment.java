package com.pixelperfect.socialhub.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.activities.NetworkActivity;
import com.pixelperfect.socialhub.adapters.UserAdapter;
import com.pixelperfect.socialhub.models.Message;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ChatsFragment extends Fragment implements View.OnClickListener {

    public NetworkActivity networkActivity;

    RecyclerView chats_recycler_view;
    ImageButton btn_send;
    EditText text_send;
    Message message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_chats, container, false);

        chats_recycler_view = root.findViewById(R.id.chats_recycler_view);
        chats_recycler_view.setHasFixedSize(true);
        chats_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));

        networkActivity = (NetworkActivity) getActivity();
        assert networkActivity != null;

        btn_send = root.findViewById(R.id.btn_send);
        text_send = root.findViewById(R.id.text_send);

        btn_send.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                if (!text_send.getText().toString().equals("")) {
                    message = new Message(networkActivity.currentUser.getUid(), Calendar.getInstance().getTime().toString(), text_send.getText().toString(), "text");
                    sendMessage(message);
                } else {
                    Toast.makeText(getContext(), "You can't send a empty message !", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
                break;
        }
    }

    private void sendMessage(Message message) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("idSender", message.getIdSender());
        hashMap.put("date", message.getDate());
        hashMap.put("content", message.getContent());
        hashMap.put("type", message.getType());
        networkActivity.referenceCurrentNetwork.child("messages").push().setValue(hashMap);
    }
}