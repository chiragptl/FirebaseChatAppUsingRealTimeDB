package com.example.chatappfirebase.setProfile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class SetProfileViewModel extends AndroidViewModel {

    private final SetProfileRepository setProfileRepository;
    public final MutableLiveData<Boolean> profileAdded;

    public SetProfileViewModel(@NonNull Application application) {
        super(application);
        setProfileRepository = SetProfileRepository.getInstance(application);
        profileAdded = new MutableLiveData<>();
    }

    public void saveProfile(String name) {
        setProfileRepository.saveProfile(name);
    }
}
