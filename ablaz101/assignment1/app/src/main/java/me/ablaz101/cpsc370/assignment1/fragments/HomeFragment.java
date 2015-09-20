package me.ablaz101.cpsc370.assignment1.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.ablaz101.cpsc370.assignment1.MainActivity;
import me.ablaz101.cpsc370.assignment1.R;

/**
 * Created by Xavi Ablaza on 9/19/15.
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
