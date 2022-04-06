package com.example.chatappfirebase.IndividualChat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappfirebase.R;
import com.example.chatappfirebase.dashboard.DashboardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class IndividualChatFragment extends Fragment {
    EditText mGetMessage;
    ImageButton mSendMessageButton;

    CardView mSendMessageCardView;
    androidx.appcompat.widget.Toolbar mToolbarOfSpecificChat;
    ImageView mImageviewOfSpecificUser;
    TextView mNameOfSpecificUser;

    private String enteredMessage;
    String mReceiverName, mReceiverUid, mSenderUid;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String senderReceiver, receiverSender;

    ImageButton mBackButtonOfSpecificChat;

    RecyclerView mMessageRecyclerView;

    String currentTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    IndividualChatMessageAdapter messagesAdapter;
    ProgressDialog progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual_chat, container, false);

        mGetMessage = view.findViewById(R.id.getmessage);
        mSendMessageCardView = view.findViewById(R.id.carviewofsendmessage);
        mSendMessageButton = view.findViewById(R.id.imageviewsendmessage);
        mToolbarOfSpecificChat = view.findViewById(R.id.toolbarofspecificchat);
        mNameOfSpecificUser = view.findViewById(R.id.Nameofspecificuser);
        mImageviewOfSpecificUser = view.findViewById(R.id.specificuserimageinimageview);
        mBackButtonOfSpecificChat = view.findViewById(R.id.backbuttonofspecificchat);

        progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading Chats ...");
        progressBar.show();

        mMessageRecyclerView = view.findViewById(R.id.recyclerviewofspecific);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(linearLayoutManager);

        messagesAdapter = new IndividualChatMessageAdapter(getContext());
        mMessageRecyclerView.setAdapter(messagesAdapter);


        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbarOfSpecificChat);
        mToolbarOfSpecificChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("toolbar", "Toolbar is Clicked");
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");

        mSenderUid = firebaseAuth.getUid();
        Bundle bundleData = getArguments();
        mReceiverUid = bundleData.getString("receiverUid");
        mReceiverName = bundleData.getString("name");
        senderReceiver = mSenderUid + mReceiverUid;
        receiverSender = mReceiverUid + mSenderUid;

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("chats").child(senderReceiver).child("messages");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("snapshot", "snapshot count: "+snapshot.getChildrenCount());
                messagesAdapter.add(snapshot.getValue(Messages.class));
                if (progressBar.isShowing())
                {
                    Log.d("ProgressBar", "dismiss");
                    progressBar.dismiss();
                }
                Log.d("ADAPTER","ON DATA CHANGE");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Messages messages = snapshot.getValue(Messages.class);

                Log.d("onChildChanged",messages.getSenderId()+" "+messages.getTimestamp());
                int index = messagesAdapter.findIndex(messages.getSenderId(),messages.getTimestamp());
                Log.d("onChildChanged", "onChildChanged: index "+index);
                Log.d("change",messages.getMessage()+" " +previousChildName);
                messagesAdapter.update(messages,index);
                Log.d("message",messages.getMessage()+" "+messages.getSenderId());
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
        if (progressBar.isShowing())
        {
            Log.d("ProgressBar", "dismiss");
            progressBar.dismiss();
        }

        mBackButtonOfSpecificChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new DashboardFragment();
                redirectToFragment(fragment);
            }
        });

        mNameOfSpecificUser.setText(mReceiverName);
        Picasso.get().load(R.drawable.defaultprofile).into(mImageviewOfSpecificUser);
        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredMessage = mGetMessage.getText().toString();
                if (enteredMessage.isEmpty()) {
                    Log.d("message", "Enter message first");
                } else {
                    Date date = new Date();
                    currentTime = simpleDateFormat.format(calendar.getTime());
                    Messages messages = new Messages(enteredMessage, firebaseAuth.getUid(), date.getTime(), currentTime);
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats")
                            .child(senderReceiver)
                            .child("messages")
                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseDatabase.getReference()
                                    .child("chats")
                                    .child(receiverSender)
                                    .child("messages")
                                    .push()
                                    .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });
                    mGetMessage.setText(null);
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK){
                    Log.d("Back Button","back button pressed");
                    Fragment fragment = new DashboardFragment();
                    redirectToFragment(fragment);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Fragment fragment = new DashboardFragment();
        redirectToFragment(fragment);
    }

    private void redirectToFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.replace, fragment);
        fragmentTransaction.commit();
    }
}