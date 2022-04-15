package com.example.chatappfirebase.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.chatappfirebase.data.ChatsRepository;

import java.util.ArrayList;

public class ChatViewModel extends AndroidViewModel {

    private final ChatsRepository repository;

    public MutableLiveData<ArrayList<FirebaseChatModel>> firebaseChatModelArrayListMutableLiveData;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        repository = ChatsRepository.getInstance(application);
        firebaseChatModelArrayListMutableLiveData = repository.getFirebaseChatModelArrayListMutableLiveData();
    }

    public MutableLiveData<ArrayList<FirebaseChatModel>> getFirebaseChatModelArrayListMutableLiveData() {
        return firebaseChatModelArrayListMutableLiveData;
    }

    public void eventChangeListener(){
        repository.eventChangeListener();
    }
}
