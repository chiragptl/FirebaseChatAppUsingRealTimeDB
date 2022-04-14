package com.example.chatappfirebase.individualChat;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class IndividualChatRepository {

    private static IndividualChatRepository individualChatRepository;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    public MutableLiveData<ArrayList<Messages>> messageList;
    public MutableLiveData<Messages> editedMessage;
    public MutableLiveData<Integer> editedMessageIndex;
    public MutableLiveData<String> sentText;

    public MutableLiveData<ArrayList<Messages>> getMessageList() {
        return messageList;
    }

    public MutableLiveData<Messages> getEditedMessage() {
        return editedMessage;
    }

    public static IndividualChatRepository getInstance(Application application){
        if(individualChatRepository == null){
            individualChatRepository = new IndividualChatRepository(application);
        }
        return individualChatRepository;
    }


    public IndividualChatRepository(Application application) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        messageList = new MutableLiveData<>();
        editedMessage = new MutableLiveData<>();
        editedMessageIndex = new MutableLiveData<>();
        sentText = new MutableLiveData<>();
    }

    public void LoadChat(String senderReceiver) {

        ArrayList<Messages> messages = new ArrayList<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("chats").child(senderReceiver).child("messages");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("snapshot", "Message timestamp: " + Objects.requireNonNull(snapshot.getValue(Messages.class)).getMessage());
                messages.add(snapshot.getValue(Messages.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Messages messages = snapshot.getValue(Messages.class);

                Log.d("change", messages.getMessage() + " " + previousChildName);

                editedMessage.setValue(messages);

                Log.d("message", messages.getMessage() + " " + messages.getSenderId());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        messageList.postValue(messages);
    }


    public void sentMessage(String enteredMessage, String senderReceiver, String receiverSender) {
        Date date = new Date();
        String currentTime = SimpleDateFormat.getTimeInstance().format(Calendar.getInstance().getTime());

        Messages messages = new Messages(enteredMessage, firebaseAuth.getUid(), date.getTime(), currentTime);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("chats")
                .child(senderReceiver)
                .child("messages")
                .push().setValue(messages).addOnCompleteListener(task -> firebaseDatabase.getReference()
                .child("chats")
                .child(receiverSender)
                .child("messages")
                .push()
                .setValue(messages).addOnCompleteListener(task1 -> {
                }));
        sentText.setValue(null);
    }
}
