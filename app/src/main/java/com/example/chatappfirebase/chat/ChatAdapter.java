package com.example.chatappfirebase.chat;

import static com.example.chatappfirebase.util.FragmentRedirect.redirectToFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappfirebase.individualChat.IndividualChatFragment;
import com.example.chatappfirebase.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    final ArrayList<FirebaseChatModel> firebaseChatModels;
    final FragmentManager fragmentManager;

    public ChatAdapter(ArrayList<FirebaseChatModel> firebaseChatModels, FragmentManager fragmentManager) {
        this.firebaseChatModels = firebaseChatModels;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatviewlayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {

        FirebaseChatModel chatModel = firebaseChatModels.get(position);
        holder.particularUserName.setText(chatModel.getName());
        Picasso.get().load(R.drawable.default_profile).into(holder.mImageViewOfUser);
        holder.statusOfUser.setText(chatModel.getStatus());
        holder.itemView.setOnClickListener(view -> {
            Fragment fragment = new IndividualChatFragment();
            Bundle individualChatData = new Bundle();
            individualChatData.putString("name", chatModel.getName());
            individualChatData.putString("receiverUid", chatModel.getUid());
            individualChatData.putString("imageUri", chatModel.getImage());
            individualChatData.putString("status", chatModel.getName());
            fragment.setArguments(individualChatData);
            redirectToFragment(fragmentManager, fragment);
        });
    }

    @Override
    public int getItemCount() {
        return firebaseChatModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView particularUserName;
        private final TextView statusOfUser;
        private final ImageView mImageViewOfUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            particularUserName = itemView.findViewById(R.id.nameOfUser);
            statusOfUser = itemView.findViewById(R.id.statusOfUser);
            mImageViewOfUser = itemView.findViewById(R.id.imageViewOfUser);
        }
    }

}
