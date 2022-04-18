package com.example.chatappfirebase.dashboard;

import static com.example.chatappfirebase.util.FragmentRedirect.redirectToFragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.chatappfirebase.R;
import com.example.chatappfirebase.login.LoginFragment;
import com.example.chatappfirebase.login.LoginViewModel;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class DashboardFragment extends Fragment {

    TabLayout tabLayout;
    TabItem chatTab;
    ViewPager2 viewPager;
    UserListAdapter userListAdapter;
    Toolbar toolBar;
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tabLayout = view.findViewById(R.id.include);
        chatTab = view.findViewById(R.id.chat);
        viewPager = view.findViewById(R.id.fragmentContainer);

        toolBar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolBar);

        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_more_vert_24);
        toolBar.setOverflowIcon(drawable);

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
            loginViewModel.signOut();
            Fragment fragment = new LoginFragment();
            redirectToFragment(requireActivity().getSupportFragmentManager(), fragment);
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        loginViewModel.updateUserStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
        requireView().setFocusableInTouchMode(true);
        requireView().requestFocus();
        requireView().setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_BACK) {
                Log.d("Back Button", "back button pressed");
                requireActivity().finish();
                return true;
            }
            return false;
        });
    }
}
