package com.example.chatappfirebase.IndividualChat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappfirebase.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class IndividualChatMessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Messages> messagesArrayList = new ArrayList<>();

    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public void add(Messages addMessage) {
        messagesArrayList.add(addMessage);
        Log.d("add", "add: single value");
        notifyItemInserted(messagesArrayList.size());
    }

    public void addAll(ArrayList<Messages> addAllMessage) {
        this.messagesArrayList.addAll(addAllMessage);
        Log.d("add", "add: all value");
        notifyItemRangeInserted(getItemCount(), addAllMessage.size());
    }

    public int findIndex(String senderId, long timeStamp){
        int index = -1;
        for (Messages msg: messagesArrayList) {
            index++;
            if(msg.getSenderId().equalsIgnoreCase(senderId) && msg.getTimestamp() == timeStamp);
            {
                return index;
            }
        }
        return index;
    }

    public void update(Messages messages, int index) {
        Log.d("index", "index in update: "+index);
        messagesArrayList.set(index,messages);
        Log.d("message","Updated");
        notifyItemChanged(index);
    }

    public IndividualChatMessageAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_chat_layout, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_chat_layout, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = messagesArrayList.get(position);
        if (holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.textViewMessage.setText(messages.getMessage());
            viewHolder.timeOfMessage.setText(messages.getCurrenttime());
        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.textViewMessage.setText(messages.getMessage());
            viewHolder.timeOfMessage.setText(messages.getCurrenttime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        TextView timeOfMessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.sendermessage);
            timeOfMessage = itemView.findViewById(R.id.timeofmessage);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        TextView timeOfMessage;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.sendermessage);
            timeOfMessage = itemView.findViewById(R.id.timeofmessage);
        }
    }

}
