package com.example.chatappfirebase.dashboard;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.chatappfirebase.R;
import com.example.chatappfirebase.login.LoginFragment;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardFragment extends Fragment {

    TabLayout tabLayout;
    TabItem mChat;
    ViewPager2 viewPager;
    UserListAdapter userListAdapter;
    androidx.appcompat.widget.Toolbar mToolBar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tabLayout = view.findViewById(R.id.include);
        mChat = view.findViewById(R.id.chat);
        viewPager = view.findViewById(R.id.fragmentContainer);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        mToolBar =view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(mToolBar);

        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_more_vert_24);
        mToolBar.setOverflowIcon(drawable);

        userListAdapter = new UserListAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(userListAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 || tab.getPosition() == 1) {
                    Log.d("tab", "setOnTabSelectedListener" + tab.getPosition());
                    userListAdapter.notifyItemChanged(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addStatesFromChildren();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            Log.d("logout", "Logout");
            Fragment fragment = new LoginFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("logout", true);
            fragment.setArguments(bundle);
            redirectToFragment(fragment);
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getUid();
        assert uid != null;
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(uid);
        documentReference.update("status", "Online").addOnSuccessListener(aVoid -> Log.d("status", "Now User is Online"));
    }

    private void redirectToFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.replace, fragment);
        fragmentTransaction.commit();
    }
}
