package edu.chapman.cpsc370.asdplaydate.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.activities.MainActivity;
import edu.chapman.cpsc370.asdplaydate.activities.ProfileActivity;
import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;

/**
 * Created by TheHollowManV on 11/4/2015.
 */
public class LoginFragment extends Fragment
{
    private Button loginBtn;
    private EditText email;
    private EditText password;
    private ProgressDialog progressDialog;

    SessionManager sessionManager;

    public LoginFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        sessionManager = new SessionManager(getActivity().getApplicationContext());

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        loginBtn = (Button) rootView.findViewById(R.id.loginButton);
        email = (EditText) rootView.findViewById(R.id.loginEmail);
        password = (EditText) rootView.findViewById(R.id.loginPassword);

        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                login();
            }
        });

        return rootView;
    }

    private void login()
    {
        // Show progress dialog
        progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true);

        // Attempt to log in with entered email and password
        ASDPlaydateUser.logInInBackground(email.getText().toString(), password.getText().toString(),
                new UserLogInCallback());

    }

    private class UserLogInCallback implements LogInCallback
    {
        @Override
        public void done(ParseUser user, ParseException e) {

            if(e == null)
            {
                progressDialog.dismiss();

                if(user != null)
                {
                    sessionManager.storeSessionToken(user.getSessionToken());

                    // If there are no errors, go to main activity
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(intent);
                }

            }
            else
            {
                // Show an error message
                Toast.makeText(getActivity(), "Invalid email/password combination",
                        Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }
        }
    }
}
