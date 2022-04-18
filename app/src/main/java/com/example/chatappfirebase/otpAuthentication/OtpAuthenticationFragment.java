package com.example.chatappfirebase.otpAuthentication;

import static com.example.chatappfirebase.util.FragmentRedirect.redirectToFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chatappfirebase.R;
import com.example.chatappfirebase.dashboard.DashboardFragment;
import com.example.chatappfirebase.login.LoginFragment;
import com.example.chatappfirebase.login.LoginViewModel;
import com.example.chatappfirebase.setProfile.SetProfileFragment;

public class OtpAuthenticationFragment extends Fragment {

    TextView changeNumber;
    EditText getOtp;
    Button verifyOtp;
    String enteredOtp;
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

        changeNumber = view.findViewById(R.id.changeNumber);
        verifyOtp = view.findViewById(R.id.verifyOtp);
        getOtp = view.findViewById(R.id.getOtp);

        changeNumber.setOnClickListener(view1 -> {
            Fragment fragment = new LoginFragment();
            redirectToFragment(requireActivity().getSupportFragmentManager(), fragment);
        });

        verifyOtp.setOnClickListener(view12 -> {
            enteredOtp = getOtp.getText().toString();
            assert getArguments() != null;
            String codeReceived = getArguments().getString("code");
            if (enteredOtp.isEmpty() || !codeReceived.equalsIgnoreCase(enteredOtp)) {
                Log.d("otp", "Empty");
                Toast.makeText(getContext(), "Enter Valid OTP No.", Toast.LENGTH_SHORT).show();
            } else {
                authViewModel.authenticateNumberManually(codeReceived,enteredOtp);
                authViewModel.userFromFirebaseMutableLiveData.observe(requireActivity(), aBoolean -> {
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
                });
                }
            });
        return view;
    }
}
