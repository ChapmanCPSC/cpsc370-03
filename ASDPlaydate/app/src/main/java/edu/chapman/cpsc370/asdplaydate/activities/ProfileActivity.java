package edu.chapman.cpsc370.asdplaydate.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.chapman.cpsc370.asdplaydate.R;

public class ProfileActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Find Views By ID
        Button saveProfileButton = (Button) findViewById( R.id.buttonSaveProfile );//find list view

        //Click Listeners
        saveProfileButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(ProfileActivity.this, TempSettingsActivity.class);
                ProfileActivity.this.startActivity(myIntent);
            }
        });
    }


}
