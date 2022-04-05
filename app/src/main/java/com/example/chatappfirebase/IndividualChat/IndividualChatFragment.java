package com.example.chatappfirebase.IndividualChat;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class IndividualChatFragment extends Fragment {
    EditText mgetmessage;
    ImageButton msendmessagebutton;

    CardView msendmessagecardview;
    androidx.appcompat.widget.Toolbar mtoolbarofspecificchat;
    ImageView mimageviewofspecificuser;
    TextView mnameofspecificuser;

    private String enteredmessage;
    String mrecievername, sendername, mrecieveruid, msenderuid;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String senderroom, recieverroom;

    ImageButton mbackbuttonofspecificchat;

    RecyclerView mmessagerecyclerview;

    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    IndividualChatMessageAdapter messagesAdapter;
    ArrayList<Messages> messagesArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual_chat, container, false);

        mgetmessage = view.findViewById(R.id.getmessage);
        msendmessagecardview = view.findViewById(R.id.carviewofsendmessage);
        msendmessagebutton = view.findViewById(R.id.imageviewsendmessage);
        mtoolbarofspecificchat = view.findViewById(R.id.toolbarofspecificchat);
        mnameofspecificuser = view.findViewById(R.id.Nameofspecificuser);
        mimageviewofspecificuser = view.findViewById(R.id.specificuserimageinimageview);
        mbackbuttonofspecificchat = view.findViewById(R.id.backbuttonofspecificchat);

        messagesArrayList = new ArrayList<>();
        mmessagerecyclerview = view.findViewById(R.id.recyclerviewofspecific);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        mmessagerecyclerview.setLayoutManager(linearLayoutManager);

        messagesAdapter = new IndividualChatMessageAdapter(getContext(), messagesArrayList);
        mmessagerecyclerview.setAdapter(messagesAdapter);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbarofspecificchat);
        mtoolbarofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("toolbar", "Toolbar is Clicked");
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");

        msenderuid = firebaseAuth.getUid();
        mrecieveruid = getArguments().getString("receiveruid");
        mrecievername = getArguments().getString("name");
        senderroom = msenderuid + mrecieveruid;
        recieverroom = mrecieveruid + msenderuid;

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("chats").child(senderroom).child("messages");
        messagesAdapter = new IndividualChatMessageAdapter(getContext(), messagesArrayList);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Messages messages = snapshot1.getValue(Messages.class);
                    Log.d("message",messages.getMessage()+" "+messages.getSenderId());
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        mbackbuttonofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new DashboardFragment();
                redirectToFragment(fragment);
            }
        });

        mnameofspecificuser.setText(mrecievername);
        Picasso.get().load(R.drawable.defaultprofile).into(mimageviewofspecificuser);
        msendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredmessage = mgetmessage.getText().toString();
                if (enteredmessage.isEmpty()) {
                    Log.d("message", "Enter message first");
                } else {
                    Date date = new Date();
                    currenttime = simpleDateFormat.format(calendar.getTime());
                    Messages messages = new Messages(enteredmessage, firebaseAuth.getUid(), date.getTime(), currenttime);
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats")
                            .child(senderroom)
                            .child("messages")
                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseDatabase.getReference()
                                    .child("chats")
                                    .child(recieverroom)
                                    .child("messages")
                                    .push()
                                    .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });
                    mgetmessage.setText(null);
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (messagesAdapter != null) {
            messagesAdapter.notifyDataSetChanged();
        }
    }

    private void redirectToFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.replace, fragment);
        fragmentTransaction.commit();
    }
}

