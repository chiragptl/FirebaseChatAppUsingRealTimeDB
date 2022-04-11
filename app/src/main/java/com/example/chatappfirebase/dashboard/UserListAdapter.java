package com.example.chatappfirebase.dashboard;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chatappfirebase.chat.ChatFragment;

public class UserListAdapter extends FragmentStateAdapter {
    final int tabCount = 1;

    public UserListAdapter(@NonNull FragmentManager fm, Lifecycle lifecycle) {
        super(fm,lifecycle);
        Log.d("PagerAdapter", "PagerAdapter: called");
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            Log.d("tab1", "ChatFragment");
            return new ChatFragment();
        }
        return new DashboardFragment();
    }

    @Override
    public int getItemCount() {
        return tabCount;
    }
}
