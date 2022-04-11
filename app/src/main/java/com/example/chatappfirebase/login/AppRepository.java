package com.example.chatappfirebase.login;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AppRepository {
    private final Application application;
    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private final MutableLiveData<Boolean> logoutMutableLiveData;
    private final MutableLiveData<Boolean> loginMutableLiveData;
    String verificationID, phoneNumber;
    private final Bundle authData;

    public AppRepository(Application application) {
        this.application = application;
        firebaseUserMutableLiveData = new MutableLiveData<>();
        logoutMutableLiveData = new MutableLiveData<>();
        loginMutableLiveData = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
        authData = new Bundle();
    }

    public void authenticateNumber(String phoneNumber, Activity activity) {
        this.phoneNumber = phoneNumber;
        Log.d("phone", this.phoneNumber + " from authenticateNumber");
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
//                authenticateNumberManually(code);
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
            Log.d("phone",phoneNumber + " from after code sent");
            authData.putString("code",verificationID);
            authData.putBoolean("logout",false);
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
                        Log.d("Login","Login Successfully");
                        loginMutableLiveData.postValue(true);
                    }
                    else{
                        Toast.makeText(application, "Login Successfully not", Toast.LENGTH_SHORT).show();
                        loginMutableLiveData.postValue(false);
                    }
                });
    }

    public MutableLiveData<Boolean> getLoginMutableLiveData() {
        return loginMutableLiveData;
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