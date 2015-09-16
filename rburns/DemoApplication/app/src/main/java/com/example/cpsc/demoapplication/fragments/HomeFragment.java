package com.example.cpsc.demoapplication.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cpsc.demoapplication.MainActivity;
import com.example.cpsc.demoapplication.R;

/**
 * Created by cpsc on 9/16/15.
 */
public class HomeFragment extends Fragment
{
    public HomeFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity act)
    {
        super.onAttach(act);
        ((MainActivity) act).onSectionAttached(0);
    }
}
