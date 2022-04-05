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

public class OtpAuthenticationFragment extends Fragment {

    TextView mchangenumber;
    EditText mgetotp;
    android.widget.Button mverifyotp;
    String enteredotp;
    FirebaseAuth firebaseAuth;
    ProgressBar mprogressbarofotpauth;
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

        mchangenumber = view.findViewById(R.id.changenumber);
        mverifyotp = view.findViewById(R.id.verifyotp);
        mgetotp = view.findViewById(R.id.getotp);
        mprogressbarofotpauth = view.findViewById(R.id.progressbarofotpauth);
        firebaseAuth = FirebaseAuth.getInstance();

        mchangenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new LoginFragment();
                fragmentRedirect(fragment);
            }
        });

        mverifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredotp = mgetotp.getText().toString();
                String phone = getArguments().getString("number");
                Log.d("phone",phone+" from bundle");
                if (enteredotp.isEmpty()) {
                    Log.d("otp", "Empty");
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String number = user.getPhoneNumber();
                        Log.d("phone",number +" from firebase user");
                        if(phone.equals(number)) {
                            Fragment fragment = new DashboardFragment();
                            fragmentRedirect(fragment);
                        }
                    }
                    mprogressbarofotpauth.setVisibility(View.VISIBLE);
                    String codeReceived = getArguments().getString("code");
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeReceived, enteredotp);
                    signInWithCredential(credential);
                }
            }
        });
        return view;
    }

    private void fragmentRedirect(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.replace, fragment);
        fragmentTransaction.commit();
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("login", "Login Successfully");
                        Fragment fragment = new SetProfileFragment();
                        Bundle authData = loginViewModel.getAuthData();
                        fragment.setArguments(authData);
                        fragmentRedirect(fragment);
                    } else {
                        Log.d("login", "Login failed");
                    }
                });
    }
}
