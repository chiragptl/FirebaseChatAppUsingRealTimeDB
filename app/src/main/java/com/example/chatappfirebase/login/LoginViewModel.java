package com.example.chatappfirebase.login;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends AndroidViewModel {
    private final AppRepository appRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);

        appRepository = new AppRepository(application);
        userMutableLiveData = appRepository.getFirebaseUserMutableLiveData();

    }

    public void authenticateNumber(String number, Activity activity){
        appRepository.authenticateNumber(number,activity);
    }

    public void authenticateNumberManually(String number){
        appRepository.authenticateNumberManually(number);

    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}
