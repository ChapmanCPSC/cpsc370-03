package edu.chapman.cpsc370.asdplaydate.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.chapman.cpsc370.asdplaydate.R;

public class ProfileActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        //Find Views By ID
        FloatingActionButton saveProfileButton = (FloatingActionButton) findViewById(R.id.fab_saveProfile);//find list view
        final TextView defaultSearchRadius = (TextView) findViewById(R.id.textViewSearchRadius);

        //Click Listeners
        saveProfileButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(ProfileActivity.this, SettingsActivity.class);
                ProfileActivity.this.startActivity(myIntent);
            }
        });
    }
}
