package com.example.chatappfirebase.dashboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappfirebase.R;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    private final List<MessagesList> messagesLists;
    private final Context context;

    public MessagesAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MessagesList messagesList = messagesLists.get(position);
        holder.name.setText(messagesList.getName());
        holder.lastMessage.setText(messagesList.getLastMessage());

        if(messagesList.getUnseenMessages() == 0){
            holder.unseenMessage.setVisibility(View.GONE);
        }
        else {
            holder.unseenMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return messagesLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView profile;
        private TextView name;
        private TextView lastMessage;
        private TextView unseenMessage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            unseenMessage = itemView.findViewById(R.id.unseenMessage);
        }
    }
}
