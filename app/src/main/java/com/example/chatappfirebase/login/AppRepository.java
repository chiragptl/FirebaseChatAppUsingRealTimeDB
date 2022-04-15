package com.example.chatappfirebase.login;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AppRepository {
    private final Application application;
    private static FirebaseAuth firebaseAuth;
    private final MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private final MutableLiveData<Boolean> userLoggedMutableLiveData;
    private final MutableLiveData<Boolean> userFromFirebaseMutableLiveData;
    static String verificationID, phoneNumber;
    private final Bundle authData;

    public AppRepository(Application application) {
        this.application = application;
        firebaseUserMutableLiveData = new MutableLiveData<>();
        userLoggedMutableLiveData = new MutableLiveData<>();
        userFromFirebaseMutableLiveData = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
        authData = new Bundle();

        if (firebaseAuth.getCurrentUser() != null){
            firebaseUserMutableLiveData.postValue(firebaseAuth.getCurrentUser());
        }
    }

    public void authenticateNumber(String phoneNumber, Activity activity) {

        AppRepository.phoneNumber = phoneNumber;
        Log.d("phone", AppRepository.phoneNumber + " from authenticateNumber");
        Log.d("getClass",application.getClass().toString());

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+91"+phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential)
        {
            final String code = credential.getSmsCode();
            if(code!=null)
            {
                firebaseUserMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                Log.d("autoLogin","AutoLogin");
            }
        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d("verificationFailed",e.getLocalizedMessage());
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token)
        {
            super.onCodeSent(s, token);
            verificationID = s;
            Log.d("code","code sent");
            Toast.makeText(application, "Code sent", Toast.LENGTH_SHORT).show();
            authData.putString("code",verificationID);
        }};

    public void authenticateNumberManually(String codeReceived, String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeReceived, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Log.d("Login","Login Successfully");
                        checkIsExistingUser();
                        firebaseUserMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                        userLoggedMutableLiveData.postValue(true);
                    }
                    else{
                        Log.d("Login", "Login Successfully not "+ Objects.requireNonNull(task.getException()).getLocalizedMessage());
                    }
                });
    }


    public void checkIsExistingUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseDatabase.getInstance().getReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Otp", "onChildAdded: " + snapshot.getKey());
                assert user != null;
                if (user.getUid().equalsIgnoreCase(snapshot.getKey()))
                    userFromFirebaseMutableLiveData.postValue(true);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
    }

    public Bundle getAuthData() {
        return authData;
    }

    public void signOut(){
        firebaseAuth.signOut();
        userLoggedMutableLiveData.postValue(false);
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public MutableLiveData<Boolean> getUserLoggedMutableLiveData() {
        return userLoggedMutableLiveData;
    }

    public MutableLiveData<Boolean> getUserFromFirebaseMutableLiveData() {
        return userFromFirebaseMutableLiveData;
    }

    public void updateUserStatus() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        String uid = firebaseAuth.getUid();
        assert uid != null;
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(uid);
        documentReference.update("status", "Online").addOnSuccessListener(aVoid -> Log.d("status", "Now User is Online"));

    }
}