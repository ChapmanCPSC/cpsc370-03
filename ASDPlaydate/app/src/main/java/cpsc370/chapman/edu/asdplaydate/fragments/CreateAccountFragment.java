package cpsc370.chapman.edu.asdplaydate.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cpsc370.chapman.edu.asdplaydate.R;
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
        return rootView;
    }

}
