package edu.chapman.cpsc370.asdplaydate.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.chapman.cpsc370.asdplaydate.AccountActivity;
import edu.chapman.cpsc370.asdplaydate.R;
/**
 * Created by TheHollowManV on 11/4/2015.
 */

public class CreateAccountFragment extends Fragment
{
    EditText email;
    EditText pass;
    EditText passCom;
    TextView link;
    Button btn;
    public CreateAccountFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_create_account, container, false);
        email = (EditText)rootView.findViewById(R.id.editText);
        pass = (EditText)rootView.findViewById(R.id.editText2);
        passCom= (EditText)rootView.findViewById(R.id.editText3);
        link = (TextView)rootView.findViewById(R.id.textView);
        btn = (Button)rootView.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "TODO: Open Profile Page and Save Account Info",
                        Toast.LENGTH_LONG).show();
            }
        });
        link.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((AccountActivity)getActivity()).loadLogin();
            }
        });
        return rootView;
    }

}
