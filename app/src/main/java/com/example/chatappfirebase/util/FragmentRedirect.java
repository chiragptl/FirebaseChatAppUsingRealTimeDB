package com.example.chatappfirebase.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chatappfirebase.R;

public class FragmentRedirect extends Fragment {
    static FragmentManager fragmentManager;
    static FragmentTransaction fragmentTransaction;

    public FragmentRedirect() {
    }

    public static void redirectToFragment(FragmentManager supportFragmentManager, Fragment fragment) {
        fragmentManager = supportFragmentManager;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.replace, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
