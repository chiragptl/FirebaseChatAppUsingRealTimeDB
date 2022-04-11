package com.example.chatappfirebase.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Objects;

public class ChatFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;
    ArrayList<FirebaseChatModel> firebaseChatModelArrayList;
    ChatAdapter myChatAdapter;
    RecyclerView mRecyclerview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("chatFragment","onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container,false);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        mRecyclerview =view.findViewById(R.id.recyclerview);

        mRecyclerview.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerview.setLayoutManager(linearLayoutManager);

        firebaseChatModelArrayList = new ArrayList<>();
        myChatAdapter = new ChatAdapter(firebaseChatModelArrayList, requireActivity().getSupportFragmentManager());

        mRecyclerview.setAdapter(myChatAdapter);
        eventChangeListener();
        Log.d("currentUser", Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
        Log.d("chatFragment","onCreateView");
        return view;
    }

    private void eventChangeListener() {
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
                        if (!firebaseChatModelArrayList.contains(firebaseChat)) {
                            firebaseChatModelArrayList.add(firebaseChat);
                        }
                    }
                    myChatAdapter.notifyItemRangeInserted(myChatAdapter.getItemCount(),firebaseChatModelArrayList.size());
                });
    }
}
