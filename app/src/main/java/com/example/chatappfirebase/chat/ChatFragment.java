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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;
    ArrayList<FirebaseChatModel> firebaseChatModelArrayList;
    ChatAdapter myChatAdapter;
    RecyclerView mrecyclerview;

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
        mrecyclerview=view.findViewById(R.id.recyclerview);

        mrecyclerview.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerview.setLayoutManager(linearLayoutManager);

        firebaseChatModelArrayList = new ArrayList<>();
        myChatAdapter = new ChatAdapter(getContext(),firebaseChatModelArrayList, getActivity().getSupportFragmentManager());

        mrecyclerview.setAdapter(myChatAdapter);
        eventChangeListener();
        Log.d("currentUser",firebaseAuth.getCurrentUser().getUid());
        Log.d("chatFragment","onCreateView");

//        chatAdapter=new FirestoreRecyclerAdapter<FirebaseChatModel, NoteViewHolder>(allUserName) {
//            @Override
//            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull FirebaseChatModel firebasemodel) {
//
//                noteViewHolder.particularusername.setText(firebasemodel.getName());
//                String uri=firebasemodel.getImage();
//                if(!uri.isEmpty()) {
//                    Picasso.get().load(uri).into(mimageviewofuser);
//                }else{
//                    Picasso.get().load(R.drawable.defaultprofile).into(mimageviewofuser);
//                }
//                if(firebasemodel.getStatus().equals("Online"))
//                {
//                    noteViewHolder.statusofuser.setText(firebasemodel.getStatus());
//                    noteViewHolder.statusofuser.setTextColor(Color.GREEN);
//                }
//                else
//                {
//                    noteViewHolder.statusofuser.setText(firebasemodel.getStatus());
//                }
//
//                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Fragment fragment = new IndividualChatFragment();
//                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        Bundle individualChatData = new Bundle();
//                        individualChatData.putString("name",firebasemodel.getName());
//                        individualChatData.putString("receiveruid",firebasemodel.getName());
//                        individualChatData.putString("imageuri",firebasemodel.getName());
//                        individualChatData.putString("name",firebasemodel.getName());
//                        fragment.setArguments(individualChatData);
//                        fragmentTransaction.replace(R.id.replace, fragment);
//                        fragmentTransaction.commit();
//                    }
//                });
//            }
//            @NonNull
//            @Override
//            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chatviewlayout,parent,false);
//                return new NoteViewHolder(view);
//            }
//        };
        return view;
    }

    private void eventChangeListener() {
        firebaseFirestore.collection("Users").whereNotEqualTo("uid",firebaseAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!= null)
                        {
                            Log.e("Firestore Error", error.getLocalizedMessage());
                            return;
                        }
                        for (DocumentChange documentChange: value.getDocumentChanges()){
                            firebaseChatModelArrayList.add(documentChange.getDocument().toObject(FirebaseChatModel.class));
                        }
                        myChatAdapter.notifyDataSetChanged();
                    }
                });
    }
}
