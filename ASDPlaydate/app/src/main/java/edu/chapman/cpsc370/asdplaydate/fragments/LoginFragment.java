package edu.chapman.cpsc370.asdplaydate.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import edu.chapman.cpsc370.asdplaydate.MainActivity;
import edu.chapman.cpsc370.asdplaydate.R;

/**
 * Created by TheHollowManV on 11/4/2015.
 */
public class LoginFragment extends Fragment
{
    Button btn;
    public LoginFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        btn = (Button)rootView.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                login();
            }
        });
        return rootView;
    }

    void login()
    {
        Intent myIntent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(myIntent);
    }
}
