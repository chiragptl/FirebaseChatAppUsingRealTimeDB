package com.example.chatappfirebase.login;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends AndroidViewModel {
    private final AppRepository appRepository;
    public final MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    public final MutableLiveData<Boolean> userLoggedMutableLiveData;
    public final MutableLiveData<Boolean> userFromFirebaseMutableLiveData;
    public LoginViewModel(@NonNull Application application) {
        super(application);

        appRepository = new AppRepository(application);
        firebaseUserMutableLiveData = appRepository.getFirebaseUserMutableLiveData();
        userLoggedMutableLiveData = appRepository.getUserLoggedMutableLiveData();
        userFromFirebaseMutableLiveData = appRepository.getUserFromFirebaseMutableLiveData();
    }

    public void authenticateNumber(String number, Activity activity){
        appRepository.authenticateNumber( number,activity);
    }

    public void authenticateNumberManually(String codeReceived, String code){
        appRepository.authenticateNumberManually(codeReceived, code);
    }

    public Bundle getAuthData() {
        return appRepository.getAuthData();
    }

    public void signOut(){
        appRepository.signOut();
    }
}
