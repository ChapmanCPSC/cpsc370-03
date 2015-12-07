package edu.chapman.cpsc370.asdplaydate.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.activities.AccountActivity;
import edu.chapman.cpsc370.asdplaydate.activities.ChatActivity;
import edu.chapman.cpsc370.asdplaydate.activities.ProfileActivity;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;

/**
 * Created by TheHollowManV on 11/4/2015.
 */

public class CreateAccountFragment extends Fragment
{
    EditText email;
    EditText pass;
    EditText passCom;
    TextView link;
    Button createAccountBtn;

    ProgressDialog progressDialog;

    public static final String ACCOUNT_EMAIL = "account_email";
    public static final String ACCOUNT_PASSWORD = "account_password";

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
        createAccountBtn = (Button)rootView.findViewById(R.id.button);

        createAccountBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Check if email field is empty
                String emailAddress = email.getText().toString().trim();
                if(emailAddress.equals("") || !emailAddress.contains("@") || emailAddress == null)
                {
                    Toast.makeText(getActivity(), getString(R.string.invalid_email), Toast.LENGTH_LONG).show();
                }
                // Check if password matches password confirmation
                else if(!pass.getText().toString().equals(passCom.getText().toString())
                        || pass.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getActivity(), getString(R.string.incorrect_passwords), Toast.LENGTH_LONG).show();
                }
                else
                {
                    checkEmail();
                }
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

    private void checkEmail() {

        ParseQuery<ASDPlaydateUser> query = new ParseQuery<>(ASDPlaydateUser.class);
        query.whereEqualTo(ASDPlaydateUser.ATTR_EMAIL, email.getText().toString());

        // Show progress dialog
        progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true);

        query.findInBackground(new FindCallback<ASDPlaydateUser>() {
            @Override
            public void done(List<ASDPlaydateUser> objects, ParseException e) {

                progressDialog.dismiss();

                // Check if an error occurred
                if(e != null)
                {
                    Toast.makeText(getActivity(), "An error occurred, please try again", Toast.LENGTH_LONG).show();
                    return;
                }

                // No results found, email is not in use
                if (objects.size() == 0)
                {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra(ACCOUNT_EMAIL, email.getText().toString());
                    intent.putExtra(ACCOUNT_PASSWORD, pass.getText().toString());

                    getActivity().startActivity(intent);
                }

                // Email is already in use
                else
                {
                    Toast.makeText(getActivity(), "Email is already in use", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
