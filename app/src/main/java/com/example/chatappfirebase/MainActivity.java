package com.example.chatappfirebase;

import static com.example.chatappfirebase.util.FragmentRedirect.redirectToFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.example.chatappfirebase.dashboard.DashboardFragment;
import com.example.chatappfirebase.databinding.ActivityMainBinding;
import com.example.chatappfirebase.login.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Fragment fragment = new LoginFragment();
        redirectToFragment(getSupportFragmentManager(),fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String number = user.getPhoneNumber();
            Log.d("phone",number +" from firebase user on resume");
            Fragment fragment = new DashboardFragment();
            redirectToFragment(getSupportFragmentManager(), fragment);
        }
    }

}