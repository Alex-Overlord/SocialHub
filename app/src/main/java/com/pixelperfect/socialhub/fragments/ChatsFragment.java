package com.pixelperfect.socialhub.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixelperfect.socialhub.R;
import com.pixelperfect.socialhub.activities.NetworkActivity;

public class ChatsFragment extends Fragment {

    public NetworkActivity networkActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }
}