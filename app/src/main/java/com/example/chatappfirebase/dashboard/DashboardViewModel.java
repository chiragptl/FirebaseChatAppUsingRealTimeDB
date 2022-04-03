package com.example.chatappfirebase.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.chatappfirebase.login.AppRepository;
import com.google.firebase.auth.FirebaseUser;

public class DashboardViewModel extends AndroidViewModel {
    private final AppRepository appRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    private final MutableLiveData<Boolean> logoutMutableLiveData;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        this.appRepository = new AppRepository(application);
        this.userMutableLiveData = this.appRepository.getFirebaseUserMutableLiveData();
        logoutMutableLiveData = this.appRepository.getLogoutMutableLiveData();
    }

    public void logout(){
        appRepository.logOut();
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getLogoutMutableLiveData() {
        return logoutMutableLiveData;
    }
}
