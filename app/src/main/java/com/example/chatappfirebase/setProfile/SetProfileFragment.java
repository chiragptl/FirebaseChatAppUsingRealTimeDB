package com.example.chatappfirebase.setProfile;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chatappfirebase.R;
import com.example.chatappfirebase.dashboard.DashboardFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class SetProfileFragment extends Fragment {

    private ImageView mGetUserImageInImageView;
    private static final int PICK_IMAGE = 123;
    private Uri imagePath;
    private EditText mGetUserName;
    private FirebaseAuth firebaseAuth;
    private String name;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String ImageUriAccessToken;
    ProgressBar mProgressbarOfSetProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();



        mGetUserName = view.findViewById(R.id.getusername);
        CardView mGetUserImage = view.findViewById(R.id.getuserimage);
        mGetUserImageInImageView = view.findViewById(R.id.getuserimageinimageview);
        android.widget.Button mSaveProfile = view.findViewById(R.id.saveProfile);
        mProgressbarOfSetProfile = view.findViewById(R.id.progressbarofsetProfile);

        mGetUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });


        mSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = mGetUserName.getText().toString();
                if (name.isEmpty()) {
                    Log.d("empty", "Name is Empty");
                } else if (imagePath == null) {
                    Log.d("empty", "Image is Empty");
                } else {
                    mProgressbarOfSetProfile.setVisibility(View.VISIBLE);
                    sendDataToRealTimeDatabase();
                    mProgressbarOfSetProfile.setVisibility(View.INVISIBLE);
                    Log.d("working","now work on chatActivity");
                    Fragment fragment = new DashboardFragment();
                    fragmentRedirect(fragment);
                }
            }
        });
        return view;
    }

    private void sendDataToRealTimeDatabase() {
        name = mGetUserName.getText().toString().trim();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile mUserProfile = new UserProfile(name, firebaseAuth.getUid());
        databaseReference.setValue(mUserProfile);
        Log.d("profile added", "User Profile Added SuccessfulLy");
        sendImageToStorage();
    }

    private void sendImageToStorage() {
        sendDataToCloudFirestore();
    }
    private void sendDataToCloudFirestore() {

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name", name);
        userdata.put("image", ImageUriAccessToken);
        userdata.put("uid", firebaseAuth.getUid());
        userdata.put("status", "Online");

        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("success","Data on Cloud Firestore send success");
            }
        });
    }
    private void fragmentRedirect(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.replace, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imagePath = data.getData();
            mGetUserImageInImageView.setImageURI(imagePath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
