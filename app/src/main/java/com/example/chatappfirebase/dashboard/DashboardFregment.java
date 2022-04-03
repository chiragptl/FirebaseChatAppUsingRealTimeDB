package com.example.chatappfirebase.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappfirebase.MemoryData;
import com.example.chatappfirebase.R;
import com.example.chatappfirebase.login.LoginViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardFregment extends Fragment {

    //dashboard layout element
    private final List<MessagesList> messagesLists = new ArrayList<>();
    private RecyclerView messagesRecycleView;
    private DashboardViewModel dashboardViewModel;
    String number, name;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mvvmchatapp-76ac2-default-rtdb.firebaseio.com/");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getActivity().getIntent();
        number = i.getStringExtra("phone");
        name = i.getStringExtra("name");

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        dashboardViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){

                }
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fregment_dashboard,container,false);


        messagesRecycleView.setHasFixedSize(true);
        messagesRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesLists.clear();
                for (DataSnapshot dataSnapshot: snapshot.child("users").getChildren()){
                    final String getMobile = dataSnapshot.getKey();
                    if(!getMobile.equals(name)) {
                        final String getName = dataSnapshot.child("name").getValue(String.class);

                        MessagesList messagesList = new MessagesList(getName,getMobile,"",0);
                        messagesLists.add(messagesList);

                    }
                }
                messagesRecycleView.setAdapter(new MessagesAdapter(messagesLists, getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}
