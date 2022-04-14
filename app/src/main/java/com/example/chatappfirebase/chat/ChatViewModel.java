package com.example.chatappfirebase.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.chatappfirebase.data.ChatsRepository;

import java.util.ArrayList;

public class ChatViewModel extends AndroidViewModel {

    private ChatsRepository repository;

    public MutableLiveData<ArrayList<FirebaseChatModel>> firebaseChatModelArrayListMutableLiveData;
    public ArrayList<FirebaseChatModel> firebaseChatModelArrayList;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        repository = ChatsRepository.getInstance(application);
        firebaseChatModelArrayListMutableLiveData = new MutableLiveData<>();
    }

//    private void init() {
//        populateList();
//        firebaseChatModelArrayListMutableLiveData.setValue(firebaseChatModelArrayList);
//    }

//    private void populateList() {
//        firebaseChatModelArrayList.addAll(repository.);
//    }

    public MutableLiveData<ArrayList<FirebaseChatModel>> getFirebaseChatModelArrayListMutableLiveData() {
        return repository.getFirebaseChatModelArrayListMutableLiveData();
    }

//    public void eventChangeListener(){
//        repository.eventChangeListener();
//    }
}
