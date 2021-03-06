package com.example.chatappfirebase.login;

import static com.example.chatappfirebase.util.FragmentRedirect.redirectToFragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.chatappfirebase.R;
import com.example.chatappfirebase.otpAuthentication.OtpAuthenticationFragment;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {
    EditText phone;
    Button btnGenOTP;
    FirebaseAuth mAuth;

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);

        phone = view.findViewById(R.id.phone);
        btnGenOTP = view.findViewById(R.id.btnGenerateOTP);
        mAuth = FirebaseAuth.getInstance();

        loginViewModel.userLoggedMutableLiveData.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean){
                    loginViewModel.signOut();
                }
            }
        });
        btnGenOTP.setOnClickListener(view12 -> {
            if(TextUtils.isEmpty(phone.getText().toString()))
            {
                Toast.makeText(view12.getContext(), "Enter Valid Phone No.", Toast.LENGTH_SHORT).show();
            }
            else {
                String number = phone.getText().toString();
                Log.d("phone",number + " from user input");
                loginViewModel.authenticateNumber(number, getActivity());
                Fragment fragment = new OtpAuthenticationFragment();
                Bundle authData = loginViewModel.getAuthData();
                fragment.setArguments(authData);
                redirectToFragment(requireActivity().getSupportFragmentManager(), fragment);
            }
        });
        return view;
    }

}
