package com.example.chatappfirebase.login;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends AndroidViewModel {
    private final AppRepository appRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    private final MutableLiveData<Boolean> loginMutableLiveData;
    private final Bundle authData;


    public LoginViewModel(@NonNull Application application) {
        super(application);

        appRepository = new AppRepository(application);
        userMutableLiveData = appRepository.getFirebaseUserMutableLiveData();
        loginMutableLiveData = new MutableLiveData<>();
        authData = new Bundle();
    }

    public void authenticateNumber(String name, String number, Activity activity){
        appRepository.authenticateNumber(name, number,activity);
    }

    public void authenticateNumberManually(String number){
        appRepository.authenticateNumberManually(number);
        if(appRepository.getLoginMutableLiveData().getValue().equals(true)){
            loginMutableLiveData.postValue(true);
        }
        else {
            loginMutableLiveData.postValue(true);
        }
    }

    public Bundle getAuthData() {
        return appRepository.getAuthData();
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoginMutableLiveData() {
        return loginMutableLiveData;
    }
}
