package com.example.chatappfirebase.setProfile;

import static com.example.chatappfirebase.util.Constants.IMAGE;
import static com.example.chatappfirebase.util.Constants.NAME;
import static com.example.chatappfirebase.util.Constants.ONLINE;
import static com.example.chatappfirebase.util.Constants.STATUS;
import static com.example.chatappfirebase.util.Constants.UID;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SetProfileRepository {
    private static SetProfileRepository setProfileRepository;
    private final String userId;
    private final FirebaseFirestore firebaseFirestore;
    private final MutableLiveData<Boolean> profileAdded;

    public MutableLiveData<Boolean> getProfileAdded() {
        return profileAdded;
    }

    public static SetProfileRepository getInstance(Application application){
        if(setProfileRepository == null){
            setProfileRepository = new SetProfileRepository(application);
        }
        return setProfileRepository;
    }
    public SetProfileRepository(Application application) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        profileAdded = new MutableLiveData<>();
    }

    public void saveProfile(String name) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(Objects.requireNonNull(userId));
        UserProfile mUserProfile = new UserProfile(name, userId);
        databaseReference.setValue(mUserProfile);
        Log.d("profile added", "User Profile Added SuccessfulLy");
        uploadDataToCloudFireStore(name);
        profileAdded.setValue(true);
    }

    private void uploadDataToCloudFireStore(String name) {
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(Objects.requireNonNull(userId));
        Map<String, Object> userdata = new HashMap<>();
        userdata.put(NAME, name);
        userdata.put(IMAGE, "");
        userdata.put(UID, userId);
        userdata.put(STATUS, ONLINE);
        documentReference.set(userdata).addOnSuccessListener(aVoid -> Log.d("success", "Data on Cloud Firestore send success"));
    }
}
