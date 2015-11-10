package edu.chapman.cpsc370.asdplaydate.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import edu.chapman.cpsc370.asdplaydate.R;

public class SettingsActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_settings);

        //Temp code while testing back butto
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);


        //Find Views By ID
        TextView logoutButton = (TextView) findViewById(R.id.textViewLogout);
        TextView profileButton = (TextView) findViewById(R.id.textViewEditProfile);

        //Click Listeners
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(SettingsActivity.this, ProfileActivity.class);
                SettingsActivity.this.startActivity(myIntent);
            }});

        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You have been logged out",
                        Toast.LENGTH_LONG).show();
            }});

    }
}
