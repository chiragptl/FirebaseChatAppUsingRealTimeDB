package com.example.chatappfirebase.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Objects;

public class ChatFragment extends Fragment {

    private ChatViewModel chatViewModel;
    ArrayList<FirebaseChatModel> firebaseChatModelArrayList;
    ChatAdapter myChatAdapter;

    LinearLayoutManager linearLayoutManager;
    RecyclerView mRecyclerview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);

        Log.d("chatFragment", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mRecyclerview = view.findViewById(R.id.recyclerview);
        mRecyclerview.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerview.setLayoutManager(linearLayoutManager);
//        firebaseChatModelArrayList = new ArrayList<>();

        myChatAdapter = new ChatAdapter(firebaseChatModelArrayList, requireActivity().getSupportFragmentManager());

        chatViewModel.getFirebaseChatModelArrayListMutableLiveData().observe(requireActivity(), userListUpdateObserver);
        mRecyclerview.setAdapter(myChatAdapter);

        Log.d("chatFragment", "onCreateView");
        return view;
    }
    Observer<ArrayList<FirebaseChatModel>> userListUpdateObserver = new Observer<ArrayList<FirebaseChatModel>>() {
        @Override
        public void onChanged(ArrayList<FirebaseChatModel> firebaseChatModels) {
            myChatAdapter.updateUserList(firebaseChatModels);

        }
    };
}
