package com.example.chatappfirebase.otpAuthentication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.chatappfirebase.R;
import com.example.chatappfirebase.dashboard.DashboardFragment;
import com.example.chatappfirebase.login.LoginFragment;
import com.example.chatappfirebase.login.LoginViewModel;
import com.example.chatappfirebase.setProfile.SetProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

public class OtpAuthenticationFragment extends Fragment {

    TextView mChangeNumber;
    EditText mGetOtp;
    android.widget.Button mVerifyOtp;
    String enteredOtp;
    FirebaseAuth firebaseAuth;
    ProgressBar mProgressbarOfOtpAuth;
    private LoginViewModel loginViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp_authentication, container, false);

        mChangeNumber = view.findViewById(R.id.changeNumber);
        mVerifyOtp = view.findViewById(R.id.verifyOtp);
        mGetOtp = view.findViewById(R.id.getOtp);
        mProgressbarOfOtpAuth = view.findViewById(R.id.progressBarOfOtpAuth);
        firebaseAuth = FirebaseAuth.getInstance();


        mChangeNumber.setOnClickListener(view1 -> {
            Fragment fragment = new LoginFragment();
            fragmentRedirect(fragment);
        });

        mVerifyOtp.setOnClickListener(view12 -> {
            enteredOtp = mGetOtp.getText().toString();
            if (enteredOtp.isEmpty()) {
                Log.d("otp", "Empty");
            } else {
                mProgressbarOfOtpAuth.setVisibility(View.VISIBLE);
                assert getArguments() != null;
                String codeReceived = getArguments().getString("code");
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeReceived, enteredOtp);
                signInWithCredential(credential);
            }
        });
        return view;
    }

    private void fragmentRedirect(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.replace, fragment);
        fragmentTransaction.commit();
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("login", "Login Successfully");
                        checkIsExistingUser();
                    } else {
                        Log.d("login", "Login failed");
                    }
                });
    }

    private void checkIsExistingUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseDatabase.getInstance().getReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Otp", "onChildAdded: " + snapshot.getKey());
                assert user != null;
                if (user.getUid().equalsIgnoreCase(snapshot.getKey())) {
                    Fragment fragment = new DashboardFragment();
                    Bundle authData = loginViewModel.getAuthData();
                    fragment.setArguments(authData);
                    fragmentRedirect(fragment);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Fragment fragment = new SetProfileFragment();
        Bundle authData = loginViewModel.getAuthData();
        fragment.setArguments(authData);
        fragmentRedirect(fragment);
    }
}
