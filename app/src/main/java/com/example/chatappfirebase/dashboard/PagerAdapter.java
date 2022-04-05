package com.example.chatappfirebase.dashboard;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatappfirebase.chat.ChatFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    int tabcount = 1;

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        Log.d("PagerAdapter", "PagerAdapter: called");
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            Log.d("tab1", "ChatFragment");
            return new ChatFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
