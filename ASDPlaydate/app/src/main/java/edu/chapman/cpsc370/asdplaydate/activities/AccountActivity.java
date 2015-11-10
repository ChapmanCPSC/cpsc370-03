package edu.chapman.cpsc370.asdplaydate.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.fragments.CreateAccountFragment;
import edu.chapman.cpsc370.asdplaydate.fragments.LoginFragment;

public class AccountActivity extends FragmentActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        loadFragment();
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
