package com.example.chatappfirebase.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.chatappfirebase.MemoryData;
import com.example.chatappfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {
    EditText name, phone, otp;
    Button btnGenOTP, btnVerify;
    FirebaseAuth mAuth;
    ProgressBar bar;

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    Navigation.findNavController(requireActivity(),R.id.action_loginFregment_to_dashboardFregment);
                    //redirect to
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fregment_login,container,false);

        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        otp = view.findViewById(R.id.otp);
        btnGenOTP = view.findViewById(R.id.btnGenerateOTP);
        btnVerify =view.findViewById(R.id.btnVerifyOTP);
        mAuth = FirebaseAuth.getInstance();
        bar = view.findViewById(R.id.bar);


        if (!MemoryData.getData(getActivity()).isEmpty()){

            // redirect to dashboard
        }

        btnGenOTP.setOnClickListener(view12 -> {
            if(TextUtils.isEmpty(phone.getText().toString()))
            {
                Toast.makeText(view12.getContext(), "Enter Valid Phone No.", Toast.LENGTH_SHORT).show();
            }
            else {
                String number = phone.getText().toString();
                bar.setVisibility(View.VISIBLE);
                loginViewModel.authenticateNumber(name.getText().toString(), number, getActivity());
                btnVerify.setEnabled(true);
                bar.setVisibility(View.INVISIBLE);
            }
        });

        btnVerify.setOnClickListener(view1 -> {
            if(TextUtils.isEmpty(otp.getText().toString()))
            {
                Toast.makeText(getActivity(), "Wrong OTP Entered", Toast.LENGTH_SHORT).show();
            }
            else {
                loginViewModel.authenticateNumberManually(otp.getText().toString());
            }
        });
        return view;
    }


}
