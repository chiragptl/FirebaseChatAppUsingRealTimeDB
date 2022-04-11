package com.example.chatappfirebase.setProfile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.chatappfirebase.R;
import com.example.chatappfirebase.dashboard.DashboardFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SetProfileFragment extends Fragment {

    private ImageView mGetUserImageInImageView;
    private EditText mGetUserName;
    private FirebaseAuth firebaseAuth;
    private String name;
    private FirebaseFirestore firebaseFirestore;
    ProgressBar mProgressbarOfSetProfile;

    private MyLifecycleObserver mObserver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_profile, container, false);

        mObserver = new MyLifecycleObserver(requireActivity().getActivityResultRegistry());
        getLifecycle().addObserver(mObserver);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mGetUserName = view.findViewById(R.id.getUserName);
        CardView mGetUserImage = view.findViewById(R.id.getUserImage);
        mGetUserImageInImageView = view.findViewById(R.id.getUserImageInImageView);
        android.widget.Button mSaveProfile = view.findViewById(R.id.saveProfile);
        mProgressbarOfSetProfile = view.findViewById(R.id.progressBarOfSetProfile);

        mGetUserImage.setOnClickListener(view1 -> mObserver.selectImage());

        mSaveProfile.setOnClickListener(view12 -> {
            name = mGetUserName.getText().toString();
            if (name.isEmpty()) {
                Log.d("empty", "Name is Empty");
            } else {
                mProgressbarOfSetProfile.setVisibility(View.VISIBLE);
                sendDataToRealTimeDatabase();
                mProgressbarOfSetProfile.setVisibility(View.INVISIBLE);
                Log.d("working", "now work on chatActivity");
                Fragment fragment = new DashboardFragment();
                fragmentRedirect(fragment);
            }
        });
        return view;
    }

    private void sendDataToRealTimeDatabase() {
        name = mGetUserName.getText().toString().trim();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(Objects.requireNonNull(firebaseAuth.getUid()));
        UserProfile mUserProfile = new UserProfile(name, firebaseAuth.getUid());
        databaseReference.setValue(mUserProfile);
        Log.d("profile added", "User Profile Added SuccessfulLy");
        sendImageToStorage();
    }

    private void sendImageToStorage() {
        sendDataToCloudFirestore();
    }

    private void sendDataToCloudFirestore() {

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(Objects.requireNonNull(firebaseAuth.getUid()));
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name", name);
        userdata.put("image", "");
        userdata.put("uid", firebaseAuth.getUid());
        userdata.put("status", "Online");

        documentReference.set(userdata).addOnSuccessListener(aVoid -> Log.d("success", "Data on Cloud Firestore send success"));
    }

    private void fragmentRedirect(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.replace, fragment);
        fragmentTransaction.commit();
    }


    class MyLifecycleObserver implements DefaultLifecycleObserver {
        private final ActivityResultRegistry mRegistry;
        private ActivityResultLauncher<String> mGetContent;

        MyLifecycleObserver(@NonNull ActivityResultRegistry registry) {
            mRegistry = registry;
        }

        public void onCreate(@NonNull LifecycleOwner owner) {
            mGetContent = mRegistry.register("key", owner, new ActivityResultContracts.GetContent(),
                    uri -> mGetUserImageInImageView.setImageURI(uri));
        }

        public void selectImage() {
            mGetContent.launch("image/*");
        }
    }
}
