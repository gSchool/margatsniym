package com.houkcorp.margatsniym.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.houkcorp.margatsniym.R;

public class MyUserFragment extends Fragment {

    public static MyUserFragment newInstance() {
        return new MyUserFragment();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_user, container, false);

        return root;
    }
}