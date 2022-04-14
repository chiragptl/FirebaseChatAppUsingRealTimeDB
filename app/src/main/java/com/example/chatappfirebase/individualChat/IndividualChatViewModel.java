package com.example.chatappfirebase.individualChat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class IndividualChatViewModel extends AndroidViewModel {

    public MutableLiveData<ArrayList<Messages>> message;

    public IndividualChatRepository repository;
    public MutableLiveData<Messages> editedMessage;

    public IndividualChatViewModel(@NonNull Application application) {
        super(application);
        repository = IndividualChatRepository.getInstance(application);
        message = repository.getMessageList();
        editedMessage = repository.getEditedMessage();
    }
    public void LoadChat(String senderReceiver){
        repository.LoadChat(senderReceiver);
    }
    public void sendMessage(String enteredMessage, String senderReceiver, String receiverSender) {
        repository.sentMessage(enteredMessage, senderReceiver, receiverSender);
    }

    public MutableLiveData<ArrayList<Messages>> getMessage() {
        return message;
    }

    public MutableLiveData<Messages> getEditedMessage() {
        return editedMessage;
    }
}
