package com.example.chatappfirebase.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappfirebase.IndividualChat.IndividualChatFragment;
import com.example.chatappfirebase.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    Context context;
    ArrayList<FirebaseChatModel> firebaseChatModels;
    FragmentManager fragmentManager;

    public ChatAdapter(Context context, ArrayList<FirebaseChatModel> firebaseChatModels, FragmentManager fragmentManager) {
        this.context = context;
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
        holder.particularusername.setText(chatModel.getName());
        Picasso.get().load(R.drawable.defaultprofile).into(holder.mimageviewofuser);
        holder.statusofuser.setText(chatModel.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment = new IndividualChatFragment();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle individualChatData = new Bundle();
                        individualChatData.putString("name",chatModel.getName());
                        individualChatData.putString("receiveruid",chatModel.getUid());
                        individualChatData.putString("imageuri",chatModel.getImage());
                        individualChatData.putString("status",chatModel.getName());
                        fragment.setArguments(individualChatData);
                        fragmentTransaction.replace(R.id.replace, fragment);
                        fragmentTransaction.commit();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return firebaseChatModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        private final TextView particularusername;
        private final TextView statusofuser;
        private final ImageView mimageviewofuser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            particularusername=itemView.findViewById(R.id.nameofuser);
            statusofuser=itemView.findViewById(R.id.statusofuser);
            mimageviewofuser=itemView.findViewById(R.id.imageviewofuser);
        }
    }

}
