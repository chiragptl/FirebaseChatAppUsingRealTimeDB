package com.example.chatappfirebase.otpAuthentication;

import static com.example.chatappfirebase.util.FragmentRedirect.redirectToFragment;

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
import androidx.lifecycle.Observer;
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
    ProgressBar mProgressbarOfOtpAuth;
    private LoginViewModel authViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp_authentication, container, false);

        mChangeNumber = view.findViewById(R.id.changeNumber);
        mVerifyOtp = view.findViewById(R.id.verifyOtp);
        mGetOtp = view.findViewById(R.id.getOtp);
        mProgressbarOfOtpAuth = view.findViewById(R.id.progressBarOfOtpAuth);

        mChangeNumber.setOnClickListener(view1 -> {
            Fragment fragment = new LoginFragment();
            redirectToFragment(requireActivity().getSupportFragmentManager(), fragment);
        });

        mVerifyOtp.setOnClickListener(view12 -> {
            enteredOtp = mGetOtp.getText().toString();
            if (enteredOtp.isEmpty()) {
                Log.d("otp", "Empty");
            } else {
                mProgressbarOfOtpAuth.setVisibility(View.VISIBLE);
                assert getArguments() != null;
                String codeReceived = getArguments().getString("code");

                authViewModel.authenticateNumberManually(codeReceived,enteredOtp);
                authViewModel.userFromFirebaseMutableLiveData.observe(requireActivity(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            Fragment fragment;
                            if(aBoolean){
                                fragment = new DashboardFragment();
                                Bundle authData = authViewModel.getAuthData();
                                fragment.setArguments(authData);
                            }
                            else {
                                fragment = new SetProfileFragment();
                                Bundle authData = authViewModel.getAuthData();
                                fragment.setArguments(authData);
                            }
                            redirectToFragment(requireActivity().getSupportFragmentManager(), fragment);
                        }
                    });
                }
            });
        return view;
    }

//    private void signInWithCredential(PhoneAuthCredential credential) {
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Log.d("login", "Login Successfully");
//                    } else {
//                        Log.d("login", "Login failed");
//                    }
//                });
//    }

//    private void checkIsExistingUser() {
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        FirebaseDatabase.getInstance().getReference().addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Log.d("Otp", "onChildAdded: " + snapshot.getKey());
//                assert user != null;
//                if (user.getUid().equalsIgnoreCase(snapshot.getKey())) {
//
//                }
//            }
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
////        fragmentRedirect(fragment);
//    }

}
