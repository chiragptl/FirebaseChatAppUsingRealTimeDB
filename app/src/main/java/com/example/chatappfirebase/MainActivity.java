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

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragment = new LoginFragment();
        fragmentRedirect(fragment);
    }

    @Override
    protected void onStop() {
        super.onStop();
        checkLogout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogout();
        Log.d("user","please Login manually");
    }
    private void checkLogout(){
        if(fragment.getArguments()!=null){
            if(fragment.getArguments().getBoolean("logout")) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Log.d("logout","logout from mainactivity");
            }
        }
    }

    private void fragmentRedirect(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.replace, fragment);
        fragmentTransaction.commit();
    }
}