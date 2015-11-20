package edu.chapman.cpsc370.asdplaydate.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.fragments.CreateAccountFragment;
import edu.chapman.cpsc370.asdplaydate.fragments.LoginFragment;
import edu.chapman.cpsc370.asdplaydate.fragments.SplashFragment;
import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;

public class AccountActivity extends FragmentActivity
{

    SessionManager sessionManager;
    private String sessionToken;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        sessionManager = new SessionManager(getApplicationContext());
        sessionToken = sessionManager.getSessionToken();
        if(sessionToken != null)
        {
            loadSplash();
            ASDPlaydateUser.becomeInBackground(sessionToken, new LogInCallback()
            {
                @Override
                public void done(ParseUser user, ParseException e)
                {
                    if(user != null && e == null)
                    {
                        startActivity(new Intent(AccountActivity.this, MainActivity.class));
                        finish();
                    }
                }
            });
        }
        else {
            loadFragment();
        }
        
    }

    void loadSplash()
    {
        Fragment f = new SplashFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentView, f);
        fragmentTransaction.commit();
    }

    void loadFragment()
    {
        Fragment f = new CreateAccountFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentView, f);
        fragmentTransaction.commit();

    }
    public void loadLogin()
    {
        Fragment f = new LoginFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentView, f);
        fragmentTransaction.commit();
    }

}
