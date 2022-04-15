package com.example.chatappfirebase.setProfile;

import static com.example.chatappfirebase.util.FragmentRedirect.redirectToFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.chatappfirebase.R;
import com.example.chatappfirebase.dashboard.DashboardFragment;

public class SetProfileFragment extends Fragment {

    private ImageView mGetUserImageInImageView;
    private EditText mGetUserName;
    private String name;
    private MyLifecycleObserver mObserver;
    private SetProfileViewModel setProfileViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProfileViewModel = new ViewModelProvider(requireActivity()).get(SetProfileViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_profile, container, false);

        mGetUserName = view.findViewById(R.id.getUserName);
        Button mSaveProfile = view.findViewById(R.id.saveProfile);
        CardView mGetUserImage = view.findViewById(R.id.getUserImage);
        mGetUserImageInImageView = view.findViewById(R.id.getUserImageInImageView);

        mObserver = new MyLifecycleObserver(requireActivity().getActivityResultRegistry());
        getLifecycle().addObserver(mObserver);

        mGetUserImage.setOnClickListener(view1 -> mObserver.selectImage());

        mSaveProfile.setOnClickListener(view12 -> {
            name = mGetUserName.getText().toString();
            if (name.isEmpty()) {
                Log.d("empty", "Name is Empty");
            } else {
                setProfileViewModel.saveProfile(mGetUserName.getText().toString());
                Log.d("working", "now work on chatActivity");
                setProfileViewModel.profileAdded.observe(requireActivity(), aBoolean -> {
                    if (aBoolean) {
                        Fragment fragment = new DashboardFragment();
                        redirectToFragment(requireActivity().getSupportFragmentManager(), fragment);
                    }
                });
            }
        });
        return view;
    }

    class MyLifecycleObserver implements DefaultLifecycleObserver {
        private final ActivityResultRegistry mRegistry;
        private ActivityResultLauncher<String> mGetContent;

        MyLifecycleObserver(@NonNull ActivityResultRegistry registry) {
            mRegistry = registry;
        }

        public void onCreate(@NonNull LifecycleOwner owner) {
            mGetContent = mRegistry.register("key", owner, new ActivityResultContracts.GetContent(),
                    uri -> mGetUserImageInImageView.setImageURI(uri));
        }

        public void selectImage() {
            mGetContent.launch("image/*");
        }
    }
}
