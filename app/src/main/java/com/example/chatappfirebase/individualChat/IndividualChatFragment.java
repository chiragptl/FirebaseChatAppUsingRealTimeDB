package com.example.chatappfirebase.individualChat;

import static com.example.chatappfirebase.util.Constants.NAME;
import static com.example.chatappfirebase.util.Constants.RECEIVER_UID;
import static com.example.chatappfirebase.util.FragmentRedirect.redirectToFragment;

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
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappfirebase.R;
import com.example.chatappfirebase.dashboard.DashboardFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Objects;

public class IndividualChatFragment extends Fragment {
    EditText getMessage;
    ImageButton sendMessageButton;
    CardView sendMessageCardView;
    Toolbar toolbarOfSpecificChat;
    ImageView imageviewOfSpecificUser;
    TextView nameOfSpecificUser;
    ImageButton backButtonOfSpecificChat;
    RecyclerView messageRecyclerView;

    private String enteredMessage;
    String receiverName, receiverUid, senderUid;
    String senderReceiver, receiverSender;

    IndividualChatMessageAdapter messagesAdapter;

    private IndividualChatViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(IndividualChatViewModel.class);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        senderUid = firebaseAuth.getUid();
        Bundle bundleData = getArguments();
        receiverUid = Objects.requireNonNull(bundleData).getString(RECEIVER_UID);
        receiverName = bundleData.getString(NAME);
        senderReceiver = senderUid + receiverUid;
        receiverSender = receiverUid + senderUid;
        viewModel.LoadChat(senderReceiver);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual_chat, container, false);

        getMessage = view.findViewById(R.id.getMessage);
        sendMessageCardView = view.findViewById(R.id.cardViewOfSendMessage);
        sendMessageButton = view.findViewById(R.id.imageViewSendMessage);
        toolbarOfSpecificChat = view.findViewById(R.id.toolbarOfSpecificChat);
        nameOfSpecificUser = view.findViewById(R.id.NameOfSpecificUser);
        imageviewOfSpecificUser = view.findViewById(R.id.specificUserImageInImageView);
        backButtonOfSpecificChat = view.findViewById(R.id.backButtonOfSpecificChat);

        messageRecyclerView = view.findViewById(R.id.recyclerViewOfSpecific);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(linearLayoutManager);

        messagesAdapter = new IndividualChatMessageAdapter(getContext());

        viewModel.getMessage().observe(requireActivity(),messagesObserver);
        viewModel.getEditedMessage().observe(requireActivity(),messagesUpdateObserver);
        viewModel.getSentText().observe(requireActivity(),sentText);
        messageRecyclerView.setAdapter(messagesAdapter);


        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbarOfSpecificChat);
        toolbarOfSpecificChat.setOnClickListener(view1 -> Log.d("toolbar", "Toolbar is Clicked"));


        backButtonOfSpecificChat.setOnClickListener(view13 -> {
            Fragment fragment = new DashboardFragment();
            redirectToFragment(requireActivity().getSupportFragmentManager(), fragment);
        });

        nameOfSpecificUser.setText(receiverName);
        Picasso.get().load(R.drawable.default_profile).into(imageviewOfSpecificUser);

        sendMessageButton.setOnClickListener(view12 -> {
            enteredMessage = getMessage.getText().toString();
            if (enteredMessage.isEmpty()) {
                Log.d("message", "Enter message first");
            } else {
                viewModel.sendMessage(enteredMessage, senderReceiver, receiverSender);
            }
        });
        return view;
    }

    Observer<ArrayList<Messages>> messagesObserver = new Observer<ArrayList<Messages>>() {
        @Override
        public void onChanged(ArrayList<Messages> messages) {
            messagesAdapter.addAll(messages);
        }
    };

    Observer<Messages> messagesUpdateObserver = new Observer<Messages>() {
        @Override
        public void onChanged(Messages messages) {
            int index = messagesAdapter.findIndex(messages.getSenderId(),messages.getTimestamp());
            messagesAdapter.update(messages, index);
        }
    };

    Observer<String> sentText = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            getMessage.setText(s);
        }
    };

}