package com.example.chatappfirebase.login;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.chatappfirebase.MemoryData;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class AppRepository {
    private final Application application;
    private final FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private final MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private final MutableLiveData<Boolean> logoutMutableLiveData;
    private final MutableLiveData<Boolean> logedMutableLiveData;
    Activity activity;
    String verificationID;
    String phoneNumber;
    String name;
    Bundle authData;

    public AppRepository(Application application) {
        this.application = application;
        firebaseUserMutableLiveData = new MutableLiveData<>();
        logoutMutableLiveData = new MutableLiveData<>();
        logedMutableLiveData = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mvvmchatapp-76ac2-default-rtdb.firebaseio.com/");
        authData = new Bundle();
    }

    public void authenticateNumber(String name, String phoneNumber, Activity activity) {
        this.activity = activity;
        this.name = name;
        this.phoneNumber = phoneNumber;
        Log.d("getClass",application.getClass().toString());
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+91"+phoneNumber)  // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
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
                authenticateNumberManually(code);
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
            authData.putString("number",phoneNumber);
            authData.putString("code",verificationID);
        }};

    public Bundle getAuthData() {
        return authData;
    }

    public void authenticateNumberManually(String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signInWithCredential(credential);
        
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(!snapshot.child("users").hasChild(phoneNumber)){
                                    databaseReference.child("users").child(phoneNumber).child("number").setValue(phoneNumber);
                                    databaseReference.child("users").child(phoneNumber).child("name").setValue(name);
//                                    MemoryData.saveData(phoneNumber, activity);
//                                    MemoryData.saveName(name, activity);
                                    Log.d("databaseReference","added into firebase realtime");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Log.d("Login","Login Successfully");
                        logedMutableLiveData.postValue(true);

                    }
                    else{
                        Toast.makeText(application, "Login Successfully not", Toast.LENGTH_SHORT).show();
                        logedMutableLiveData.postValue(false);
                    }
                });
    }

    public MutableLiveData<Boolean> getLogedMutableLiveData() {
        return logedMutableLiveData;
    }

    public void logOut(){
        firebaseAuth.signOut();
        logoutMutableLiveData.postValue(true);
    }
    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public MutableLiveData<Boolean> getLogoutMutableLiveData() {
        return logoutMutableLiveData;
    }
}