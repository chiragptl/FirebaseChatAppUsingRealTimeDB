package com.example.chatappfirebase.login;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class LoginViewModel extends AndroidViewModel {
    private final AppRepository appRepository;


    public LoginViewModel(@NonNull Application application) {
        super(application);

        appRepository = new AppRepository(application);
    }

    public void authenticateNumber(String number, Activity activity){
        appRepository.authenticateNumber( number,activity);
    }


    public Bundle getAuthData() {
        return appRepository.getAuthData();
    }

}
