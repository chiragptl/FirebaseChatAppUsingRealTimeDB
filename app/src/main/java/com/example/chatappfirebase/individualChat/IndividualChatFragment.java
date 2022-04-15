package com.example.chatappfirebase.individualChat;

import static com.example.chatappfirebase.util.Constants.NAME;
import static com.example.chatappfirebase.util.Constants.RECEIVER_UID;
import static com.example.chatappfirebase.util.FragmentRedirect.redirectToFragment;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappfirebase.R;
import com.example.chatappfirebase.dashboard.DashboardFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class IndividualChatFragment extends Fragment {
    EditText mGetMessage;
    ImageButton mSendMessageButton;
    CardView mSendMessageCardView;
    androidx.appcompat.widget.Toolbar mToolbarOfSpecificChat;
    ImageView mImageviewOfSpecificUser;
    TextView mNameOfSpecificUser;
    ImageButton mBackButtonOfSpecificChat;
    RecyclerView mMessageRecyclerView;
    ProgressDialog progressBar;

    private String enteredMessage;
    String mReceiverName, mReceiverUid, mSenderUid;
    String senderReceiver, receiverSender;

    IndividualChatMessageAdapter messagesAdapter;

    private IndividualChatViewModel viewModel;
    private FirebaseAuth firebaseAuth;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(IndividualChatViewModel.class);

        firebaseAuth = FirebaseAuth.getInstance();
        mSenderUid = firebaseAuth.getUid();
        Bundle bundleData = getArguments();
        mReceiverUid = Objects.requireNonNull(bundleData).getString(RECEIVER_UID);
        mReceiverName = bundleData.getString(NAME);
        senderReceiver = mSenderUid + mReceiverUid;
        receiverSender = mReceiverUid + mSenderUid;
        viewModel.LoadChat(senderReceiver);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual_chat, container, false);

        mGetMessage = view.findViewById(R.id.getMessage);
        mSendMessageCardView = view.findViewById(R.id.cardViewOfSendMessage);
        mSendMessageButton = view.findViewById(R.id.imageViewSendMessage);
        mToolbarOfSpecificChat = view.findViewById(R.id.toolbarOfSpecificChat);
        mNameOfSpecificUser = view.findViewById(R.id.NameOfSpecificUser);
        mImageviewOfSpecificUser = view.findViewById(R.id.specificUserImageInImageView);
        mBackButtonOfSpecificChat = view.findViewById(R.id.backButtonOfSpecificChat);

        progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading Chats ...");
        progressBar.show();

        mMessageRecyclerView = view.findViewById(R.id.recyclerViewOfSpecific);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(linearLayoutManager);

        messagesAdapter = new IndividualChatMessageAdapter(getContext());

        viewModel.getMessage().observe(requireActivity(),messagesObserver);
        viewModel.getEditedMessage().observe(requireActivity(),messagesUpdateObserver);
        mMessageRecyclerView.setAdapter(messagesAdapter);


        ((AppCompatActivity) requireActivity()).setSupportActionBar(mToolbarOfSpecificChat);
        mToolbarOfSpecificChat.setOnClickListener(view1 -> Log.d("toolbar", "Toolbar is Clicked"));

        progressBar.dismiss();

        mBackButtonOfSpecificChat.setOnClickListener(view13 -> {
            Fragment fragment = new DashboardFragment();
            redirectToFragment(requireActivity().getSupportFragmentManager(), fragment);
        });

        mNameOfSpecificUser.setText(mReceiverName);
        Picasso.get().load(R.drawable.default_profile).into(mImageviewOfSpecificUser);

        mSendMessageButton.setOnClickListener(view12 -> {
            enteredMessage = mGetMessage.getText().toString();
            if (enteredMessage.isEmpty()) {
                Log.d("message", "Enter message first");
            } else {
                viewModel.sendMessage(enteredMessage, senderReceiver, receiverSender);
                mGetMessage.setText(null);
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

    @Override
    public void onResume() {
        super.onResume();
        requireView().setFocusableInTouchMode(true);
        requireView().requestFocus();
        requireView().setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                Log.d("Back Button", "back button pressed");
                Fragment fragment = new DashboardFragment();
                redirectToFragment(requireActivity().getSupportFragmentManager(), fragment);
                return true;
            }
            return false;
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Fragment fragment = new DashboardFragment();
        redirectToFragment(requireActivity().getSupportFragmentManager(), fragment);
    }

}