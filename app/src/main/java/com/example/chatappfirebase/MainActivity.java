package com.example.chatappfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
        FragmentRedirect(fragment);
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
            FragmentRedirect(fragment);
        }
    }

    private void FragmentRedirect(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.replace, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}