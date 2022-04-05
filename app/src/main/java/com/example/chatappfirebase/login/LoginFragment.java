package com.example.chatappfirebase.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.chatappfirebase.R;
import com.example.chatappfirebase.otpAuthentication.OtpAuthenticationFragment;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {
    EditText phone;
    Button btnGenOTP;
    FirebaseAuth mAuth;
    ProgressBar bar;

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLogout();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
//        loginViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
//            @Override
//            public void onChanged(FirebaseUser firebaseUser) {
//                if(firebaseUser != null){
//                    Log.d("user","onChange Already Log In");
//                    Fragment fragment = new DashboardFragment();
//                    redirectToFragment(fragment);
//                }
//            }
//        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);

        phone = view.findViewById(R.id.phone);
        btnGenOTP = view.findViewById(R.id.btnGenerateOTP);
        mAuth = FirebaseAuth.getInstance();
        bar = view.findViewById(R.id.bar);

        btnGenOTP.setOnClickListener(view12 -> {
            if(TextUtils.isEmpty(phone.getText().toString()))
            {
                Toast.makeText(view12.getContext(), "Enter Valid Phone No.", Toast.LENGTH_SHORT).show();
            }
            else {
                String number = phone.getText().toString();
                bar.setVisibility(View.VISIBLE);
                loginViewModel.authenticateNumber("null", number, getActivity());
                bar.setVisibility(View.INVISIBLE);
                Fragment fragment = new OtpAuthenticationFragment();
                Bundle authData = loginViewModel.getAuthData();
                fragment.setArguments(authData);
                redirectToFragment(fragment);
            }
        });
        return view;
    }

    private void redirectToFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.replace, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onStart() {
        super.onStart();
        checkLogout();
    }

    @Override
    public void onStop() {
        super.onStop();
        checkLogout();
    }

    private void checkLogout(){
        if(getArguments()!=null){
            if(getArguments().getBoolean("logout")) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
            }
        }
    }
}
