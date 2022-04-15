package com.example.chatappfirebase.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.chatappfirebase.chat.FirebaseChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class ChatsRepository {

    private static ChatsRepository chatsRepository;
    private final FirebaseFirestore firebaseFirestore;
    private final FirebaseAuth firebaseAuth;
    public MutableLiveData<ArrayList<FirebaseChatModel>> firebaseChatModelArrayListMutableLiveData;
    ArrayList<FirebaseChatModel> chatList = new ArrayList<>();

    public static ChatsRepository getInstance(Application application){
        if(chatsRepository == null){
            chatsRepository = new ChatsRepository(application);
        }
        return chatsRepository;
    }
    public ChatsRepository(Application application) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseChatModelArrayListMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<FirebaseChatModel>> getFirebaseChatModelArrayListMutableLiveData() {
        return firebaseChatModelArrayListMutableLiveData;
    }


    public void eventChangeListener(){
        chatList.clear();
        firebaseFirestore.collection("Users").whereNotEqualTo("uid", Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .addSnapshotListener((value, error) -> {
                    if(error!= null)
                    {
                        Log.e("Firestore Error", error.getLocalizedMessage());
                        return;
                    }
                    assert value != null;
                    for (DocumentChange documentChange: value.getDocumentChanges()){
                        FirebaseChatModel firebaseChat = documentChange.getDocument().toObject(FirebaseChatModel.class);
                        chatList.add(firebaseChat);
                        firebaseChatModelArrayListMutableLiveData.setValue(chatList);
                    }
                });
    }

}
